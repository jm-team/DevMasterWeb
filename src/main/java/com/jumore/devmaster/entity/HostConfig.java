package com.jumore.devmaster.entity;

import com.jumore.dove.aop.*;

import java.util.Date;

@Entity
@Table(name="dm_host_config")
public class HostConfig {
    /***/
    @Column(name="id")
    @Id
    private Long id;

    /**ip地址*/
    @Column(name="ip")
    private String ip;

    /**别名*/
    @Column(name="alias")
    private String alias;

    /**属主id*/
    @Column(name="owner_id")
    private Long ownerId;

    /**属主姓名*/
    @Column(name="owner_name")
    private String ownerName;

    /**主机组*/
    @Column(name="host_group")
    private String hostGroup;

    /**用户名*/
    @Column(name="user_name")
    private String userName;

    /**登录方式PASSWORD/KEY*/
    @Column(name="login_type")
    private String loginType;

    /**密码*/
    @Column(name="password")
    private String password;

    /**密钥*/
    @Column(name="secret_key")
    private String secretKey;

    /**密钥密码*/
    @Column(name="secret_key_password")
    private String secretKeyPassword;

    /**端口*/
    @Column(name="port")
    private String port;

    /**sudo(Y/N)*/
    @Column(name="sudo")
    private String sudo;

    /**sudo密码*/
    @Column(name="sudo_password")
    private String sudoPassword;

    /**su(Y/N)*/
    @Column(name="su")
    private String su;

    /**su密码*/
    @Column(name="su_password")
    private String suPassword;

    /**状态（0：不正常；1：正常）*/
    @Column(name="status")
    private Integer status;

    /**备注*/
    @Column(name="remark")
    private String remark;

    /**是否删除（0：否；1：是）*/
    @Column(name="delete_flag")
    private Integer deleteFlag;

    /**创建人*/
    @Column(name="create_id")
    private Long createId;

    /**创建时间*/
    @Column(name="create_time")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName == null ? null : ownerName.trim();
    }

    public String getHostGroup() {
        return hostGroup;
    }

    public void setHostGroup(String hostGroup) {
        this.hostGroup = hostGroup == null ? null : hostGroup.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType == null ? null : loginType.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey == null ? null : secretKey.trim();
    }

    public String getSecretKeyPassword() {
        return secretKeyPassword;
    }

    public void setSecretKeyPassword(String secretKeyPassword) {
        this.secretKeyPassword = secretKeyPassword == null ? null : secretKeyPassword.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public String getSudo() {
        return sudo;
    }

    public void setSudo(String sudo) {
        this.sudo = sudo == null ? null : sudo.trim();
    }

    public String getSudoPassword() {
        return sudoPassword;
    }

    public void setSudoPassword(String sudoPassword) {
        this.sudoPassword = sudoPassword == null ? null : sudoPassword.trim();
    }

    public String getSu() {
        return su;
    }

    public void setSu(String su) {
        this.su = su == null ? null : su.trim();
    }

    public String getSuPassword() {
        return suPassword;
    }

    public void setSuPassword(String suPassword) {
        this.suPassword = suPassword == null ? null : suPassword.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}