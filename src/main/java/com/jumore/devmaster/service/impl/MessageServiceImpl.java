package com.jumore.devmaster.service.impl;

import com.jumore.devmaster.common.model.vo.MessageParam;
import com.jumore.devmaster.common.util.HttpClientUtil;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.common.util.StringUtil;
import com.jumore.devmaster.service.MessageService;
import com.jumore.dove.service.BaseServiceImpl;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * 消息实现
 * Created by Long on 2017/3/28.
 */
@Component
public class MessageServiceImpl extends BaseServiceImpl implements MessageService {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    /**
     * 短信发送
     * @param messageParam 参数
     * @return
     * @history
     */
    public void sendMessage(MessageParam messageParam) {
        if (StringUtil.isEmpty(messageParam.getMobile())) {
            LOGGER.error("手机号不能为空");
            return;
        }

        // 封装http请求参数(发送短信请求参数)
        List<NameValuePair> formParams = new LinkedList<>();
        formParams.add(new BasicNameValuePair("tplCode", messageParam.getTemplateEnum().getJltSmsTemplate()));
        formParams.add(new BasicNameValuePair("mobile", messageParam.getMobile()));

        String args = "";

        // 非空判断
        if (messageParam.getParam() != null && messageParam.getParam().length != 0) {
            for(String param : messageParam.getParam()){
                args += param + "||";
            }
        }

        if(StringUtil.isNotEmpty(args)){
            args = StringUtil.trimEnd(args, "||");
        }

        formParams.add(new BasicNameValuePair("args", args));

        LOGGER.info("发送短信参数：tplCode=" + messageParam.getTemplateEnum().getJltSmsTemplate() + "&mobile=" + messageParam.getMobile() + "&args=" + args);

        // 短信请求最终格式示例为：http://www.jlt.com/app/smsTemplate/doSendSms?tplCode=JSM40618-0015&mobile=13395594089&args=3652
        HttpClientUtil.executeByEncodedForm(SessionHelper.getJltUrl()+"/app/smsTemplate/doSendSms", formParams);
        LOGGER.info("jltUrl=" + SessionHelper.getJltUrl());
        LOGGER.info("短信发送成功");
    }

    public void batchSendMessage(List<MessageParam> paramList){
        for(MessageParam messageParam : paramList){
            sendMessage(messageParam);
        }
    }
}
