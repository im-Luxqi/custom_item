package com.duomai.common.enums;

/**
 * @description
 * @create by 王星齐
 * @date 2020-08-21 10:44
 */
public enum SysErrorEnum {
    VALID_APPKEY("-32002", "appkey无效"), VALID_SIGN("-32003", "sign校验未通过"),
    VALID_EXECUTE("-32004", "请求的API不存在或无权限使用"),
    SERVE_INNER("-32006", "请求服务内部异常"), SERVE_CACHE("-32007", "缓存服务异常"),
    SERVE_REMOTE("-32008", "远程服务器异常"),

    ;
    private final String code;
    private final String value;


    SysErrorEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
