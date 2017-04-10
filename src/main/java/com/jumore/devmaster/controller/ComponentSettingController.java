package com.jumore.devmaster.controller;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.common.enums.BaseExceptionEnum;
import com.jumore.devmaster.common.util.PathUtils;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.FrontComponent;
import com.jumore.devmaster.entity.FrontComponentSetting;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@Controller
@RequestMapping(value = "/component/setting")
public class ComponentSettingController {

    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "/addComponentSetting")
    public ModelAndView addComponentSetting(Long compId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("compId", compId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "doAddComponentSetting")
    public ResponseVo<String> doAddComponentSetting(FrontComponentSetting setting) throws Exception {
        baseService.save(setting);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/componentSettingList")
    public ModelAndView componentSettingList(Long compId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("compId", compId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "listComponentSettingData")
    public ResponseVo<Page<FrontComponentSetting>> listComponentData(Page<FrontComponentSetting> page, Long compId) throws Exception {
        ParamMap pm = new ParamMap();
        pm.put("compId", compId);
        page = baseService.findPageByParams(FrontComponentSetting.class, page, "Component.listComponentSetting", pm);
        return ResponseVo.<Page<FrontComponentSetting>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @ResponseBody
    @RequestMapping(value = "delete")
    public ResponseVo<String> delete(Long compId) throws Exception {
        FrontComponentSetting po = baseService.get(FrontComponentSetting.class, compId);
        if(po!=null){
            baseService.delete(po);
        }
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    private void validateComponent(FrontComponent comp) {
        if (StringUtils.isEmpty(comp.getName())) {
            throw new BusinessException(BaseExceptionEnum.TITLE_NOT_BE_NULL.getMsg());
        }
        if (StringUtils.isEmpty(comp.getGroupId())) {
            throw new BusinessException(BaseExceptionEnum.COMP_GROUPID_NOT_BE_NULL.getMsg());
        }
        if (StringUtils.isEmpty(comp.getVersion())) {
            throw new BusinessException(BaseExceptionEnum.COMP_VERSION_NOT_BE_NULL.getMsg());
        }
    }

}
