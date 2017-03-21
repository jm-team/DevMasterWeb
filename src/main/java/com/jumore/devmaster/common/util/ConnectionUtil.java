package com.jumore.devmaster.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/3/21.
 */
public class ConnectionUtil {
    public static Connection initConnection(String driverClass, String dbUrl, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driverClass);
        return DriverManager.getConnection(dbUrl, username, password);
    }
}
