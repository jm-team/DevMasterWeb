package com.jumore.devmaster.entity;

import java.util.Date;

import com.jumore.dove.aop.*;
import com.jumore.dove.common.validator.MaxLength;
import com.jumore.dove.common.validator.NotEmpty;

@Entity
@Table(name="dm_page_view")
public class PageView {
    /***/
    @Id
    @AutoIncrease
    private Long id;

    /**
     *整个系统唯一 
     */
    @NotEmpty
    @MaxLength(value=40)
    private String name;
    
    /***/
    private String content;

    private String remark;
    
    @Column(name="create_time")
    private Date createTime;
    
    @Column(name="last_update_time")
    private Date lastUpdateTime;
    

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
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}