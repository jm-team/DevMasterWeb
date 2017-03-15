package com.jumore.devmaster.common.util;

import org.apache.shiro.SecurityUtils;

import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.entity.DevMasterUser;

public class SessionHelper {

    public static DevMasterUser getUser() {
        DevMasterUser user = (DevMasterUser) SecurityUtils.getSubject().getSession().getAttribute(DevMasterConst.Session.Session_User_Key);
        return user;
    }
}
