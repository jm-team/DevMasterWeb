package com.jumore.devmaster.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by jiangjianwei
 * DATE: 2016/7/29.
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 表单提交方式HTTP连接
     * @param url
     * @param formParams
     * @return
     */
    public static HttpResponse executeByEncodedForm(String url, List<NameValuePair> formParams) {
        HttpResponse response = null;
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
            post.setEntity(entity);
            response = httpClient.execute(post);
        } catch (IOException e) {
            LOGGER.info("提交请求出错：{}", e.getStackTrace());
        }

        return response;
    }

}
