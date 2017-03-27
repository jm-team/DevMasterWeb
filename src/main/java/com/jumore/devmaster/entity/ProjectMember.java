package com.jumore.devmaster.entity;

import com.jumore.dove.aop.AutoIncrease;
import com.jumore.dove.aop.Column;
import com.jumore.dove.aop.Entity;
import com.jumore.dove.aop.Id;
import com.jumore.dove.aop.Table;

@Entity
@Table(name = "dm_project_member")
public class ProjectMember {

    @Id
    @AutoIncrease
    private Long id;
    
    @Column(name="project_id")
    private Long projectId;
    
    @Column(name="member_uid")
    private Long memberUid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getMemberUid() {
        return memberUid;
    }

    public void setMemberUid(Long memberUid) {
        this.memberUid = memberUid;
    }
    
}
