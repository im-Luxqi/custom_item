package com.duomai.new_custom_base.api.product.core.apiReturnValue;


/**
 * Api响应
 * @Author 徐云
 */

public class ApiReturnValue {

    /**
     *  JsonRpc的版本号
     */
    private String jsonrpc="2.0";

    /**
     * 响应出错，返回错误对象
     */
    private Error error;

    /**
     * 响应成功，返回值
     */
    private Object result;

    /**
     * 调用标识符
     */
    private String id;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public Error getError() {
        return error;
    }

    public Object getResult() {
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*
     * @description
     * @create by 王星齐
     * @time 2019-09-16 18:45:37
     * @param error
     **/
    public ApiReturnValue setError(Error error) {
        this.error = error;
        return this;
    }

    /*
     * @description
     * @create by 王星齐
     * @time 2019-10-18 15:04:05
     * @param object
     **/
    public ApiReturnValue setResult(Object object) {
        this.result = object;
        return this;
    }
}
