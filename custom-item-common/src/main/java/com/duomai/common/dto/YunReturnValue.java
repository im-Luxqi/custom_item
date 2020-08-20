package com.duomai.common.dto;


import lombok.Data;

/**
 * 云应用固定返回格式，系统级返回
 *
 * @Author hjw
 */
@Data
public class YunReturnValue {

    /**
     * 布尔值，表示本次调用是否成功
     */
    private Boolean success = false;
    /**
     * 类型不限，调用成功（success为true）时，服务端返回的数据
     */
    private Object data;
    /**
     * 服务端返回的编码，200为业务成功，500为业务失败，其他错误为openapi返回
     */
    private String errorCode;
    /**
     * 字符串，调用失败（success为false）时，服务端返回的错误信息
     */
    private String errorMessage;


    public YunReturnValue() {
    }

    public YunReturnValue(Boolean success, String errorCode, Object data, String errorMessage) {
        this.success = success;
        this.errorCode = errorCode;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public YunReturnValue(Boolean success, String errorCode, Object data) {
        this.success = success;
        this.errorCode = errorCode;
        this.data = data;
    }

    public YunReturnValue(Boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public static YunReturnValue ok(Object data) {
        return new YunReturnValue(true, "200", new ReturnBaseData(200, data));
    }

    public static YunReturnValue fail(Object data, String code, String msg) {
        return new YunReturnValue(true, "500", new ReturnBaseData(Integer.valueOf(code), data, msg), msg);
    }
}
