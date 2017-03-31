package com.jumore.devmaster.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.common.util.PathUtils;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.DBEntity;
import com.jumore.devmaster.entity.EntityField;
import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.devmaster.service.CodeGenerateService;
import com.jumore.devmaster.service.ProjectGenerateService;
import com.jumore.devmaster.service.nashorn.ParserEngine;
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

        ParserEngine parseEngine = new ParserEngine();
        ProjectTemplate tempalte = baseService.get(ProjectTemplate.class, project.getTplId());
        
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
            DBEntity entityPo = baseService.get(DBEntity.class, Long.valueOf(entityId));
            params.put("entityName", parseEngine.toEntityName(tempalte,entityPo.getName()));
            // 先生成目录
            generateDirs(parseEngine,project, tempalte , tplPath, codePath, params , entityPo);

            // 生成文件
            generateFiles(parseEngine,project ,tempalte, tplPath , codePath , params , entityPo);
        }
        
    }

    private void generateFiles(ParserEngine parseEngine,Project project, ProjectTemplate tempalte, String tplPath, String codePath, JSONObject params , DBEntity entityPo) {
        
        //生成实体类
        Object code = generateEntityClass(entityPo , null);
        params.put("entityCode", code);
        
        // get all tpl files
        Collection<File> files = FileUtils.listFiles(new File(tplPath), null, true);

        ProjectTemplate tpl = baseService.get(ProjectTemplate.class, project.getTplId());
        if(StringUtils.isEmpty(tpl.getExts())){
            throw new BusinessException("没有设置模板支持的扩展名");
        }
        for (File tplFile : files) {
            try {
                String dest = codePath + PathUtils.getTplFileRelativePath(tplFile.getAbsolutePath(), project.getTplId());
                dest = replacePlaceHolder(dest, params);
                File destFile = new File(dest);
                String realDest = destFile.getParentFile().getAbsolutePath().replace(".", File.separator)+File.separator+destFile.getName();
                File realDestFile = new File(realDest);
                String ext = PathUtils.getExt(tplFile);
                if(!tpl.getExts().contains(ext)){
                    //直接拷贝文件
                    FileUtils.copyFile(tplFile, realDestFile);
                    System.out.println("move file : "+ realDestFile.getAbsolutePath());
                    continue;
                }
                
                // 使用模板解析器解析模板文件
                String text = resolveTemplate(tplFile , entityPo.getId() , params);
                
                System.out.println("generate file : "+ realDestFile.getAbsolutePath());
                FileUtils.writeStringToFile(realDestFile, text, "utf8");
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * 根据扩展名对应的模板解析器解析模板文件
     * @author yexinzhou
     * @date 2017年3月29日 下午1:46:23
     * @param text 模板内容
     * @param ext  模板扩展名
     * @return
     * @throws IOException 
     */
    private String resolveTemplate(File tplFile , Long dbEntityId , JSONObject params) throws IOException {
        try{
            Properties p = new Properties();  
            VelocityEngine ve = new VelocityEngine();
            p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, tplFile.getParent());
            p.setProperty(Velocity.INPUT_ENCODING, "utf8");  
            p.setProperty(Velocity.OUTPUT_ENCODING, "utf8"); 
            ve.init(p);
            Template tpl =ve.getTemplate(tplFile.getName() , "utf8"); 
            
            VelocityContext ctx = new VelocityContext(params);
            EntityField vo = new EntityField();
            vo.setDbentityId(dbEntityId);
            vo.setShowInput(DevMasterConst.ShowInput.Yes);
            List<EntityField> fieldList = baseService.listByExample(vo);
            ctx.put("fieldList", fieldList);
            StringWriter sw = new StringWriter();
            tpl.merge(ctx, sw);
            return sw.toString();
        }catch(Exception ex){
            throw new BusinessException(" 处理模板文件 "+tplFile.getName()+"发生错误:"+ex.getMessage() , ex);
        }
        
    }

    private void generateDirs(ParserEngine parseEngine , Project project,ProjectTemplate tempalte, String tplPath, String codePath, JSONObject params ,DBEntity entityPo) {
        Collection<File> dirs = FileUtils.listFilesAndDirs(new File(tplPath), FalseFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File dir : dirs) {
            if (dir.getAbsolutePath().equals(tplPath)) {
                continue;
            }
            String dest = codePath + PathUtils.getTplFileRelativePath(dir.getAbsolutePath(), project.getTplId());
            
            dest = replacePlaceHolder(dest, params);
            // replace . to \
            dest = dest.replace(".", File.separator);
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
            text = text.replace("${"+key+"}", params.getString(key));
        }
        return text;
    }
    
}
