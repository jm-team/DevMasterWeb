package com.jumore.devmaster.common.model;

import org.springframework.beans.factory.annotation.Value;

public class Configuration {

    @Value(value="${sys.dataPath}")
    private String dataPath;
    
    @Value(value="${ftp.server.address}")
    private String ftpAddr;
    
    @Value(value="${ftp.server.port}")
    private Integer ftpPort;
    
    @Value(value="${ftp.server.username}")
    private String ftpUser;
    
    @Value(value="${ftp.server.password}")
    private String ftpPwd;
    
    public String getDataPath() {
        return dataPath;
    }
    
    public String getFtpAddr() {
        return ftpAddr;
    }
    
    public Integer getFtpPort() {
        return ftpPort;
    }
    
    public String getFtpUser() {
        return ftpUser;
    }
    
    public String getFtpPwd() {
        return ftpPwd;
    }
}
