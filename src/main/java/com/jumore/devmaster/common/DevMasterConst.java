package com.jumore.devmaster.common;

public interface DevMasterConst {

    interface Token {
        int Token_Timeout_Seconds = 3600;
        int Access_Token_Timeout_Seconds = 60;
    }
    
    interface Session {
        final String Session_User_Key = "session_user";
    }
    
    interface UserType {
        final Integer Admin = 0;
        final Integer Developer = 1;
        final Integer ServiceProvider = 2;
    }
    
    interface Flag {
        final Integer Delete = 1;
        final Integer NotDelete = 0;
    }

    /**
     * 缓存key
     */
    interface CacheKey{
        /**
         * 系统key
         */
        final String SYS_KEY_MAIN = "dev-master";
        /**
         * 用户注册
         */
        final String USER_REGISTER = "user_register";

        /**
         * 重置密码
         */
        final String RESET_PWD = "reset_pwd";

        /**
         * 手机验证码失效时间
         */
        final int MOBILE_CHK_CODE_EXPIRE_TIME = 300;
    }
}
