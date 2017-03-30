package com.jumore.devmaster.entity;

import com.jumore.dove.aop.*;

@Entity
@Table(name="dm_dbentity")
public class DBEntity {
    /***/
    @Column(name="id")
    @Id
    @AutoIncrease
    private Long id;

    /**表名*/
    @Column(name="name")
    private String name;

    /**表注释*/
    @Column(name="remark")
    private String remark;

    /**数据库名*/
    @Column(name="database_name")
    private String databaseName;

    /**dm_project.id*/
    @Column(name="project_id")
    private Long projectId;
    
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
        this.name = name == null ? null : name.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName == null ? null : databaseName.trim();
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}