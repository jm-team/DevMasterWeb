package com.jumore.devmaster.controller;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.common.enums.BaseExceptionEnum;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.FrontComponent;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@Controller
@RequestMapping(value = "/component")
public class ComponentController {

    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "/addComponent")
    public ModelAndView addComponent() throws Exception {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "doAddComponent")
    public ResponseVo<String> doAddTemplate(FrontComponent comp) throws Exception {
        if (StringUtils.isEmpty(comp.getName())) {
            throw new BusinessException(BaseExceptionEnum.TITLE_NOT_BE_NULL.getMsg());
        }
        if (StringUtils.isEmpty(comp.getGroupId())) {
            throw new BusinessException(BaseExceptionEnum.COMP_GROUPID_NOT_BE_NULL.getMsg());
        }
        if (StringUtils.isEmpty(comp.getVersion())) {
            throw new BusinessException(BaseExceptionEnum.COMP_VERSION_NOT_BE_NULL.getMsg());
        }
        comp.setCreateTime(new Date());
        comp.setUid(SessionHelper.getUser().getId());
        baseService.save(comp);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/componentList")
    public ModelAndView componentList(Long scope) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("scope", scope);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "listComponentData")
    public ResponseVo<Page<FrontComponent>> listComponentData(Page<FrontComponent> page, Long scope) throws Exception {
        ParamMap pm = new ParamMap();
        pm.put("uid", SessionHelper.getUser().getId());
        page = baseService.findPageByParams(FrontComponent.class, page, "Component.listPrivateComponent", pm);
        return ResponseVo.<Page<FrontComponent>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "publish")
    public ResponseVo<String> publish(Long tplId, Integer scope) throws Exception {
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

}
