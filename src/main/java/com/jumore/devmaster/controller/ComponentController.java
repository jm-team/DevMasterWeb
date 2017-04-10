package com.jumore.devmaster.controller;

import java.io.File;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.common.enums.BaseExceptionEnum;
import com.jumore.devmaster.common.model.Configuration;
import com.jumore.devmaster.common.util.PathUtils;
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

    @Autowired
    private Configuration cfg;
    
    @RequestMapping(value = "/addComponent")
    public ModelAndView addComponent() throws Exception {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "doAddComponent")
    public ResponseVo<String> doAddComponent(FrontComponent comp) throws Exception {
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

    @RequestMapping(value = "/editComponent")
    public ModelAndView editComponent(Long id) throws Exception {
        ModelAndView mv = new ModelAndView();
        FrontComponent compPo = baseService.get(FrontComponent.class, id);
        mv.addObject("comp", compPo);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "doUpdateComponent")
    public ResponseVo<String> doUpdateComponent(FrontComponent comp) throws Exception {
        validateComponent(comp);
        FrontComponent vo = new FrontComponent();
        vo.setGroupId(comp.getGroupId());
        vo.setName(comp.getName());
        vo.setVersion(comp.getVersion());
        FrontComponent compPo = (FrontComponent) baseService.getByExample(vo);
        if (compPo!=null && compPo.getId() != comp.getId()) {
            throw new BusinessException(BaseExceptionEnum.COMP_EXIST_WITH_SAME_NAME.getMsg());
        }

        compPo = baseService.get(FrontComponent.class, comp.getId());
        String oldRoot = PathUtils.getRootName(compPo);

        compPo.setGroupId(comp.getGroupId());
        compPo.setName(comp.getName());
        compPo.setRemark(comp.getRemark());
        compPo.setVersion(comp.getVersion());
        compPo.setUpdateTime(comp.getUpdateTime());
        baseService.update(compPo);

        // 修改文件夹名称
        String newRoot = PathUtils.getRootName(comp);
        File oldRootFile = new File(PathUtils.getComponentsDir() + oldRoot);
        File newRootFile = new File(PathUtils.getComponentsDir() + newRoot);
        oldRootFile.renameTo(newRootFile);
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
