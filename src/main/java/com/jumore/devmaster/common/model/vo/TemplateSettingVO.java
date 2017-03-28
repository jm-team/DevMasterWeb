package com.jumore.devmaster.common.model.vo;

public class TemplateSettingVO {

    private Long id;
    
    private String name;
    
    private String placeholder;
    
    private Long tplId;
    
    private String value;

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

    public Long getTplId() {
        return tplId;
    }

    public void setTplId(Long tplId) {
        this.tplId = tplId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
