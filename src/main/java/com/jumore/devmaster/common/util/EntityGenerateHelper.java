package com.jumore.devmaster.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jumore.dove.common.BusinessException;

public class EntityGenerateHelper {

    private static String              DEFAULT_DB_TYPE  = "default";
    private static Map<String, String> JavaDbTypeMapper = new HashMap<String, String>();

    static {
        JavaDbTypeMapper.put(DEFAULT_DB_TYPE, "String");
        JavaDbTypeMapper.put("smallint", "Integer");
        JavaDbTypeMapper.put("int", "Integer");
        JavaDbTypeMapper.put("bigint", "Long");
        JavaDbTypeMapper.put("char", "String");
        JavaDbTypeMapper.put("varchar", "String");
        JavaDbTypeMapper.put("date", "Date");
        JavaDbTypeMapper.put("timestamp", "Date");
        JavaDbTypeMapper.put("datetime", "Date");
        JavaDbTypeMapper.put("text", "String");
    }

    public static String convertToJavaTypeSafety(String dbType) {
        if (StringUtils.isBlank(dbType)) {
            dbType = DEFAULT_DB_TYPE;
        }

        String javaType = JavaDbTypeMapper.get(dbType.toLowerCase());

        if (StringUtils.isBlank(javaType)) {
            javaType = JavaDbTypeMapper.get(DEFAULT_DB_TYPE);
        }

        return javaType;
    }

    public static String firstCharToUpperCase(String str) {
        if (StringUtils.isBlank(str)) {
            throw new BusinessException("str cannot be null");
        }

        if (str.length() == 1) {
            return str.toUpperCase();
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
