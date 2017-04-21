package com.jumore.devmaster.common.jsch;

/**
 * Created by yangjianbin on 2017/4/21.
 */
public class JSchConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private int timeout = 1800;

    public JSchConfig(String host, int port, String username, String password) {
        this(host,port,username,password,1800);
    }

    public JSchConfig(String host, int port, String username, String password, int timeout) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.timeout = timeout;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
