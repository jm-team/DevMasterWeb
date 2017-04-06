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
    TEMPLATE_NOT_EXIST_OR_DELETED("30001","模板不存在或已经删除"),
    TEMPLATE_EXIST_WITH_SAME_TITLE("30002","存在相同名称从模板"),

    /**
     * 组件提醒
     */
    COMP_VERSION_NOT_BE_NULL("40001","组件版本不能为空"),
    COMP_GROUPID_NOT_BE_NULL("40002","组件groupId不能为空"),
    COMP_EXIST_WITH_SAME_NAME("40003","存在相同groupId+名称+版本号的组件")
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
