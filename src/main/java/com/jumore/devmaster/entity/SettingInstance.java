package com.jumore.devmaster.entity;

import com.jumore.dove.aop.AutoIncrease;
import com.jumore.dove.aop.Column;
import com.jumore.dove.aop.Entity;
import com.jumore.dove.aop.Id;
import com.jumore.dove.aop.Table;

@Entity
@Table(name = "dm_setting_instance")
public class SettingInstance {

    @Id
    @AutoIncrease
    private Long id;
    
    @Column(name="project_id")
    private Long projectId;
    
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name="setting_id")
    private Long settingId;
    
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
