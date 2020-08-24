package com.duomai.common.dto;


import com.duomai.common.enums.SysErrorEnum;
import lombok.Data;

/**
 * 云应用固定返回格式，系统级返回
 */
@Data
public class YunReturnValue {

    /**
     * 布尔值，表示本次调用是否成功
     */
    private Boolean success;
    /**
     * 类型不限，调用成功（success为true）时，服务端返回的数据
     */
    private ReturnBaseData data;
    /**
     * 服务端返回的错误编码
     */
    private String errorCode;
    /**
     * 字符串，调用失败（success为false）时，服务端返回的错误信息
     */
    private String errorMessage;

    public YunReturnValue(Boolean success, String errorCode, ReturnBaseData data, String errorMessage) {
        this.success = success;
        this.errorCode = errorCode;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public YunReturnValue(Boolean success, ReturnBaseData data) {
        this.success = success;
        this.data = data;
    }

    public static YunReturnValue ok(Object data, String msg) {
        return new YunReturnValue(true, new ReturnBaseData(ReturnBaseData.success, data, msg));
    }

    public static YunReturnValue ok(String msg) {
        return ok(null, msg);
    }

    public static YunReturnValue fail(Object data, SysErrorEnum error, String msg) {
        return new YunReturnValue(true, error.getCode(), new ReturnBaseData(ReturnBaseData.error, data, msg), error.getValue());
    }

    public static YunReturnValue fail(SysErrorEnum error, String msg) {
        return fail(null, error, msg);
    }

    public static YunReturnValue fail(SysErrorEnum error) {
        return fail(null, error, error.getValue());
    }

    public static YunReturnValue fail(String msg) {
        return fail(null, SysErrorEnum.SERVE_INNER, msg);
    }
}
