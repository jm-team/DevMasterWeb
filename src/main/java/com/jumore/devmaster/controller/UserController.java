package com.jumore.devmaster.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.MD5;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "/spList")
    public ModelAndView index(String returnUrl) throws Exception {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "listSpData")
    public ResponseVo<Page<Map>> listSpData(Page<Map> page) throws Exception {
        ParamMap pm = new ParamMap();
        pm.put("type", DevMasterConst.UserType.ServiceProvider);
        pm.put("deleteFlag", DevMasterConst.Flag.NotDelete);
        page = baseService.findPageByParams(page, "User.listUser", pm);
        return ResponseVo.<Page<Map>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/addUser")
    public ModelAndView addUser() throws Exception {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/doAddUser")
    public ResponseVo<String> doAddUser(DevMasterUser user) throws Exception {
        if (StringUtils.isEmpty(user.getAccount())) {
            throw new RuntimeException("用户名不能为空");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        user.setPassword(MD5.md5(user.getPassword()));
//        user.setCreateTime(new Date());
        user.setDeleteFlag(DevMasterConst.Flag.NotDelete);
        baseService.save(user);
        return ResponseVo.<String> BUILDER().setData("").setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/devList")
    public ModelAndView devList(String returnUrl) throws Exception {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "listDevData")
    public ResponseVo<Page<Map>> listDevData(Page<Map> page) throws Exception {
        ParamMap pm = new ParamMap();
        pm.put("type", 1);
        page = baseService.findPageByParams(page, "User.listUser", pm);
        return ResponseVo.<Page<Map>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    
    @ResponseBody
    @RequestMapping(value = "/delete")
    public ResponseVo<String> delete(Long id) throws Exception {
        DevMasterUser po = baseService.get(DevMasterUser.class, id);
        if (po == null || po.getDeleteFlag() == DevMasterConst.Flag.Delete) {
            throw new RuntimeException("用户不存在或已删除");
        }
        po.setDeleteFlag(DevMasterConst.Flag.Delete);
        baseService.update(po);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }


}
