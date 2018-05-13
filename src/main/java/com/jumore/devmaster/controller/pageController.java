package com.jumore.devmaster.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.entity.FrontComponent;
import com.jumore.devmaster.entity.PageView;
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
@RequestMapping(value = "/page")
public class pageController extends BaseController {

    @Autowired
    private BaseService   baseService;
    
    @RequestMapping(value = "/pageList")
    public ModelAndView pageList(Long projectId){
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "listPageData")
    public ResponseVo<Page<Map>> listPageData(Page<Map> page, Long projectId,String name){
        ParamMap pm = new ParamMap();
        pm.put("projectId", projectId);
        page = baseService.findPageByParams(page, "Page.listPage", pm);
        return ResponseVo.<Page<Map>>BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @RequestMapping(value = "/addPage")
    public ModelAndView addPage(Long projectId){
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        return mv;
    }
    
    @ResponseBody
    @RequestMapping(value = "/doAddPage")
    public ResponseVo<String> doAddPage(PageView pageView){
        boolean exsit = DoveValidator.exsits(PageView.class, new String[]{"projectId" , "name"}, new Object[]{pageView.getProjectId() , pageView.getName()});
        if(exsit){
            throw new BusinessException("存在同名页面");
        }
        pageView.setCreateTime(new Date());
        baseService.save(pageView);
        return ResponseVo.<String>BUILDER().setData("").setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @RequestMapping(value = "/editPage")
    public ModelAndView editApi(Long pageId){
        ModelAndView mv = new ModelAndView();
        PageView po = baseService.get(PageView.class, pageId);
        mv.addObject("page", po);
        return mv;
    }
    
    @ResponseBody
    @RequestMapping(value = "/doUpdatePage")
    public ResponseVo<String> doUpdatePage(PageView pageView){
        DoveValidator.commonValidate(pageView);
        PageView vo = new PageView();
        vo.setName(pageView.getName());
        vo.setProjectId(pageView.getProjectId());
        PageView po = (PageView) baseService.getByExample(vo);
        if(po!=null && !po.getId().equals(pageView.getId())){
            throw new BusinessException("存在项目的页面名称");
        }
        baseService.update(pageView);
        return ResponseVo.<String>BUILDER().setData("").setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @ResponseBody
    @RequestMapping(value = "/delete")
    public ResponseVo<String> delete(Long pageId){
        PageView po = baseService.get(PageView.class, pageId);
        if(po!=null){
            baseService.delete(po);
        }
        return ResponseVo.<String>BUILDER().setData("").setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @PublicMethod
    @RequestMapping(value = "webBuilder")
    public ModelAndView webBuilder(Long id , String type) throws Exception {
        ModelAndView mv = new ModelAndView();
        if("page".equals(type)){
            PageView po = baseService.get(PageView.class, id);
            mv.addObject("target", po);
        }else if("comp".equals(type)){
            FrontComponent fc = baseService.get(FrontComponent.class, id);
            mv.addObject("target", fc);
        }
        mv.addObject("type", type);
        // 获取组件列表
        return mv;
    }
    
    @ResponseBody
    @RequestMapping(value = "/doSavePageContent")
    public ResponseVo<String> doSavePageContent(Long pageId , String content){
        PageView po = baseService.get(PageView.class, pageId);
        if(po==null){
            throw new BusinessException("保存失败");
        }
        po.setContent(content);
        po.setLastUpdateTime(new Date());
        baseService.update(po);
        return ResponseVo.<String>BUILDER().setData("").setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @RequestMapping(value = "/view")
    public ModelAndView view(Long pageId){
        ModelAndView mv = new ModelAndView();
        PageView po = baseService.get(PageView.class, pageId);
        mv.addObject("page", po);
        return mv;
    }
}
