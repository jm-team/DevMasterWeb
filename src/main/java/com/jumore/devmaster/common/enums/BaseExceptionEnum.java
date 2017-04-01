package com.jumore.devmaster.common.enums;

/**
 * 异常报错提示枚举
 * Created by yangjianbin on 2017/4/1.
 */
public enum BaseExceptionEnum {
    /**
     * 通用提醒
     */
    SYS_ERROR("10000","系统出错，请稍后再试"),
    STATUS_NO_EFFECT("10001","无效的状态设置"),
    PARAM_REQUIRED("10002","参数不能为空"),
    LOGIN_FAIL("10004","登录失败"),
    PASSWD_FAIL("10005","用户密码错误"),



    /**
     * 字段提醒
     */
    TITLE_NOT_BE_NULL("20000","标题不能为空"),



    /**
     * 模块提醒
     */
    TEMPLATE_EXT_NOT_BE_NULL("30000","模板文件的扩展名不能为空"),
    TEMPLATE_NOT_EXIST_OR_DELETED("30001","模板不存在或已经删除")

    ;


    BaseExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
