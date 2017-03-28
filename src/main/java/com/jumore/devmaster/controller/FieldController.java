package com.jumore.devmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.entity.EntityField;
import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@Controller
@RequestMapping(value = "/field")
public class FieldController {
    
    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "/fieldList")
    public ModelAndView fieldList(Long projectId , Long entityId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        mv.addObject("entityId", entityId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "listFieldData")
    public ResponseVo<Page<EntityField>> listProjectData(Page<EntityField> page,Long projectId , Long entityId) throws Exception {
        ParamMap pm = new ParamMap();
        pm.put("projectId", projectId);
        pm.put("entityId", entityId);
        page = baseService.findPageByParams(EntityField.class , page, "Field.listField", pm);
        return ResponseVo.<Page<EntityField>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @RequestMapping(value = "/addField")
    public ModelAndView addField(Long projectId , Long entityId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        mv.addObject("entityId", entityId);
        return mv;
    }
    
    @ResponseBody
    @RequestMapping(value = "doAddField")
    public ResponseVo<Page<String>> doAddField(EntityField field) throws Exception {
        //validate
        baseService.save(field);
        return ResponseVo.<Page<String>> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }
}
