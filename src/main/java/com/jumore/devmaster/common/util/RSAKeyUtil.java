package com.jumore.devmaster.common.util;

import com.jumore.dove.util.RSAUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/28.
 */
public class RSAKeyUtil {
    public static void getRSAKey(HttpServletRequest request, ModelAndView mv) throws NoSuchAlgorithmException {
        if(mv == null){
            return;
        }
        /*********   加密登录  begin *********************/
        HashMap<String, Object> map = RSAUtils.getKeys();
        //生成公钥和私钥
        RSAPublicKey publicKey = (RSAPublicKey) map.get(RSAUtils.RAS_Key_Public);
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get(RSAUtils.RAS_Key_Private);

        request.getSession().setAttribute(RSAUtils.RAS_Key_Private, privateKey);//私钥保存在session中，用于解密

        //公钥信息保存在页面，用于加密
        String publicKeyExponent = publicKey.getPublicExponent().toString(16);
        String publicKeyModulus = publicKey.getModulus().toString(16);
        mv.addObject("publicKeyExponent", publicKeyExponent);
        mv.addObject("publicKeyModulus", publicKeyModulus);
    }
}
