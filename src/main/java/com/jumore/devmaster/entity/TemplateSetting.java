package com.jumore.devmaster.entity;

import com.jumore.dove.aop.AutoIncrease;
import com.jumore.dove.aop.Entity;
import com.jumore.dove.aop.Id;
import com.jumore.dove.aop.Table;

@Entity
@Table(name = "dm_template_setting")
public class TemplateSetting {

    @Id
    @AutoIncrease
    private Long id;
    
    private String name;
    
    private String placeholder;

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

}
