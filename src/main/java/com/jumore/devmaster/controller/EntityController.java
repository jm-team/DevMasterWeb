package com.jumore.devmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.entity.DBEntity;
import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@Controller
@RequestMapping(value = "/entity")
public class EntityController {

    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "/entityList")
    public ModelAndView entityList(Long projectId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        Project projectPo = baseService.get(Project.class, projectId);
        if(projectPo.getTplId()!=null){
            ProjectTemplate tplPo = baseService.get(ProjectTemplate.class, projectPo.getTplId());
            if(tplPo!=null){
                mv.addObject("tplName", tplPo.getTitle());
            }
        }
        
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "listEntityData")
    public ResponseVo<Page<DBEntity>> listProjectData(Page<DBEntity> page,Long projectId) throws Exception {
        ParamMap pm = new ParamMap();
        page.setPageSize(200);
        pm.put("projectId", projectId);
        page = baseService.findPageByParams(DBEntity.class , page, "Entity.listEntity", pm);
        return ResponseVo.<Page<DBEntity>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/addEntity")
    public ModelAndView addEntity(Long projectId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/doAddEntity")
    public ResponseVo<String> doAddEntity(DBEntity entity) throws Exception {
        if (StringUtils.isEmpty(entity.getName())) {
            throw new RuntimeException("表名不能为空");
        }
        baseService.save(entity);
        return ResponseVo.<String> BUILDER().setData("").setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @RequestMapping(value = "/editEntity")
    public ModelAndView editEntity(Long id) throws Exception {
        ModelAndView mv = new ModelAndView();
        DBEntity po = baseService.get(DBEntity.class, id);
        mv.addObject("entity", po);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/doUpdateEntity")
    public ResponseVo<String> doUpdateEntity(DBEntity entity) throws Exception {
        if (StringUtils.isEmpty(entity.getName())) {
            throw new RuntimeException("表名不能为空");
        }
        DBEntity po = baseService.get(DBEntity.class, entity.getId());
        po.setName(entity.getName());
        po.setRemark(entity.getRemark());
        baseService.update(po);
        return ResponseVo.<String> BUILDER().setData("").setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "/delete")
    public ResponseVo<String> delete(Long id) throws Exception {
        DBEntity po = baseService.get(DBEntity.class, id);
        if (po == null) {
            throw new RuntimeException("表不存在或已删除");
        }
        baseService.delete(po);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }


}
