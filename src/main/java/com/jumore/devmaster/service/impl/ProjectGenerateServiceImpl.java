package com.jumore.devmaster.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jumore.devmaster.common.util.PathUtils;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.DBEntity;
import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.devmaster.service.CodeGenerateService;
import com.jumore.devmaster.service.ProjectGenerateService;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.service.BaseService;

@Component
public class ProjectGenerateServiceImpl implements ProjectGenerateService {

    @Autowired
    private BaseService baseService;
    
    @Autowired
    private CodeGenerateService codeGenerateService;
    
    @Override
    public void generateCode(Project project, String tplPath, String codePath) {

        if (StringUtils.isEmpty(project.getGenerateEntityIds())) {
            return;
        }
        String[] entityList = project.getGenerateEntityIds().split(",");
        JSONObject params = JSON.parseObject(project.getTplSettingData());
        tplPath = PathUtils.trimPathEnd(tplPath);
        // 删除原来文件
        FileUtils.deleteQuietly(new File(codePath));
        for(String entityId : entityList){
            if(StringUtils.isEmpty(entityId)){
                continue;
            }
            // 先生成目录
            generateDirs(project, tplPath, codePath, params , Long.valueOf(entityId));

            // 生成文件
            generateFiles(project , tplPath , codePath , params , Long.valueOf(entityId));
        }
        
        
    }

    private void generateFiles(Project project, String tplPath, String codePath, JSONObject params , Long entityId) {
        DBEntity entityPo = baseService.get(DBEntity.class, entityId);
        params.put("$[entityName]", toEntityName(entityPo.getName()));
        //生成实体类
        Object code = generateEntityClass(entityPo , null);
        params.put("$[entityCode]", code);
        
        // get all tpl files
        Collection<File> files = FileUtils.listFiles(new File(tplPath), null, true);

        ProjectTemplate tpl = baseService.get(ProjectTemplate.class, project.getTplId());
        if(StringUtils.isEmpty(tpl.getExts())){
            throw new BusinessException("没有设置模板支持的扩展名");
        }
        for (File file : files) {
            try {
                String dest = codePath + SessionHelper.getTplFileRelativePath(file.getAbsolutePath(), project.getTplId());
                dest = replacePlaceHolder(dest, params);
                File destFile = new File(dest);
                String ext = PathUtils.getExt(file);
                if(!tpl.getExts().contains(ext)){
                    //直接拷贝文件
                    FileUtils.copyFile(file, destFile);
                    System.out.println("move file : "+ dest);
                    continue;
                }
                
                String text = FileUtils.readFileToString(file, "utf8");
                // replace placeholder
                text = replacePlaceHolder(text, params);
                System.out.println("generate file : "+ dest);
                FileUtils.writeStringToFile(destFile, text, "utf8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateDirs(Project project, String tplPath, String codePath, JSONObject params , Long entityId) {
        DBEntity entityPo = baseService.get(DBEntity.class, entityId);
        params.put("$[entityName]", toEntityName(entityPo.getName()));
        Collection<File> dirs = FileUtils.listFilesAndDirs(new File(tplPath), FalseFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File dir : dirs) {
            if (dir.getAbsolutePath().equals(tplPath)) {
                continue;
            }
            String dest = codePath + SessionHelper.getTplFileRelativePath(dir.getAbsolutePath(), project.getTplId());
            dest = replacePlaceHolder(dest, params);
            new File(dest).mkdirs();
            System.out.println(dest);
        }
    }

    private String generateEntityClass(DBEntity entityPo , String entityPath){
        String code = codeGenerateService.generate(entityPo);
        return code;
    }
    
    private String replacePlaceHolder(String text, JSONObject params) {
        for (String key : params.keySet()) {
            text = text.replace(key, params.getString(key));
        }
        return text;
    }
    
    private String toEntityName(String tableName){
        //去掉表前缀
        String[] arr = tableName.split("_");
        StringBuilder result = new StringBuilder();
        // 首字母大写
        for(String str : arr){
            StringBuilder sb = new StringBuilder(str);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            result.append(sb);
        }
        return result.toString();
    }
}
