package com.jumore.devmaster.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.jumore.devmaster.entity.DBEntity;
import com.jumore.devmaster.entity.EntityField;
import com.jumore.devmaster.service.CodeGenerateService;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.service.BaseServiceImpl;
import com.jumore.dove.util.ParamMap;

@Component
public class EntityCodeGenerateImpl extends BaseServiceImpl implements CodeGenerateService {

    @Override
    public String generate(DBEntity table) {
        ParamMap paramMap = new ParamMap();
        paramMap.put("entityId", table.getId());
        List<EntityField> fields = super.listByParams(EntityField.class, "Field.listField", paramMap);

        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }

        EntityContentResolver resolver = new EntityContentResolver(table, fields);
        return resolver.resolve();
    }

    private class EntityContentResolver {
        final String              NEW_LINE          = System.lineSeparator();
        final String              TWO_NEW_LINE      = NEW_LINE + NEW_LINE;
        final String              TAB               = "    ";
        final String              TWO_TAB           = TAB + TAB;
        final Integer             PRIMARY_KEY_VALUE = Integer.valueOf(1);

        private DBEntity          table;
        private List<EntityField> fields;
        private StringBuilder     contentBuilder;

        public EntityContentResolver() {

        }

        public EntityContentResolver(DBEntity table, List<EntityField> fields) {
            this();
            this.table = table;
            this.fields = fields;
        }

        private EntityContentResolver resolvePackage() {
            contentBuilder.append(TWO_NEW_LINE);
            return this;
        }

        private EntityContentResolver resolveImport() {
            contentBuilder.append("import com.jumore.dove.aop.Table").append(NEW_LINE)
                .append("import com.jumore.dove.aop.Entity").append(NEW_LINE)
                .append("import com.jumore.dove.aop.Sequence").append(NEW_LINE)
                .append("import com.jumore.dove.aop.Id").append(NEW_LINE)
                .append("import com.jumore.dove.aop.Column").append(NEW_LINE)
                .append("import com.jumore.dove.aop.AutoIncrease").append(TWO_NEW_LINE);
            
            return this;
        }

        private EntityContentResolver resolveClassHeader() {
            String className = NameConverter.convertToClassName(table.getName());
            
            contentBuilder.append("@Entity").append(NEW_LINE)
                .append("@Table(name=\"").append(table.getName()).append("\")").append(NEW_LINE)
                .append("public class ").append(className).append(" {").append(NEW_LINE);
            
            return this;
        }

        private EntityContentResolver resolveFields() {
            if(CollectionUtils.isEmpty(fields)){
                return this;
            }
            
            for (EntityField field : fields) {
                String javaType = TypeConverter.convertToJavaType(field.getType());
                String fieldName = NameConverter.convertToFieldName(field.getName());
                
                contentBuilder.append(TAB).append("/** ").append(field.getDocs()).append(" */").append(NEW_LINE);
                
                if(PRIMARY_KEY_VALUE.equals(field.getPrimaryKey())){
                    contentBuilder.append(TAB).append("@Id").append(NEW_LINE)
                    .append(TAB).append("@AutoIncrease").append(NEW_LINE);
                }
                
                contentBuilder.append(TAB).append("@Column(name=\"").append(field.getName()).append("\")").append(NEW_LINE)
                    .append(TAB).append("private ").append(javaType).append(" ").append(fieldName).append(";").append(TWO_NEW_LINE);
            }
            
            return this;
        }

        private EntityContentResolver resolveGeterSetter() {
            if(CollectionUtils.isEmpty(fields)){
                return this;
            }
            
            for (EntityField field : fields) {
                String javaType = TypeConverter.convertToJavaType(field.getType());
                String fieldName = NameConverter.convertToFieldName(field.getName());
                String getterName = NameConverter.convertToGetterName(fieldName);
                String setterName = NameConverter.convertToSetterName(fieldName);
                
                contentBuilder.append(TAB).append("public ").append(javaType).append(" ").append(getterName).append("() {").append(NEW_LINE)
                    .append(TWO_TAB).append("return ").append(fieldName).append(";").append(NEW_LINE)
                    .append(TAB).append("}").append(TWO_NEW_LINE);
                
                contentBuilder.append(TAB).append("public void ").append(setterName).append("(").append(javaType).append(" ").append(fieldName).append(") {").append(NEW_LINE)
                    .append(TWO_TAB).append("this.").append(fieldName).append(" = ").append(fieldName).append(";").append(NEW_LINE)
                    .append(TAB).append("}").append(TWO_NEW_LINE);
            }
            
            return this;
        }

        private EntityContentResolver resolveClassFooter() {
            contentBuilder.append("}");
            return this;
        }

        public String resolve() {
            contentBuilder = new StringBuilder();
            resolvePackage().resolveImport().resolveClassHeader().resolveFields().resolveGeterSetter().resolveClassFooter();
            return contentBuilder.toString();
        }
    }

    private static class NameConverter {
        private static String FIELD_SEPERATOR = "_";
        private static String GETTER_PREFIX   = "get";
        private static String SETTER_PREFIX   = "set";

        public static String convertToFieldName(String dbFieldName) {
            if (StringUtils.isBlank(dbFieldName)) {
                throw new BusinessException("dbFieldName cannot be null");
            }

            String[] fragments = dbFieldName.split(FIELD_SEPERATOR);

            if (fragments.length == 1) {
                return dbFieldName;
            }

            String fieldName = fragments[0];

            for (int i = 1; i < fragments.length; i++) {
                fieldName += firstCharToUpperCase(fragments[i]);
            }

            return fieldName;
        }

        public static String convertToGetterName(String fieldName) {
            if (StringUtils.isBlank(fieldName)) {
                throw new BusinessException("fieldName cannot be null");
            }

            return GETTER_PREFIX + firstCharToUpperCase(fieldName);
        }

        public static String convertToSetterName(String fieldName) {
            if (StringUtils.isBlank(fieldName)) {
                throw new BusinessException("fieldName cannot be null");
            }

            return SETTER_PREFIX + firstCharToUpperCase(fieldName);
        }
        
        private static String convertToClassName(String tableName){
            return firstCharToUpperCase(convertToFieldName(tableName));
        }

        private static String firstCharToUpperCase(String str) {
            if (StringUtils.isBlank(str)) {
                throw new BusinessException("str cannot be null");
            }

            if (str.length() == 1) {
                return str.toUpperCase();
            }

            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }

    private static class TypeConverter {
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

        private static String convertToJavaTypeSafety(String dbType) {
            if (StringUtils.isBlank(dbType)) {
                dbType = DEFAULT_DB_TYPE;
            }

            String javaType = JavaDbTypeMapper.get(dbType.toLowerCase());

            if (StringUtils.isBlank(javaType)) {
                javaType = JavaDbTypeMapper.get(DEFAULT_DB_TYPE);
            }

            return javaType;
        }

        public static String convertToJavaType(String dbType) {
            return convertToJavaTypeSafety(dbType);
        }
    }
}
