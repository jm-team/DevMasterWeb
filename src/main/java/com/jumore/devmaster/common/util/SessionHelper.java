package com.jumore.devmaster.common.util;

import java.io.File;

import org.apache.shiro.SecurityUtils;

import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.common.model.Configuration;
import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.dove.util.SpringContextHelper;

public class SessionHelper {
    
    private static Configuration cfg;
    
    public static DevMasterUser getUser() {
        DevMasterUser user = (DevMasterUser) SecurityUtils.getSubject().getSession().getAttribute(DevMasterConst.Session.Session_User_Key);
        return user;
    }
    
    public static String getUserWorkDir(){
        if(cfg==null){
            cfg = SpringContextHelper.getBean(Configuration.class);
        }
        return cfg.getDataPath()+File.separator+getUser().getAccount()+File.separator;
    }
    
    /**
     * 聚灵通url
     * @return
     */
    public static String getJltUrl(){
        if(cfg==null){
            cfg = SpringContextHelper.getBean(Configuration.class);
        }

        return cfg.getJltUrl();
    }
}
