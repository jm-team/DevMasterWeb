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
    @Column(name="default_value")
    private String defaultValue;

    /***/
    @Column(name="docs")
    private String docs;

    /**dm_dbentity.id*/
    @Column(name="dbentity_id")
    private Long dbentityId;

    /**dm_project.id*/
    @Column(name="project_id")
    private Long projectId;

    /**1是，0否*/
    @Column(name="primary_key")
    private Integer primaryKey;
    
    @Column(name="allow_null")
    private String allowNull;
    
    /**是否表单输入
     * 1需要，0不需要
    */
    @Column(name="show_input")
    private Integer showInput;
    
    @Column(name="input_label")
    private String inputLabel;
    
    public Integer getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Integer primaryKey) {
        this.primaryKey = primaryKey;
    }


    public String getAllowNull() {
        return allowNull;
    }

    public void setAllowNull(String allowNull) {
        this.allowNull = allowNull;
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


    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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

    public Integer getShowInput() {
        return showInput;
    }

    public void setShowInput(Integer showInput) {
        this.showInput = showInput;
    }

    public String getInputLabel() {
        return inputLabel;
    }

    public void setInputLabel(String inputLabel) {
        this.inputLabel = inputLabel;
    }
}