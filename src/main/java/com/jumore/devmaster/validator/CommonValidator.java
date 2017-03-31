package com.jumore.devmaster.validator;

import com.jumore.dove.common.BusinessException;
import com.jumore.dove.system.SystemManager;
import com.jumore.dove.util.ReflectHelper;

public class CommonValidator {

    public static boolean isEntityExsit(Class<?> clazz , String[] fields , Object[] values){
        Object obj;
        try {
            obj = clazz.newInstance();
            for(int i=0;i<fields.length;i++){
                ReflectHelper.setValueByFieldName(obj, fields[i], values[i]);
            }
            Object po = SystemManager.getBaseService().getByExample(obj);
            if(po==null){
                return false;
            }else{
                return true;
            }
        }catch (Exception e) {
            throw new BusinessException("数据唯一性验证失败",e);
        }
        
    }
}
