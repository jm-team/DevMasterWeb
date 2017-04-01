package com.jumore.devmaster.controller;

import com.jumore.devmaster.common.enums.BaseExceptionEnum;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

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
    public ResponseVo<Page<Project>> doAddTemplate(ProjectTemplate tpl) throws Exception {
        if (StringUtils.isEmpty(tpl.getTitle())) {
            throw new BusinessException(BaseExceptionEnum.TITLE_NOT_BE_NULL.getMsg());
        }
        tpl.setCreateTime(new Date());
        tpl.setUid(SessionHelper.getUser().getId());
        tpl.setDeleteFlag(0);
        tpl.setScope(1);
        baseService.save(tpl);
        return ResponseVo.<Page<Project>> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/componentList")
    public ModelAndView componentList(Long scope) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("scope", scope);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "listComponentData")
    public ResponseVo<Page<ProjectTemplate>> listComponentData(Page<ProjectTemplate> page, Long scope) throws Exception {
        ParamMap pm = new ParamMap();
        if (1==scope) {
            pm.put("uid", "12");
        } else {
            pm.put("scope", 2);
        }
        page = baseService.findPageByParams(ProjectTemplate.class, page, "Component.listPrivateComponent", pm);
        return ResponseVo.<Page<ProjectTemplate>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "setScope")
    public ResponseVo<String> setScope(Long tplId, Integer scope) throws Exception {
        ProjectTemplate tplPo = baseService.get(ProjectTemplate.class, tplId);
        if (scope == null) {
            throw new BusinessException(BaseExceptionEnum.STATUS_NO_EFFECT.getMsg());
        }
        tplPo.setScope(scope);
        baseService.update(tplPo);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

}
