package com.jumore.devmaster.service.nashorn;

import java.io.File;
import java.io.IOException;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jumore.devmaster.common.util.PathUtils;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.dove.common.BusinessException;

public class ParserEngine {

    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    ScriptEngine        nashorn;

    public ParserEngine() {
        nashorn = scriptEngineManager.getEngineByName("nashorn");
        if (nashorn == null) {
            nashorn = scriptEngineManager.getEngineByName("rhino");
        }
    }

    public JSONObject initVelicityContext(ProjectTemplate template, String tableName , JSONObject params) throws ScriptException{
        params.put("tableName", tableName);
        if(StringUtils.isEmpty(template.getEntityNameFunction())){
            return params;
        }
        Bindings bindings = nashorn.createBindings();
        bindings.put("tableName", tableName);
        bindings.put("ctx", params);
        
        String parseFile = PathUtils.getTplDir(template.getId()) + template.getEntityNameFunction();
        File parse = new File(parseFile);
        if (parse.exists()) {
            String parserText;
            try {
                parserText = FileUtils.readFileToString(parse, "utf8");
                if (!StringUtils.isEmpty(parserText)) {
                    nashorn.eval(parserText , bindings);
                }
            } catch (IOException e) {
                throw new BusinessException("读取失败context初始化脚本失败" , e);
            }
            
        }
        return params;
    }
    
    public static void main(String[] args) throws ScriptException{
        ParserEngine engine = new ParserEngine();
        engine.initVelicityContext(null,"xxx" , new JSONObject());
    }
    
//    public String toEntityName(ProjectTemplate template, String tableName) {
//        String parseFile = PathUtils.getTplDir(template.getId()) + template.getEntityNameFunction();
//        File parse = new File(parseFile);
//        if (parse.exists()) {
//            try {
//                String parserText = FileUtils.readFileToString(parse, "utf8");
//                if (!StringUtils.isEmpty(parserText)) {
//
//                    Bindings bindings = nashorn.createBindings();
//                    bindings.put("tableName", tableName);
//                    String result = (String) nashorn.eval(parserText , bindings);
//                    System.out.println(result);
//                    return result;
//                } else {
//                    return toDefaultEntityName(tableName);
//                }
//            } catch (IOException ex) {
//                return toDefaultEntityName(tableName);
//            } catch (ScriptException ex) {
//                throw new BusinessException("解析器执行错误", ex);
//            }
//        } else {
//            return toDefaultEntityName(tableName);
//        }
//    }
//
//    private String toDefaultEntityName(String tableName) {
//        // 去掉表前缀
//        String[] arr = tableName.split("_");
//        StringBuilder result = new StringBuilder();
//        // 首字母大写
//        for (String str : arr) {
//            StringBuilder sb = new StringBuilder(str);
//            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
//            result.append(sb);
//        }
//        return result.toString();
//    }
}
