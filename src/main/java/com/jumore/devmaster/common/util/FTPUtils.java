package com.jumore.devmaster.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.jumore.devmaster.common.model.Configuration;
import com.jumore.dove.util.SpringContextHelper;

public abstract class FTPUtils {
    private static Configuration cfg;

    static {
        cfg = SpringContextHelper.getBean(Configuration.class);
    }

    /**
     * connect:链接到FTP
     * 
     * @author 乔广
     * @date 2017年3月27日 下午2:53:23
     * @param path 上传到ftp服务器哪个路径下
     * @param addr 地址
     * @param port 端口号
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws IOException
     * @throws SocketException
     */
    private static FTPClient connect(String path, String addr, int port, String username, String password) throws Exception {
        FTPClient client = new FTPClient();

        client.connect(addr, port);
        client.login(username, password);
        client.setFileType(FTPClient.BINARY_FILE_TYPE);
        int reply = client.getReplyCode();

        if (FTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            return null;
        }

        client.changeWorkingDirectory(path);

        return client;
    }

    public static void upload(File file) throws Exception {
        if (file == null) {
            return;
        }

        FTPClient client = connect(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)),
                cfg.getFtpAddr(), cfg.getFtpPort(), cfg.getFtpUser(), cfg.getFtpPwd());

        if (client == null) {
            throw new RuntimeException("ftp connection cannot be null");
        }

        if (file.isFile()) {
            uploadFile(client, file);
        } else {
            uploadDirecry(client, file);
        }
    }

    private static void uploadDirecry(FTPClient client, File file) throws Exception {
        client.makeDirectory(file.getName());
        client.changeWorkingDirectory(file.getName());
        String[] files = file.list();

        for (int i = 0; i < files.length; i++) {
            File f = new File(file.getPath() + File.separator + files[i]);

            if (f.isDirectory()) {
                upload(f);
                client.changeToParentDirectory();
            } else {
                uploadFile(client, f);
            }
        }
    }

    private static void uploadFile(FTPClient client, File file) throws Exception {
        FileInputStream input = new FileInputStream(file);
        client.storeFile(file.getName(), input);
        input.close();
    }

    public static void download() {

    }
}
