package com.jumore.devmaster.controller;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.eventbus.AsyncEventBus;
import com.jumore.devmaster.common.model.vo.DevMasterUserVO;
import com.jumore.devmaster.common.util.RSAKeyUtil;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.common.util.StringUtil;
import com.jumore.dove.cache.CacheService;
import com.jumore.dove.dao.CommonDao;
import com.jumore.dove.util.RSAUtils;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private BaseService baseService;

    @Resource
    private AsyncEventBus asyncEventBus;

    @Resource
    private CacheService cacheService;

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

    @ResponseBody
    @RequestMapping(value = "/resetPwd")
    public ModelAndView resetPwd(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        try {
            RSAKeyUtil.getRSAKey(request, mv);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return mv;
    }

    /**
     * 用户注册
     * @param request
     * @param userParam
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/doResetPwd")
    @ResponseBody
    public ResponseVo<String> doResetPwd(HttpServletRequest request, DevMasterUserVO userParam) throws Exception {
        DevMasterUser sessionUser = SessionHelper.getUser();
        if(sessionUser == null){
            return ResponseVo.<String> BUILDER().setDesc("获取用户信息失败").setCode(Const.BUSINESS_CODE.FAILED);
        }
        userParam.setMobile(sessionUser.getMobile());

        String msg = validateUserParam(userParam);
        if(StringUtil.isNotEmpty(msg)){
            return ResponseVo.<String> BUILDER().setDesc(msg).setCode(Const.BUSINESS_CODE.FAILED);
        }

        //解密后的密码,password是提交过来的密码
        RSAPrivateKey privateKey = (RSAPrivateKey)request.getSession().getAttribute(RSAUtils.RAS_Key_Private);
        String password = RSAUtils.decryptByPrivateKey(userParam.getPassword(), privateKey);
        userParam.setPassword(MD5.md5(password));

        DevMasterUser devMasterUser = new DevMasterUser();
        BeanUtils.copyProperties(userParam, devMasterUser);
        devMasterUser.setId(sessionUser.getId());
        try {
            baseService.update(devMasterUser);
            return ResponseVo.<String> BUILDER().setDesc("修改密码成功！").setCode(Const.BUSINESS_CODE.SUCCESS);
        } catch (Exception e){
            return ResponseVo.<String> BUILDER().setDesc(String.format("修改密码成功，%s", e.getMessage())).setCode(Const.BUSINESS_CODE.FAILED);
        }

    }

    /**
     * 注册参数验证
     * @param userParam
     * @return
     */
    private String validateUserParam(DevMasterUserVO userParam){
        String msg = "";
        if(StringUtil.isEmpty(userParam.getPassword())){
            msg += "密码不能为空！<br>";
        }

        if(StringUtil.isEmpty(userParam.getRePassword())){
            msg += "确认密码不能为空！<br>";
        }

        if(StringUtil.isNotEmpty(userParam.getPassword()) && StringUtil.isNotEmpty(userParam.getRePassword()) && !userParam.getPassword().equals(userParam.getRePassword())){
            msg += "两次密码不一致！<br>";
        }

        //校验短信校验
        String objType = DevMasterConst.CacheKey.SYS_KEY_MAIN + ":" + DevMasterConst.CacheKey.RESET_PWD;
        String smsCode = cacheService.getString(objType, userParam.getMobile());
        if(org.apache.commons.lang.StringUtils.isBlank(smsCode)){
            msg += "短信验证码已失效！<br>";
        }else if (!smsCode.equals(userParam.getSmsCode())) {
            msg += "短信验证码输入错误！<br>";
        }

        return msg;
    }
}
