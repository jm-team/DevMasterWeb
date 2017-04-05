package com.jumore.devmaster.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.common.util.PathUtils;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.devmaster.entity.TemplateSetting;
import com.jumore.devmaster.service.TemplateService;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.service.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class TemplateServiceImpl extends BaseServiceImpl implements TemplateService {

	@Override
	@Transactional
	public void cloneTemplate(Long tplId, String newTplTitle) throws Exception {
        if (StringUtils.isEmpty(newTplTitle)) {
            throw new BusinessException("模板名称不能为空");
        }
        ProjectTemplate original = this.get(ProjectTemplate.class, tplId);
        ProjectTemplate tpl = new ProjectTemplate();
        tpl.setTitle(newTplTitle);
        tpl.setUid(SessionHelper.getUser().getId());
        ProjectTemplate po = (ProjectTemplate) this.getByExample(tpl);
        if (po != null) {
            throw new BusinessException("模板名称重复，请先修改模板名称");
        }

        tpl.setCreateTime(new Date());
        tpl.setDeleteFlag(DevMasterConst.Flag.NotDelete);

        tpl.setRemark(original.getRemark());
        tpl.setScope(DevMasterConst.Scope.Private);
        tpl.setUpdateTime(new Date());
        tpl.setExts(original.getExts());
        this.save(tpl);

        // 克隆模板
        TemplateSetting settingVo = new TemplateSetting();
        settingVo.setTplId(original.getId());

        List<TemplateSetting> settingList = this.listByExample(settingVo);
        for (TemplateSetting setting : settingList) {
        	TemplateSetting newSetting = new TemplateSetting();
        	newSetting.setName(setting.getName());
        	newSetting.setPlaceholder(setting.getPlaceholder());
        	newSetting.setTplId(tpl.getId());
        	
        	this.save(newSetting);
        }

        // 拷贝文件
        String newTplDir = PathUtils.getTplDir(tpl.getId());
        String oldTplDir = PathUtils.getTplDir(tplId);
        FileUtils.copyDirectory(new File(oldTplDir), new File(newTplDir));
	}

    @Override
    @Transactional
    public void deleteTemplate(Long tplId) throws Exception {
        ProjectTemplate po = get(ProjectTemplate.class, tplId);
        //删除模板下面的参数配置
        TemplateSetting templateSetting = new TemplateSetting();
        templateSetting.setTplId(tplId);
        List<TemplateSetting> templateSettings = commonDao.listByExample(templateSetting);
        if(CollectionUtils.isNotEmpty(templateSettings)){
            List<Long> ids = Lists.transform(templateSettings, new Function<TemplateSetting, Long>() {
                @Override
                public Long apply(TemplateSetting templateSetting) {
                    return templateSetting.getId();
                }
            });
            commonDao.deleteByIds(TemplateSetting.class,ids);
        }

        if (po != null) {
            delete(po);
        }
        // 删除文件
        String tplDir = PathUtils.getTplDir(tplId);
        FileUtils.deleteQuietly(new File(tplDir));
    }
}