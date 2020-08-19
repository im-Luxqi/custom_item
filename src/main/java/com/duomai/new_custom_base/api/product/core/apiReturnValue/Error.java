package com.duomai.new_custom_base.api.product.core.apiReturnValue;


/**
 * API错误对象
 * @Author 徐云
 **/
public class Error {

    /**
     * 错误类型
     */
    private String code;

    /**
     * 错误原因
     */
    private String message;

    /**
     * 错误详情（错误的额外信息，访问限制等，可省略）
     */
    private String data;

    public Error() {
    }

    public Error(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Error(String code, String message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
