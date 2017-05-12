package com.jumore.devmaster.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.common.enums.BaseExceptionEnum;
import com.jumore.devmaster.common.model.vo.TemplateSettingVO;
import com.jumore.devmaster.common.util.PathUtils;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.devmaster.entity.TemplateSetting;
import com.jumore.devmaster.service.ProjectGenerateService;
import com.jumore.devmaster.service.TemplateService;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.common.validator.DoveValidator;
import com.jumore.dove.controller.base.BaseController;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@Controller
@RequestMapping(value = "/template")
public class TemplateController extends BaseController {

    @Autowired
    private BaseService            baseService;

    @Autowired
    private TemplateService        templateService;

    @Autowired
    private ProjectGenerateService codeGenerateService;

    @RequestMapping(value = "/addTemplate")
    public ModelAndView addTemplate() throws Exception {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "doAddTemplate")
    public ResponseVo<String> doAddTemplate(ProjectTemplate tpl) throws Exception {
        if (StringUtils.isEmpty(tpl.getTitle())) {
            throw new BusinessException(BaseExceptionEnum.TITLE_NOT_BE_NULL.getMsg());
        }
        if (StringUtils.isEmpty(tpl.getExts())) {
            throw new BusinessException(BaseExceptionEnum.TEMPLATE_EXT_NOT_BE_NULL.getMsg());
        }
        tpl.setCreateTime(new Date());
        tpl.setUid(SessionHelper.getUser().getId());
        tpl.setDeleteFlag(0);
        // 默认私有
        if (tpl.getScope() == null) {
            tpl.setScope(DevMasterConst.Scope.Private);
        }
        baseService.save(tpl);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/editTemplate")
    public ModelAndView editTemplate(Long id) throws Exception {
        ModelAndView mv = new ModelAndView();
        ProjectTemplate tplPo = baseService.get(ProjectTemplate.class, id);
        mv.addObject("tpl", tplPo);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "doUpdateTemplate")
    public ResponseVo<String> doUpdateTemplate(ProjectTemplate tpl) throws Exception {
        if (StringUtils.isEmpty(tpl.getTitle())) {
            throw new BusinessException(BaseExceptionEnum.TITLE_NOT_BE_NULL.getMsg());
        }
        ProjectTemplate tplPo = baseService.get(ProjectTemplate.class, tpl.getId());
        if (tplPo == null) {
            throw new BusinessException(BaseExceptionEnum.TEMPLATE_NOT_EXIST_OR_DELETED.getMsg());
        }
        // TODO 判断重名
        ProjectTemplate vo = new ProjectTemplate();
        vo.setUid(tpl.getUid());
        vo.setTitle(tpl.getTitle());
        ProjectTemplate po = (ProjectTemplate) baseService.getByExample(vo);
        if (po != null && po.getId() != tpl.getId()) {
            throw new BusinessException(BaseExceptionEnum.TEMPLATE_EXIST_WITH_SAME_TITLE.getMsg());
        }
        String oldRoot = tplPo.getTitle();
        tplPo.setTitle(tpl.getTitle());

        tplPo.setRemark(tpl.getRemark());
        tplPo.setExts(tpl.getExts());
        tplPo.setEntityNameFunction(tpl.getEntityNameFunction());
        baseService.update(tplPo);
        // 修改文件夹名称
        String newRoot = tpl.getTitle();
        File oldRootFile = new File(PathUtils.getTplDir() + oldRoot);
        File newRootFile = new File(PathUtils.getTplDir() + newRoot);
        oldRootFile.renameTo(newRootFile);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/templateList")
    public ModelAndView templateList(Long scope) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("scope", scope);
        mv.addObject("myUid", SessionHelper.getUser().getId());
        return mv;
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "listTemplateData")
    public ResponseVo<Page<Map>> listTemplateData(Page<Map> page, Long scope, String title) throws Exception {
        ParamMap pm = new ParamMap();
        if (1 == scope) {
            DevMasterUser user = SessionHelper.getUser();
            pm.put("uid", user.getId());
        } else {
            pm.put("scope", 2);
        }
        pm.put("title", title);
        page = baseService.findPageByParams(page, "Template.listTemplate", pm);
        return ResponseVo.<Page<Map>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "listAvaliableTemplate")
    public ResponseVo<Page<Map>> listAvaliableTemplate(Page<Map> page) throws Exception {
        ParamMap pm = new ParamMap();
        pm.put("uid", SessionHelper.getUser().getId());
        page = baseService.findPageByParams(page, "Template.listAvaliableTemplate", pm);
        return ResponseVo.<Page<Map>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "setScope")
    public ResponseVo<String> setScope(Long tplId, Integer scope) throws Exception {
        ProjectTemplate tplPo = baseService.get(ProjectTemplate.class, tplId);
        if (scope == null) {
            throw new BusinessException(BaseExceptionEnum.STATUS_NO_EFFECT.getMsg());
        }
        tplPo.setScope(scope);
        baseService.update(tplPo);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/settingList")
    public ModelAndView settingList(Long tplId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("tplId", tplId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "listSettingData")
    public ResponseVo<Page<TemplateSetting>> listSettingData(Page<TemplateSetting> page, Long tplId) throws Exception {
        ParamMap pm = new ParamMap();
        pm.put("tplId", tplId);
        page = baseService.findPageByParams(TemplateSetting.class, page, "Template.listSetting", pm);
        return ResponseVo.<Page<TemplateSetting>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/addSetting")
    public ModelAndView addSetting(Long tplId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("tplId", tplId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "doAddSetting")
    public ResponseVo<String> doAddSetting(TemplateSetting setting) throws Exception {
        if (StringUtils.isEmpty(setting.getName())) {
            throw new BusinessException("参数名称不能为空");
        }
        if (StringUtils.isEmpty(setting.getPlaceholder())) {
            throw new BusinessException("参数占位符不能为空");
        }
        // 检查占位符是否重复
        boolean exsit = DoveValidator.exsits(TemplateSetting.class, new String[]{"tplId" , "placeholder"}, new Object[]{setting.getTplId() , setting.getPlaceholder()});
        if(exsit){
            throw new BusinessException("参数占位符"+setting.getPlaceholder()+"重复");
        }
        baseService.save(setting);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "deleteSetting")
    public ResponseVo<String> deleteSetting(Long settingId) throws Exception {
        TemplateSetting po = baseService.get(TemplateSetting.class, settingId);
        if (po != null) {
            baseService.delete(po);
        }
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/chooseTemplate")
    public ModelAndView chooseTemplate(Long projectId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("scope", "public");
        mv.addObject("projectId", projectId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "doChooseTemplate")
    public ResponseVo<String> doChooseTemplate(Long projectId, Long tplId) throws Exception {
        Project projectPo = baseService.get(Project.class, projectId);
        projectPo.setTplId(tplId);
        baseService.update(projectPo);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/fullfillTemplate")
    public ModelAndView fullfillTemplate(Long projectId, String entityIds) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        Project projectPo = baseService.get(Project.class, projectId);
        if (projectPo.getTplId() == null) {
            throw new BusinessException("请先设置工程模板");
        }
        projectPo.setGenerateEntityIds(entityIds);
        baseService.update(projectPo);

        TemplateSetting settingVo = new TemplateSetting();
        settingVo.setTplId(projectPo.getTplId());
        JSONObject tplSettingData = new JSONObject();
        if (!StringUtil.isEmpty(projectPo.getTplSettingData())) {
            tplSettingData = JSON.parseObject(projectPo.getTplSettingData());
        }

        mv.addObject("tplSettingData", tplSettingData);
        List<TemplateSetting> settingList = baseService.listByExample(settingVo);

        List<TemplateSettingVO> settingVOList = new ArrayList<TemplateSettingVO>();
        for (TemplateSetting setting : settingList) {
            TemplateSettingVO vo = new TemplateSettingVO();
            vo.setName(setting.getName());
            vo.setPlaceholder(setting.getPlaceholder());
            vo.setValue(tplSettingData.getString(setting.getPlaceholder()));
            settingVOList.add(vo);
        }
        mv.addObject("settingList", settingVOList);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "doGenerateCode")
    public ResponseVo<String> doGenerateCode(Long projectId) throws Exception {
        Project projectPo = baseService.get(Project.class, projectId);
        projectPo.setTplSettingData(JSON.toJSONString(this.getPageData()));
        baseService.update(projectPo);

        String codePath = PathUtils.getCodeGenerateDir(projectPo.getName());
        String tplPath = PathUtils.getTplDir(projectPo.getTplId());
        codeGenerateService.generateCode(projectPo, tplPath, codePath);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "doCloneTemplate")
    public ResponseVo<String> doCloneTemplate(Long tplId, String newTplTitle) throws Exception {
        templateService.cloneTemplate(tplId, newTplTitle);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    /**
     * 删除模板
     * 
     * @param tplId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "deleteTemplate")
    public ResponseVo<String> deleteTemplate(Long tplId) throws Exception {
        templateService.deleteTemplate(tplId);

        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }
}
