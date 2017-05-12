package com.jumore.devmaster.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.util.StringUtils;

import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.devmaster.entity.FrontComponent;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.system.SystemManager;

public class PathUtils {

    public static final String Template_Dir_Name = "tpls";
    
    public static final String Components_Dir = "comps";
    
    public static final String Code_Generate_Dir = "codeGenerate";
    
    public static String trimPathEnd(String path){
        if(StringUtils.isEmpty(path)){
            return path;
        }
        return new File(path).getAbsolutePath();
    }
    
    public static String getGeneratedCodeAbsolutePath(String projectName , String realtivePath) {
        return getCodeGenerateDir(projectName) + realtivePath;
    }
    
    
    public static String getAbsolutePath(){
        return getAbsolutePath("");
    }
    
    public static String getAbsolutePath(String path){
        String rootPath = SessionHelper.getUserWorkDir() + Template_Dir_Name + File.separator;
        String absolutePath = rootPath;
        
        if(!StringUtils.isEmpty(path)){
            absolutePath += path;
        }
        
        return absolutePath.replace('\\', '/');
    }
    
    public static String getCodeGenerateDir(String projectName){
        return SessionHelper.getUserWorkDir()+Code_Generate_Dir + File.separator + projectName + File.separator;
    }
    
    public static String getComponentsDir(){
        return SessionHelper.getUserWorkDir()+Components_Dir + File.separator;
    }
    
    public static String getTplDir(){
        return SessionHelper.getUserWorkDir()+Template_Dir_Name + File.separator;
    }
    
    public static String getTplDir(Long tplId){
//        ProjectTemplate tpl = SystemManager.getBaseService().get(ProjectTemplate.class, tplId);
        return SessionHelper.getUserWorkDir()+"tpls" + File.separator + tplId + File.separator;
    }
    
    public static String getTplDir(Long tplId , DevMasterUser user){
      return SessionHelper.getUserWorkDir(user)+"tpls" + File.separator + tplId + File.separator;
  }
    
    public static String getTplFileRelativePath(String tplPath , Long tplId){
        return tplPath.replace(getTplDir(tplId), "");
    }
    
    public static String getExt(File file){
        String[] arr = file.getName().split("\\.");
        return arr[arr.length-1];
    }
    
    public static void compressZipfile(String sourceDir, OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        compressDirectoryToZipfile(sourceDir, sourceDir, zos);
        IOUtils.closeQuietly(zos);
    }
    
    private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException, FileNotFoundException {
    	File[] fileList = new File(sourceDir).listFiles();
    	if (fileList.length == 0) { // 空文件夹
    		ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + "/");
            out.putNextEntry(entry);
            out.closeEntry();
    	} else {
	        for (File file : fileList) {
	            if (file.isDirectory()) {
	                compressDirectoryToZipfile(rootDir, sourceDir + File.separator + file.getName(), out);
	            } else {
	                ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + File.separator + file.getName());
	                out.putNextEntry(entry);
	
	                FileInputStream in = new FileInputStream(sourceDir + File.separator + file.getName());
	                IOUtils.copy(in, out);
	                IOUtils.closeQuietly(in);
	            }
	        }
    	}
    }
    
    public static String getRootName(FrontComponent comp) {
        return comp.getGroupId() + "." + comp.getName() + "." + comp.getVersion();
    }
}
