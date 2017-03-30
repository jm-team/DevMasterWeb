package com.jumore.devmaster.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 消息模板
 * @author: liuming
 * @since: 2016年10月27日
 * @history:
 */
public enum MessageTemplateEnum {
    SMS_REGISTER("新用户注册", "JMT-000001", "JMT-000001");

    String operation; //操作
    String code;     // 编号
    String jltSmsTemplate;//聚灵通消息模版编号


    /**
     * 通过枚举<code>code</code>获得枚举。
     *
     * @param code
     * @return MessageTemplateEnum 权限值枚举
     */
    public static MessageTemplateEnum getEnumByCode(String code) {
        for (MessageTemplateEnum mtEnum : MessageTemplateEnum.values()) {
            if (StringUtils.equals(code,mtEnum.getCode())) {
                return mtEnum;
            }
        }
        return null;
    }

    MessageTemplateEnum(String operation, String code, String jltSmsTemplate) {
        this.operation = operation;
        this.code = code;
        this.jltSmsTemplate = jltSmsTemplate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getJltSmsTemplate() {
        return jltSmsTemplate;
    }

    public void setJltSmsTemplate(String jltSmsTemplate) {
        this.jltSmsTemplate = jltSmsTemplate;
    }
}
