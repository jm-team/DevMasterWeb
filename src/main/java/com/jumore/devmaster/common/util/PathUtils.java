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

public class PathUtils {

    public static String trimPathEnd(String path){
        if(StringUtils.isEmpty(path)){
            return path;
        }
        return new File(path).getAbsolutePath();
    }
    
    public static String getGeneratedCodeAbsolutePath(String projectName , String realtivePath) {
        return SessionHelper.getCodeGenerateDir(projectName) + realtivePath;
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
}
