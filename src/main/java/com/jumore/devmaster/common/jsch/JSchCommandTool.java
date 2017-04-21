package com.jumore.devmaster.common.jsch;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangjianbin on 2017/4/21.
 */
public class JSchCommandTool {

    /**
     * 删除文件
     *
     * @param deleteFile 要删除的文件
     */
    public void delete(Session session, String deleteFile) {
        Channel channel;
        ChannelSftp chSftp;
        try {
            channel = session.openChannel("sftp"); // 打开SFTP通道
            channel.connect(); // 建立SFTP通道的连接
            chSftp = (ChannelSftp) channel;
            chSftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 执行命令 类型 cd /usr/local/nginx
     *
     * @param session
     * @param command
     * @return
     * @throws Exception
     */
    public static Map<String, Object> execCommand(Session session, String command) throws Exception {
        Channel channel = null;
        Map<String, Object> mapResult = new HashMap();
        StringBuffer result = new StringBuffer();//脚本返回结果
        BufferedReader reader = null;
        int returnCode;//脚本执行退出状态码
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            channel.connect();//执行命令 等待执行结束
            InputStream in = channel.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            String res;
            while ((res = reader.readLine()) != null) {
                result.append(res + "\n");
            }
            returnCode = channel.getExitStatus();
            mapResult.put("returnCode", returnCode);
            mapResult.put("result", result.toString());

        } catch (Exception e) {
            channel.disconnect();
            session.disconnect();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
        return mapResult;
    }

}
