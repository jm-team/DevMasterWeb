package com.jumore.devmaster.controller;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.jumore.devmaster.common.util.RSAKeyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.util.MD5;
import com.jumore.dove.util.RSAUtils;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

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
            return ResponseVo.<String> BUILDER().setDesc("用户密码错误").setCode(Const.BUSINESS_CODE.FAILED);
        } catch (Exception ex) {
            return ResponseVo.<String> BUILDER().setDesc("登录失败").setCode(Const.BUSINESS_CODE.FAILED);
        }
    }

}
