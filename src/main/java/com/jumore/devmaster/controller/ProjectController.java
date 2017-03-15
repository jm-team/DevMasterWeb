package com.jumore.devmaster.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.devmaster.entity.Project;
import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.MD5;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@PublicMethod
@Controller
@RequestMapping(value = "/project")
public class ProjectController {

    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "/projectList")
    public ModelAndView projectList() throws Exception {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "listProjectData")
    public ResponseVo<Page<Project>> listProjectData(Page<Project> page) throws Exception {
        ParamMap pm = new ParamMap();
        page = baseService.findPageByParams(Project.class , page, "Project.listProject", pm);
        return ResponseVo.<Page<Project>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/addProject")
    public ModelAndView addProject() throws Exception {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/doAddProject")
    public ResponseVo<String> doAddProject(Project project) throws Exception {
        if (StringUtils.isEmpty(project.getName())) {
            throw new RuntimeException("工程名不能为空");
        }
        if(StringUtils.isEmpty(project.getDbUrl())){
            throw new RuntimeException("数据库连接不能为空");
        }
        if(StringUtils.isEmpty(project.getDbUserName())){
            throw new RuntimeException("数据库账号不能为空");
        }
        if(StringUtils.isEmpty(project.getDbPassword())){
            throw new RuntimeException("数据库密码不能为空");
        }
        project.setCreateTime(new Date());
        baseService.save(project);
        return ResponseVo.<String> BUILDER().setData("").setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @RequestMapping(value = "/editProject")
    public ModelAndView editProject(Long id) throws Exception {
        ModelAndView mv = new ModelAndView();
        Project po = baseService.get(Project.class, id);
        mv.addObject("project", po);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/doUpdateProject")
    public ResponseVo<String> doUpdateProject(Project project) throws Exception {
        if (StringUtils.isEmpty(project.getName())) {
            throw new RuntimeException("工程名不能为空");
        }
        if(StringUtils.isEmpty(project.getDbUrl())){
            throw new RuntimeException("数据库连接不能为空");
        }
        if(StringUtils.isEmpty(project.getDbUserName())){
            throw new RuntimeException("数据库账号不能为空");
        }
        if(StringUtils.isEmpty(project.getDbPassword())){
            throw new RuntimeException("数据库密码不能为空");
        }
        Project po = baseService.get(Project.class, project.getId());
        po.setName(project.getName());
        po.setRemark(project.getRemark());
        po.setDbUserName(project.getDbUrl());
        po.setDbUrl(project.getDbUrl());
        po.setDbPassword(project.getDbPassword());
        baseService.update(po);
        return ResponseVo.<String> BUILDER().setData("").setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "/delete")
    public ResponseVo<String> delete(Long id) throws Exception {
        Project po = baseService.get(Project.class, id);
        if (po == null) {
            throw new RuntimeException("项目不存在或已删除");
        }
        baseService.delete(po);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "/syncDatabase")
    public ResponseVo<String> syncDatabase(Long id) throws Exception {
        Project po = baseService.get(Project.class, id);
        if (po == null) {
            throw new RuntimeException("项目不存在或已删除");
        }
        //TODO 使用jdbc获取数据库表字段信息保存到DBEntity和EntityField表中
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }
}
