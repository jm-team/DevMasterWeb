package com.jumore.devmaster.entity;

import com.jumore.dove.aop.*;

@Entity
@Table(name="dm_http_api")
public class HttpAPI {
    /***/
    @Column(name="id")
    @Id
    @AutoIncrease
    private Long id;

    /**
     *整个系统唯一 
     */
    private String code;
    
    /***/
    private String url;

    private String docs;

    /**dm_project.id*/
    @Column(name="project_id")
    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

}