package com.jumore.devmaster.controller;

import com.alibaba.fastjson.JSONObject;
import com.jumore.devmaster.common.enums.BaseExceptionEnum;
import com.jumore.devmaster.common.util.RSAKeyUtil;
import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.util.MD5;
import com.jumore.dove.util.RSAUtils;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "")
@PublicMethod
public class LoginController {

    @RequestMapping(value = "/login")
    public ModelAndView login(HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView();
        RSAKeyUtil.getRSAKey(request, mv);
        return mv;
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request) throws Exception {
        SecurityUtils.getSubject().logout();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/login");
        return mv;
    }
    
    @RequestMapping(value = "/getPublicKey")
    @ResponseBody
    public ResponseVo<String> getPublicKey(HttpServletRequest request) throws Exception {
        /*********   加密登录  begin *********************/
        HashMap<String, Object> map = RSAUtils.getKeys();
        //生成公钥和私钥
        RSAPublicKey publicKey = (RSAPublicKey) map.get(RSAUtils.RAS_Key_Public);
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get(RSAUtils.RAS_Key_Private);

        request.getSession().setAttribute(RSAUtils.RAS_Key_Private, privateKey);//私钥保存在session中，用于解密

        //公钥信息保存在页面，用于加密
        String publicKeyExponent = publicKey.getPublicExponent().toString(16);
        String publicKeyModulus = publicKey.getModulus().toString(16);
        JSONObject data = new JSONObject();
        data.put("publicKeyExponent", publicKeyExponent);
        data.put("publicKeyModulus", publicKeyModulus);
        return ResponseVo.<String> BUILDER().setData(data.toJSONString()).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/doLogin")
    @ResponseBody
    public ResponseVo<String> doLogin(HttpServletRequest request, String username, String password) throws Exception {
        RSAPrivateKey privateKey = (RSAPrivateKey) request.getSession().getAttribute(RSAUtils.RAS_Key_Private);
        // 解密后的密码,password是提交过来的密码
        String descrypedPwd = RSAUtils.decryptByPrivateKey(password, privateKey);
        UsernamePasswordToken token = new UsernamePasswordToken(username, MD5.md5(descrypedPwd));
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(token);
            request.getSession().removeAttribute(RSAUtils.RAS_Key_Private);
            return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
        } catch (AuthenticationException ex) {
            return ResponseVo.<String> BUILDER().setDesc(BaseExceptionEnum.PASSWD_FAIL.getMsg()).setCode(Const.BUSINESS_CODE.FAILED);
        } catch (Exception ex) {
            return ResponseVo.<String> BUILDER().setDesc(BaseExceptionEnum.LOGIN_FAIL.getMsg()).setCode(Const.BUSINESS_CODE.FAILED);
        }
    }

}
