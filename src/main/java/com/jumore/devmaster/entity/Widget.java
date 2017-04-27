/**
 * Project Name:DevMasterWeb File Name:Widget.java Package
 * Name:com.jumore.devmaster.entity Copyright (c) 2017, JUMORE Co.,Ltd. All
 * Rights Reserved.
 *
 * @author 乔广
 * @date 2017年4月27日 下午4:03:53
 */
package com.jumore.devmaster.entity;

import java.util.Date;

import com.jumore.dove.aop.AutoIncrease;
import com.jumore.dove.aop.Column;
import com.jumore.dove.aop.Entity;
import com.jumore.dove.aop.Id;
import com.jumore.dove.aop.Table;

/**
 * Function: Widget
 * 
 * @author 乔广
 * @date 2017年4月27日 下午4:03:53
 * @version
 * @see
 */
@Entity
@Table(name = "dm_widget")
public class Widget {
    @Id
    @AutoIncrease
    private Long    id;

    @Column(name = "name")
    private String  name;

    @Column(name = "template")
    private String template;

    @Column(name = "create_time")
    private Date    createTime;

    /**
     * id
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * template
     *
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * createTime
     *
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
