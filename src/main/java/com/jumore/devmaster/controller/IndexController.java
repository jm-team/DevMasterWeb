package com.jumore.devmaster.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@Controller
@RequestMapping(value = "")
public class IndexController {


    @RequestMapping(value = "index")
    public ModelAndView index(String returnUrl) throws Exception {
        ModelAndView mv = new ModelAndView();
        DevMasterUser user = SessionHelper.getUser();
        if(user!=null){
            mv.addObject("username", user.getAccount());
        }
        return mv;
    }

    @PublicMethod
    @RequestMapping(value = "layout")
    public ModelAndView layout() throws Exception {
        ModelAndView mv = new ModelAndView();
        // 获取组件列表
        return mv;
    }
    
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "listAppData")
    public ResponseVo<Page<Map>> listAppData(Page<Map> page) throws Exception {
//        page = spaService.findPageByParams(page, "App.listApp", new ParamMap());
        return ResponseVo.<Page<Map>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

}
