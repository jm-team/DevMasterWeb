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
}
