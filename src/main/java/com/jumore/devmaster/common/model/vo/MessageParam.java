package com.jumore.devmaster.common.model.vo;

import com.jumore.devmaster.enums.MessageTemplateEnum;

/**
 * 消息发送参数
 * Created by Long on 2017/3/28.
 */
public class MessageParam {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 短信模板
     */
    private MessageTemplateEnum templateEnum;
    /**
     * 参数
     */
    private String[] param;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public MessageTemplateEnum getTemplateEnum() {
        return templateEnum;
    }

    public void setTemplateEnum(MessageTemplateEnum templateEnum) {
        this.templateEnum = templateEnum;
    }

    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }

    public MessageParam(String mobile, MessageTemplateEnum templateEnum, String... param) {
        this.mobile = mobile;
        this.templateEnum = templateEnum;
        this.param = param;
    }
}
