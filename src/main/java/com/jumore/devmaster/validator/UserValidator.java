package com.jumore.devmaster.validator;

import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.dove.system.SystemManager;

public class UserValidator {

    public static boolean isUserExsit(String account){
        DevMasterUser vo = new DevMasterUser();
        vo.setAccount(account);
        vo.setDeleteFlag(DevMasterConst.Flag.NotDelete);
        Object po = SystemManager.getBaseService().getByExample(vo);
        if(po==null){
            return false;
        }else{
            return true;
        }
    }
}
