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

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jumore.devmaster.common.CodeMirrorModeContainer;
import com.jumore.devmaster.common.TreeIconClassContainer;
import com.jumore.devmaster.common.util.ConnectionUtil;
import com.jumore.devmaster.common.util.PathUtils;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.DBEntity;
import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.devmaster.entity.EntityField;
import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.devmaster.validator.CommonValidator;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

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
        if(CommonValidator.isEntityExsit(Project.class, new String[]{"name","ownerId"}, new Object[]{project.getName() , project.getOwnerId()})){
            throw new RuntimeException("工程名称不能重复");
        }
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
        po.setDbUserName(project.getDbUserName());
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
            throw new BusinessException("同步库表信息失败" , e);
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
        List<String> pkList = new ArrayList<String>();
        ResultSet set = metadata.getPrimaryKeys(null, null, tableName);
        while(set.next()){
            pkList.add(set.getString("COLUMN_NAME").toString());
        }
        while (columns.next()) {
            field = new EntityField();
            //{.BUFFER_LENGTH=7, .CHAR_OCTET_LENGTH=15, .COLUMN_DEF=12, .COLUMN_NAME=3, .COLUMN_SIZE=6, .
            //DATA_TYPE=4, .DECIMAL_DIGITS=8, .IS_AUTOINCREMENT=22, .IS_GENERATEDCOLUMN=23, .IS_NULLABLE=17, .NULLABLE=10, 
            //.NUM_PREC_RADIX=9, .ORDINAL_POSITION=16, .REMARKS=11, .SCOPE_CATALOG=18, .SCOPE_SCHEMA=19,
            //.SCOPE_TABLE=20, .SOURCE_DATA_TYPE=21, .SQL_DATA_TYPE=13, .SQL_DATETIME_SUB=14, .TABLE_CAT=0, .TABLE_NAME=2, .TABLE_SCHEM=1, .TYPE_NAME=5}
            field.setName(columns.getString("COLUMN_NAME"));
            field.setType(columns.getString("TYPE_NAME"));
            field.setLength(Integer.parseInt(columns.getString("COLUMN_SIZE")));
            field.setDefaultValue(columns.getString("COLUMN_DEF"));
            field.setDocs(columns.getString("REMARKS"));
            field.setAllowNull(columns.getString("IS_NULLABLE"));
            if(pkList.contains(field.getName())){
                field.setPrimaryKey(1);
            }
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
        String dirStr = PathUtils.getAbsolutePath(path);
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
            root.put("iconCls", TreeIconClassContainer.getIconClass(dir));
            arr.add(root);

            return arr;
        }

        File[] files = dir.listFiles();

        for (File file : files) {
            JSONObject obj = new JSONObject();
            obj.put("text", file.getName());
            obj.put("folder", file.isDirectory());
            obj.put("id", getRelativePath(file));
            obj.put("iconCls", TreeIconClassContainer.getIconClass(file));

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
        String absolutePath = PathUtils.getAbsolutePath(parent + File.separator + newFileName);
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
        String absolutePath = PathUtils.getAbsolutePath(oldNameId);
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
            String filePath = PathUtils.getAbsolutePath(path);
            File file = new File(filePath);
            if(!file.exists()){
                FileUtils.forceMkdir(file.getParentFile());
                file.createNewFile();
            }
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
            path = PathUtils.getAbsolutePath(path);
            File file = new File(path);
            FileUtils.write(file, content);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.FAILED);
        }

        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/picture")
    public void readPicture(String path, HttpServletResponse response) throws IOException{
        path = PathUtils.getAbsolutePath(path);
        File file = new File(path);
        
        response.setHeader("Content-Type","image/jped");
        response.getOutputStream().write(FileUtils.readFileToByteArray(file));
        response.flushBuffer();
    }
    
    private String getRelativePath(File file) {
        if (file == null) {
            return org.apache.commons.lang.StringUtils.EMPTY;
        }

        return file.getAbsolutePath().replace('\\', '/').replace(PathUtils.getAbsolutePath(),
                org.apache.commons.lang.StringUtils.EMPTY);
    }

    @ResponseBody
    @RequestMapping(value = "/upload")
    public ResponseVo<String> upload(@RequestParam(value = "uploadFile", required = false)MultipartFile file, String parent) throws IOException{
        if(org.apache.commons.lang.StringUtils.isBlank(parent) || file == null){
            throw new BusinessException("parent directory cannot be empty or file cannot be null");
        }
        
        String path = PathUtils.getAbsolutePath(parent);
        File dir = new File(path);
        
        if(!dir.exists() || !dir.isDirectory()){
            throw new BusinessException("parent directory is not exists or is not a directory");
        }
        
        File localFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
        
        if(localFile.exists()){
            throw new BusinessException("the file "+ file.getOriginalFilename() + " is already exists");
        }
        
        localFile.createNewFile();
        FileUtils.writeByteArrayToFile(localFile, file.getBytes());
        
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }
}
