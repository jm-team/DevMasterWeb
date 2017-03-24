package com.jumore.devmaster.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jumore.devmaster.common.CodeMirrorModeContainer;
import com.jumore.devmaster.common.util.ConnectionUtil;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.DBEntity;
import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.devmaster.entity.EntityField;
import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@PublicMethod
@Controller
@RequestMapping(value = "/project")
public class ProjectController {

    @Autowired
    private BaseService baseService;

    @Autowired
    // private

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
        page = baseService.findPageByParams(Project.class, page, "Project.listProject", pm);
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
        if (StringUtils.isEmpty(project.getDbUrl())) {
            throw new RuntimeException("数据库连接不能为空");
        }
        if (StringUtils.isEmpty(project.getDbUserName())) {
            throw new RuntimeException("数据库账号不能为空");
        }
        if (StringUtils.isEmpty(project.getDbPassword())) {
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
        if (StringUtils.isEmpty(project.getDbUrl())) {
            throw new RuntimeException("数据库连接不能为空");
        }
        if (StringUtils.isEmpty(project.getDbUserName())) {
            throw new RuntimeException("数据库账号不能为空");
        }
        if (StringUtils.isEmpty(project.getDbPassword())) {
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

        if (addTableAndColumnInfo(po, "com.mysql.jdbc.Driver")) {
            return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
        }

        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.FAILED);
    }

