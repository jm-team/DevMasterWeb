package com.jumore.devmaster.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Administrator on 2017/3/21.
 */
public class ConnectionUtil {
    public static Connection initConnection(String driverClass, String dbUrl, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driverClass);
        Properties props =new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty("remarks", "true"); //设置可以获取remarks信息
        props.setProperty("useInformationSchema", "true");//设置可以获取tables remarks信息
        return DriverManager.getConnection(dbUrl, props);
    }
}
