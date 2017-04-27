/**
 * Project Name:DevMasterWeb File Name:WidgetController.java Package
 * Name:com.jumore.devmaster.controller Copyright (c) 2017, JUMORE Co.,Ltd. All
 * Rights Reserved.
 *
 * @author 乔广
 * @date 2017年4月27日 下午4:01:50
 */
package com.jumore.devmaster.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jumore.devmaster.common.util.StringUtil;
import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.entity.Widget;
import com.jumore.devmaster.validator.CommonValidator;
import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

/**
 * Function: 组件Controller
 * 
 * @author 乔广
 * @date 2017年4月27日 下午4:01:50
 * @version
 * @see
 */
@Controller
@RequestMapping(value = "/widget")
public class WidgetController {
    @Autowired
    private BaseService baseService;

    @PublicMethod
    @ResponseBody
    @RequestMapping(value = "/doAddWidget")
    public ResponseVo<String> doAddProject(Widget widget) throws Exception {
        if (StringUtils.isEmpty(widget.getName())) {
            throw new RuntimeException("组件名称不能为空");
        }

        if (StringUtils.isEmpty(widget.getTemplate())) {
            throw new RuntimeException("组件模板不能为空");
        }

        widget.setCreateTime(new Date());

        if (CommonValidator.isEntityExsit(Widget.class, new String[] { "name" }, new Object[] { widget.getName() })) {
            throw new RuntimeException("组件名称不能重复");
        }

        baseService.save(widget);

        return ResponseVo.<String> BUILDER().setData("").setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @SuppressWarnings("rawtypes")
    @PublicMethod
    @ResponseBody
    @RequestMapping(value = "/listAllWidgetData")
    public ResponseVo<List<Widget>> listAllWidgetData() throws Exception {
        Widget vo = new Widget();
        List<Widget> list = baseService.<Widget>listByExample(vo);
        return ResponseVo.<List<Widget>> BUILDER().setData(list).setCode(Const.BUSINESS_CODE.SUCCESS);
    }
}
