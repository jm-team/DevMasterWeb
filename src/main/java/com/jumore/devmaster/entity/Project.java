package com.jumore.devmaster.entity;

import java.util.Date;

import com.jumore.dove.aop.AutoIncrease;
import com.jumore.dove.aop.Column;
import com.jumore.dove.aop.Entity;
import com.jumore.dove.aop.Id;
import com.jumore.dove.aop.Table;

@Entity
@Table(name = "dm_project")
public class Project {

    @Id
    @AutoIncrease
    private Long id;

    private String  name;

    @Column(name="owner_id")
    private Integer ownerId;

    @Column(name="create_time")
    private Date    createTime;

    private String  remark;

    @Column(name="db_url")
    private String  dbUrl;

    @Column(name="db_username")
    private String  dbUserName;

    @Column(name="db_password")
    private String  dbPassword;

    @Column(name="tpl_id")
    private Long tplId;
    
    @Column(name="tpl_setting_data")
    private String tplSettingData;
    
    @Column(name="generate_entity_ids")
    private String generateEntityIds;
    
    public String getGenerateEntityIds() {
        return generateEntityIds;
    }

    public void setGenerateEntityIds(String generateEntityIds) {
        this.generateEntityIds = generateEntityIds;
    }

    public Long getTplId() {
        return tplId;
    }

    public void setTplId(Long tplId) {
        this.tplId = tplId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getTplSettingData() {
        return tplSettingData;
    }

    public void setTplSettingData(String tplSettingData) {
        this.tplSettingData = tplSettingData;
    }

}
