package com.jumore.devmaster.service;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.common.log.LogHelper;
import com.jumore.dove.service.BaseService;

/**
 *.
 */
public class DevMasterRealm extends AuthorizingRealm {

    protected final LogHelper logHelper = LogHelper.getLogger(this.getClass());

    @Autowired
    BaseService               baseService;

    /**
     * 为当前登录的Subject授予角色和权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
        // DevMasterUser user = SessionHelper.getUser();
        // if(user.getType()==DevMasterConst.UserType.Admin){
        // simpleAuthorInfo.addRole("admin");
        // }
        return simpleAuthorInfo;
    }

    /**
     * 验证当前登录的Subject.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        logHelper.getBuilder().debug("验证当前Subject时获取到token为" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));
        DevMasterUser vo = new DevMasterUser();
        vo.setAccount(token.getUsername());
        vo.setPassword(String.valueOf(token.getPassword()));
        DevMasterUser userPo = (DevMasterUser) baseService.getByExample(vo);
        if (userPo == null) {
            throw new BusinessException("账号密码不正确");
        }
        SecurityUtils.getSubject().getSession().setAttribute(DevMasterConst.Session.Session_User_Key, userPo);
        AuthenticationInfo authInfo = new SimpleAuthenticationInfo(userPo, userPo.getPassword(), this.getName());
        return authInfo;
    }
}
