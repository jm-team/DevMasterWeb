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
}
