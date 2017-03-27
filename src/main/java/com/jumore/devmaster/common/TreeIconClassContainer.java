package com.jumore.devmaster.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jumore.dove.common.BusinessException;

public abstract class TreeIconClassContainer {
    private static Map<String, String> extensionClassMapper;
    
    static{
        extensionClassMapper = new HashMap<String, String>();
        
        extensionClassMapper.put("folder", "my-tree-folder-icon");
        extensionClassMapper.put("html", "my-tree-html-icon");
        extensionClassMapper.put("js", "my-tree-js-icon");
        extensionClassMapper.put("css", "my-tree-css-icon");
        extensionClassMapper.put("xml", "my-tree-xml-icon");
        extensionClassMapper.put("sql", "my-tree-sql-icon");
        extensionClassMapper.put("default", "my-tree-default-icon");
        
        extensionClassMapper.put("jpg", "my-tree-picture-icon");
        extensionClassMapper.put("png", "my-tree-picture-icon");
    }
    
    private static String getIconClassSafety(String  extension){
        if(!containsKeyIgnoreCase(extension)){
            return extensionClassMapper.get("default");
        }
        
        return extensionClassMapper.get(extension.toLowerCase());
    }
    
    public static String getIconClass(String  extension){
        return getIconClassSafety(extension);
    } 
    
    public static String getIconClass(File file){
        if(file == null){
            throw new BusinessException("file connot be null");
        }
        
        if(file.isDirectory()){
            return extensionClassMapper.get("folder");
        }
        
        String extension = StringUtils.EMPTY;
        int index = file.getName().lastIndexOf('.');
        
        if(index > 0){
            extension = file.getName().substring(index + 1);
        }
        
        return getIconClass(extension);
    } 
    
    private static boolean containsKeyIgnoreCase(String key){
        if(StringUtils.isBlank(key)){
            return false;
        }
        
        key = key.toLowerCase();
        
        return extensionClassMapper.keySet().contains(key);
    }
}
