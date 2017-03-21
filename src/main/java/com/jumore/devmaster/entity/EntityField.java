package com.jumore.devmaster.entity;

import com.jumore.dove.aop.*;

@Entity
@Table(name="dm_entity_field")
public class EntityField {
    /***/
    @Column(name="id")
    @Id
    @AutoIncrease
    private Long id;

    /***/
    @Column(name="name")
    private String name;

    /***/
    @Column(name="type")
    private String type;

    /***/
    @Column(name="length")
    private Integer length;

    /***/
    @Column(name="defaultValue")
    private String defaultvalue;

    /***/
    @Column(name="docs")
    private String docs;

    /**dm_dbentity.id*/
    @Column(name="dbentity_id")
    private Long dbentityId;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getDefaultvalue() {
        return defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue == null ? null : defaultvalue.trim();
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs == null ? null : docs.trim();
    }

    public Long getDbentityId() {
        return dbentityId;
    }

    public void setDbentityId(Long dbentityId) {
        this.dbentityId = dbentityId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}