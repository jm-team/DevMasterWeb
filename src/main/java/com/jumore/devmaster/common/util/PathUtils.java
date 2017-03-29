package com.jumore.devmaster.common.util;

import java.io.File;

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
}
