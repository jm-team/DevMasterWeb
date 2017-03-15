package com.jumore.devmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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

@PublicMethod
@Controller
@RequestMapping(value = "/field")
public class FieldController {

    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "/fieldList")
    public ModelAndView fieldList(Long entityId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("entityId", entityId);
        return mv;
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "listFieldData")
    public ResponseVo<Page<EntityField>> listProjectData(Page<EntityField> page,Long projectId) throws Exception {
        ParamMap pm = new ParamMap();
        pm.put("projectId", projectId);
        page = baseService.findPageByParams(EntityField.class , page, "Field.listField", pm);
        return ResponseVo.<Page<EntityField>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }
}
