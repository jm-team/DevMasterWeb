package com.jumore.devmaster.entity;

import com.jumore.dove.aop.AutoIncrease;
import com.jumore.dove.aop.Column;
import com.jumore.dove.aop.Entity;
import com.jumore.dove.aop.Id;
import com.jumore.dove.aop.Table;

@Entity
@Table(name = "dm_front_component_setting")
public class FrontComponentSetting {

    @Id
    @AutoIncrease
    private Long id;
    
    private String name;
    
    private String placeholder;
    
    @Column(name="default_value")
    private String defaultValue;
    
    /**
     * input/enum
     */
    private String type;
    
    /**
     * 如果是枚举值，枚举值列表
     * 使用,隔开
     */
    @Column(name="enum_list")
    private String enumList;
    
    @Column(name="comp_id")
    private Long compId;

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

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnumList() {
        return enumList;
    }

    public void setEnumList(String enumList) {
        this.enumList = enumList;
    }

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
    }

}
