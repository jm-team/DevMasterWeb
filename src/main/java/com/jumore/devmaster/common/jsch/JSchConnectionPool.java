package com.jumore.devmaster.common.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.collections.CollectionUtils;

import java.util.LinkedList;
import java.util.Properties;

/**
 * Created by yangjianbin on 2017/4/21.
 */
public class JSchConnectionPool {

    private static LinkedList<Session> pool = new LinkedList<>();

    private static JSch jSch = new JSch();


    public static Session getConnection(JSchConfig jSchConfig){
        Session session = null;
        if(CollectionUtils.isNotEmpty(pool)){
            session = pool.getLast();
        }
        if(session == null){
            try {
                session = jSch.getSession(jSchConfig.getUsername(),jSchConfig.getHost(),jSchConfig.getPort());
                session.setPassword(jSchConfig.getPassword());
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.setTimeout(jSchConfig.getTimeout());
                session.connect();
                pool.push(session);
            } catch (Exception e) {
                e.printStackTrace();
                //TODO 抛出异常
            }
        }
        return session;
    }

}
