package com.duomai.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * API请求
 * 接收API的参数，包含系统级参数和业务级参数，业务级参数中的值为一个json格式的字符串
 */
@Data
public class ApiSysParameter {

    /**
     * JsonRpc的版本号
     */
    @NotBlank(message = "JsonRpc的版本号不能为空")
    private String jsonrpc;

    /**
     * 调用方法名
     */
    @NotBlank(message = "调用方法名不能为空")
    private String method;

    //参数，包括公共参数和业务级参数
    /**
     *
     */
    @Valid
    private ApiParameter params;

    /**
     * 调用标识符
     */
    private String id;

    /**
     * 调用标识符
     */
    private Date requestStartTime;


    @JSONField(name = "params")
    public ApiParameter getApiParameter() {
        return params;
    }

    @JSONField(name = "params")
    public void setApiParameter(ApiParameter apiParameter) {
        this.params = apiParameter;
    }
}
