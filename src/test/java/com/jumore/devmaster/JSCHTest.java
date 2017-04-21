package com.jumore.devmaster;

import com.jcraft.jsch.Session;
import com.jumore.devmaster.common.jsch.JSchCommandTool;
import com.jumore.devmaster.common.jsch.JSchConfig;
import com.jumore.devmaster.common.jsch.JSchConnectionPool;
import org.junit.Test;

import java.util.Map;

/**
 * Created by yangjianbin on 2017/4/21.
 */
public class JSCHTest {

    @Test
    public void test1() throws Exception{
        JSchConfig jSchConfig = new JSchConfig("",22,"","");
        Session session = JSchConnectionPool.getConnection(jSchConfig);
        String command = "pwd";
        Map<String,Object> mapResult = JSchCommandTool.execCommand(session,command);
        System.out.println(mapResult.get("result"));
    }


}
