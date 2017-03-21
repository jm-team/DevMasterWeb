package com.jumore.devmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.entity.Project;
import com.jumore.dove.aop.annotation.PublicMethod;
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

    @RequestMapping(value = "/editTpl")
    public ModelAndView editTpl(Long id) throws Exception {
        ModelAndView mv = new ModelAndView();
        return mv;
    }
    
    @RequestMapping(value = "/listTemplate")
    public ModelAndView listTemplate(Long id) throws Exception {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "listTemplateData")
    public ResponseVo<Page<Project>> listProjectData(Page<Project> page , String scope) throws Exception {
        ParamMap pm = new ParamMap();
        if("private".equals(scope)){
            pm.put("uid", "12");
        }else{
            pm.put("scope", 2);
        }
        page = baseService.findPageByParams(Project.class , page, "Template.listTemplate", pm);
        return ResponseVo.<Page<Project>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

}
