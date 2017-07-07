package com.jumore.devmaster.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.entity.HttpAPI;
import com.jumore.devmaster.service.HttpAPIService;
import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.common.validator.DoveValidator;
import com.jumore.dove.controller.base.BaseController;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@PublicMethod
@Controller
@RequestMapping(value = "/api")
public class ApiController extends BaseController {

    @Autowired
    private HttpAPIService   httpAPIService;
    
    @RequestMapping(value = "/apiList")
    public ModelAndView componentList(Long projectId){
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "listApiData")
    public ResponseVo<Page<Map>> listComponentData(Page<Map> page, Long projectId,String url){
        ParamMap pm = new ParamMap();
        pm.put("projectId", projectId);
        pm.put("url", url);
        page = httpAPIService.findPageByParams(page, "HttpAPI.listHttpAPI", pm);
        return ResponseVo.<Page<Map>>BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @RequestMapping(value = "/addApi")
    public ModelAndView addApi(Long projectId){
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        return mv;
    }
    
    @RequestMapping(value = "/doAddApi")
    public ModelAndView doAddApi(HttpAPI api){
        ModelAndView mv = new ModelAndView();
        boolean exsit = DoveValidator.exsits(HttpAPI.class, new String[]{"projectId" , "url"}, new Object[]{api.getProjectId() , api.getUrl()});
        if(exsit){
            throw new BusinessException("存在相同url的接口");
        }
        httpAPIService.save(api);
        return mv;
    }
    
    @RequestMapping(value = "/editApi")
    public ModelAndView editApi(Long apiId){
        ModelAndView mv = new ModelAndView();
        return mv;
    }
}
