package com.jumore.devmaster.controller;

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
import com.jumore.devmaster.common.model.vo.TemplateSettingVO;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.devmaster.entity.TemplateSetting;
import com.jumore.devmaster.service.ProjectGenerateService;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.controller.base.BaseController;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@Controller
@RequestMapping(value = "/template")
public class TemplateController extends BaseController{

    @Autowired
    private BaseService baseService;

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
            throw new BusinessException("标题不能为空");
        }
        if (StringUtils.isEmpty(tpl.getExts())) {
            throw new BusinessException("模板文件的扩展名不能为空");
        }
        tpl.setCreateTime(new Date());
        tpl.setUid(SessionHelper.getUser().getId());
        tpl.setDeleteFlag(0);
        tpl.setScope(1);
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
            throw new BusinessException("标题不能为空");
        }
        ProjectTemplate tplPo = baseService.get(ProjectTemplate.class, tpl.getId());
        if(tplPo==null){
            throw new BusinessException("模板不存在或已经删除");
        }
        tplPo.setTitle(tpl.getTitle());
        tplPo.setRemark(tpl.getRemark());
        tplPo.setExts(tpl.getExts());
        baseService.update(tplPo);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/templateList")
    public ModelAndView templateList(Long scope) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("scope", scope);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "listTemplateData")
    public ResponseVo<Page<Map>> listTemplateData(Page<Map> page, Long scope, String title) throws Exception {
        ParamMap pm = new ParamMap();
        if (1==scope) {
            DevMasterUser user = SessionHelper.getUser();
            pm.put("uid", user.getId());
        } else {
            pm.put("scope", 2);
        }
        pm.put("title", title);
        page = baseService.findPageByParams(page, "Template.listTemplate", pm);
        return ResponseVo.<Page<Map>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "setScope")
    public ResponseVo<String> setScope(Long tplId, Integer scope) throws Exception {
        ProjectTemplate tplPo = baseService.get(ProjectTemplate.class, tplId);
        if (scope == null) {
            throw new BusinessException("无效的状态设置");
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
        baseService.save(setting);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "deleteSetting")
    public ResponseVo<String> deleteSetting(Long settingId) throws Exception {
        TemplateSetting po = baseService.get(TemplateSetting.class, settingId);
        if(po!=null){
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
    public ResponseVo<String> doChooseTemplate(Long projectId , Long tplId) throws Exception {
        Project projectPo = baseService.get(Project.class, projectId);
        projectPo.setTplId(tplId);
        baseService.update(projectPo);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @RequestMapping(value = "/fullfillTemplate")
    public ModelAndView fullfillTemplate(Long projectId , String entityIds) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        Project projectPo = baseService.get(Project.class, projectId);
        if(projectPo.getTplId()==null){
            throw new BusinessException("请先设置工程模板");
        }
        projectPo.setGenerateEntityIds(entityIds);
        baseService.update(projectPo);
        
        TemplateSetting settingVo = new TemplateSetting();
        settingVo.setTplId(projectPo.getTplId());
        JSONObject tplSettingData = new JSONObject();
        if(!StringUtil.isEmpty(projectPo.getTplSettingData())){
            tplSettingData = JSON.parseObject(projectPo.getTplSettingData());
        }
        
        mv.addObject("tplSettingData", tplSettingData);
        List<TemplateSetting> settingList = baseService.listByExample(settingVo);
        
        List<TemplateSettingVO> settingVOList = new ArrayList<TemplateSettingVO>();
        for(TemplateSetting setting : settingList){
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
        
        String codePath = SessionHelper.getCodeGenerateDir(projectPo.getName());
        String tplPath = SessionHelper.getTplDir(projectPo.getTplId());
        codeGenerateService.generateCode(projectPo , tplPath, codePath);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @ResponseBody
    @RequestMapping(value = "doCloneTemplate")
    public ResponseVo<String> doChooseTemplate(Long tplId , String newTplTitle) throws Exception {
        if(StringUtils.isEmpty(newTplTitle)){
            throw new BusinessException("模板名称不能为空");
        }
        ProjectTemplate original = baseService.get(ProjectTemplate.class, tplId);
        ProjectTemplate tpl = new ProjectTemplate();
        tpl.setTitle(newTplTitle);
        tpl.setUid(SessionHelper.getUser().getId());
        ProjectTemplate po = (ProjectTemplate)baseService.getByExample(tpl);
        if(po!=null){
            throw new BusinessException("模板名称重复，你先修改模板名称");
        }
        
        tpl.setCreateTime(new Date());
        tpl.setDeleteFlag(DevMasterConst.Flag.NotDelete);
        
        tpl.setRemark(original.getRemark());
        tpl.setScope(DevMasterConst.Scope.Private);
        tpl.setUpdateTime(new Date());
        tpl.setExts(original.getExts());
        baseService.save(tpl);
        
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }
}
