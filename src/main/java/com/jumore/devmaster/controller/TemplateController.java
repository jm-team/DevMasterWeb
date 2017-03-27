package com.jumore.devmaster.controller;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.devmaster.entity.TemplateSetting;
import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@PublicMethod
@Controller
@RequestMapping(value = "/template")
public class TemplateController {

    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "/addTemplate")
    public ModelAndView addTemplate() throws Exception {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "doAddTemplate")
    public ResponseVo<Page<Project>> doAddTemplate(ProjectTemplate tpl) throws Exception {
        if (StringUtils.isEmpty(tpl.getTitle())) {
            throw new BusinessException("标题不能为空");
        }
        tpl.setCreateTime(new Date());
        tpl.setUid(SessionHelper.getUser().getId());
        tpl.setDeleteFlag(0);
        tpl.setScope(1);
        baseService.save(tpl);
        return ResponseVo.<Page<Project>> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/templateList")
    public ModelAndView templateList(Long scope) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("scope", scope);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "listTemplateData")
    public ResponseVo<Page<ProjectTemplate>> listTemplateData(Page<ProjectTemplate> page, Long scope) throws Exception {
        ParamMap pm = new ParamMap();
        if (1==scope) {
            pm.put("uid", "12");
        } else {
            pm.put("scope", 2);
        }
        page = baseService.findPageByParams(ProjectTemplate.class, page, "Template.listTemplate", pm);
        return ResponseVo.<Page<ProjectTemplate>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
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
    public ResponseVo<Page<TemplateSetting>> listSettingData(Page<TemplateSetting> page, Long scope) throws Exception {
        ParamMap pm = new ParamMap();
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
    public ResponseVo<Page<Project>> doAddSetting(TemplateSetting setting) throws Exception {
        if (StringUtils.isEmpty(setting.getName())) {
            throw new BusinessException("参数名称不能为空");
        }
        if (StringUtils.isEmpty(setting.getPlaceholder())) {
            throw new BusinessException("参数占位符不能为空");
        }
        baseService.save(setting);
        return ResponseVo.<Page<Project>> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
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
    public ResponseVo<Page<Project>> doChooseTemplate(Long projectId , Long tplId) throws Exception {
        Project projectPo = baseService.get(Project.class, projectId);
        projectPo.setTplId(tplId);
        baseService.update(projectPo);
        return ResponseVo.<Page<Project>> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }
}
