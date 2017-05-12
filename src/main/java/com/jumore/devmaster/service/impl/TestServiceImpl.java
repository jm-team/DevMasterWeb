package com.jumore.devmaster.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.entity.ProjectMember;
import com.jumore.devmaster.service.TestService;
import com.jumore.dove.service.BaseServiceImpl;

/**
 * Created by Administrator on 2017/3/31.
 */
@Service
public class TestServiceImpl extends BaseServiceImpl implements TestService{
    
    @Transactional(rollbackFor={Exception.class})
    public void addProject(){
        Project project = new Project();
        project.setName("aaaa");
        project.setDbUrl("dbUrl");
        project.setDbUserName("dbUsername");
        project.setDbPassword("dbPassword");
        this.save(project);
    }
    
    @Transactional(rollbackFor={Exception.class})
    public void addProjectMember(){
        ProjectMember member = new ProjectMember();
        member.setProjectId(1000L);
        member.setMemberUid(222L);
        this.save(member);  
        if("".equals("")){
            // 抛出异常使事务回滚
            throw new RuntimeException("测试异常");
        }
    }
}