    /**
     * 添加表与列数据
     * 
     * @param project Project
     * @param driverClass 驱动
     */
    @Transactional
    private boolean addTableAndColumnInfo(Project project, String driverClass) {
        ParamMap pm = new ParamMap();
        pm.put("projectId", project.getId());
        // 保存之前先删除老的数据
        baseService.execute("Entity.delField", pm);
        baseService.execute("Entity.delEntity", pm);

        try {
            Connection connection = ConnectionUtil.initConnection(driverClass, project.getDbUrl(), project.getDbUserName(),
                    project.getDbPassword());
            DatabaseMetaData metadata = connection.getMetaData();
            // 获取表信息
            ResultSet tables = metadata.getTables(null, null, null, null);

            List<DBEntity> dbEntityList = new ArrayList<>();
            DBEntity dbEntity;
            while (tables.next()) {
                dbEntity = new DBEntity();
                dbEntity.setDatabaseName(tables.getString("TABLE_CAT"));
                dbEntity.setName(tables.getString("TABLE_NAME"));// 表名
                dbEntity.setRemark(tables.getString("REMARKS"));// 表备注
                dbEntity.setProjectId(project.getId());
                dbEntityList.add(dbEntity);
            }
            if (!CollectionUtils.isEmpty(dbEntityList)) {
                // 保存表信息
                baseService.batchSave(dbEntityList);

                // 获取列信息
                List<EntityField> entityFieldList = getAllColumnInfoList(dbEntityList, metadata, project.getId());

                // 保存列信息
                baseService.batchSave(entityFieldList);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取所有表的列信息
     * 
     * @param entities
     * @param metadata
     * @param projectId
     * @return
     */
    private List<EntityField> getAllColumnInfoList(List<DBEntity> entities, DatabaseMetaData metadata, Long projectId) throws SQLException {
        List<EntityField> allList = new ArrayList<>();
        for (DBEntity entity : entities) {
            allList.addAll(getColumnInfoList(metadata, entity.getName(), entity.getId(), projectId));
        }
        return allList;
    }

    /**
     * 获取表的列信息
     * 
     * @param metadata
     * @param tableName
     * @param entityId
     * @param projectId
     * @return
     */
    private List<EntityField> getColumnInfoList(DatabaseMetaData metadata, String tableName, Long entityId, Long projectId)
            throws SQLException {
        List<EntityField> lst = new ArrayList<>();
        EntityField field;
        ResultSet columns = null;
        columns = metadata.getColumns(null, null, tableName, null);
        while (columns.next()) {
            field = new EntityField();
            field.setName(columns.getString("COLUMN_NAME"));
            field.setType(columns.getString("TYPE_NAME"));
            field.setLength(Integer.parseInt(columns.getString("COLUMN_SIZE")));
            field.setDefaultvalue(columns.getString("COLUMN_DEF"));
            field.setDocs(columns.getString("REMARKS"));
            field.setDbentityId(entityId);
            field.setProjectId(projectId);
            lst.add(field);
        }

        return lst;
    }

    @RequestMapping("/index")
    public ModelAndView index(Long tplId) {
        ModelAndView mv = new ModelAndView();
        ProjectTemplate tplPo = baseService.get(ProjectTemplate.class, tplId);
        DevMasterUser user = SessionHelper.getUser();
        if (user.getId() != tplPo.getUid()) {
            mv.addObject("readonly", true);
        }
        mv.addObject("tplId", tplId);
        return mv;
    }

    /**
     * getTemplateFiles:(这里用一句话描述这个方法的作用).
     * 
     * @author Administrator
     * @date 2017年3月23日 上午10:30:39
     * @param id 要展开文件夹的相对路径
     * @param tplId 模板Id。当id为空时，tplId为要展开目录的相对路径
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getTemplateFiles")
    public JSONArray getTemplateFiles(String id, Long tplId) throws Exception {
        String path = org.apache.commons.lang.StringUtils.isBlank(id) ? String.valueOf(tplId) : id;
        String dirStr = SessionHelper.getAbsolutePath(path);
        File dir = new File(dirStr);

        if (!dir.exists()) {
            FileUtils.forceMkdir(dir);
        }

        JSONArray arr = new JSONArray();

        if (org.apache.commons.lang.StringUtils.isBlank(id)) {
            JSONObject root = new JSONObject();
            ProjectTemplate tplPo = baseService.get(ProjectTemplate.class, tplId);

            root.put("text", tplPo.getTitle());
            root.put("id", tplId);
            root.put("state", "closed");
            root.put("folder", true);
            arr.add(root);

            return arr;
        }

        File[] files = dir.listFiles();

        for (File file : files) {
            JSONObject obj = new JSONObject();
            obj.put("text", file.getName());
            obj.put("folder", file.isDirectory());
            obj.put("id", getRelativePath(file));

            if (file.isDirectory()) {
                obj.put("state", "closed");
            } else {
                obj.put("state", "open");
            }

            arr.add(obj);
        }

        return arr;
    }

    @ResponseBody
    @RequestMapping(value = "addFile")
    public ResponseVo<String> addFile(Long tplId, String parent, String newFileName, boolean isFile) throws Exception {
        String absolutePath = SessionHelper.getAbsolutePath(parent + File.separator + newFileName);
        File newFile = new File(absolutePath);

        if (newFile.exists()) {
            return ResponseVo.<String> BUILDER().setDesc("文件名重复").setCode(Const.BUSINESS_CODE.FAILED);
        }

        try {
            if (isFile) {
                newFile.createNewFile();
            } else {
                FileUtils.forceMkdir(newFile);
            }
        } catch (Exception ex) {
            return ResponseVo.<String> BUILDER().setDesc(ex.getMessage()).setCode(Const.BUSINESS_CODE.FAILED);
        }

        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "deleteFile")
    public ResponseVo<String> deleteFile(Long tplId, String fileName) throws Exception {
        String workDir = SessionHelper.getUserWorkDir();
        String tplDir = workDir + "tpls" + File.separator;
        File file = new File(tplDir + fileName);
        FileUtils.deleteQuietly(file);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping("/rename")
    public ResponseVo<String> rename(String oldNameId, String newName) {
        String absolutePath = SessionHelper.getAbsolutePath(oldNameId);
        File file = new File(absolutePath);

        int index = file.getAbsolutePath().lastIndexOf(file.separator);
        String newFileName = index == -1 ? newName : file.getAbsolutePath().substring(0, index) + File.separator + newName;
        File newFile = new File(newFileName);

        boolean success = file.renameTo(newFile);

        return ResponseVo.<String> BUILDER().setCode(success ? Const.BUSINESS_CODE.SUCCESS : Const.BUSINESS_CODE.FAILED)
                .setData(getRelativePath(newFile));
    }

    @RequestMapping("/codemirror")
    public ModelAndView codemirror(String path) throws IOException {
        ModelAndView mv = new ModelAndView();

        if (org.apache.commons.lang.StringUtils.isNotBlank(path)) {
            String filePath = SessionHelper.getAbsolutePath(path);
            File file = new File(filePath);
            String content = FileUtils.readFileToString(file);
            
            if(!StringUtils.isEmpty(content)){
                content = content.replaceAll("</textarea>", "&lt;/textarea&gt;");
            }
            
            mv.addObject("content", content);
            mv.addObject("path", path);
            mv.addObject("fileName", file.getName());
            mv.addObject("mode", CodeMirrorModeContainer.get(file));
        }

        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "save")
    public ResponseVo<String> save(String path, String content) {
        try {
            path = SessionHelper.getAbsolutePath(path);
            File file = new File(path);
            FileUtils.write(file, content);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.FAILED);
        }

        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    private String getRelativePath(File file) {
        if (file == null) {
            return org.apache.commons.lang.StringUtils.EMPTY;
        }

        return file.getAbsolutePath().replace('\\', '/').replace(SessionHelper.getAbsolutePath(),
                org.apache.commons.lang.StringUtils.EMPTY);
    }
}
