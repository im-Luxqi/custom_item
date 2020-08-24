package com.duomai.common.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 公共参数
 **/
@Data
public class CommomParameter {
    /**
     * 分配给应用的AppKey
     */
    @NotBlank(message = "AppKey不能为空")
    private String appkey;

    /**
     * 时间戳
     */
    @NotBlank(message = "时间戳不能为空")
    private String timestamp;

    /**
     * API请求签名
     */
    @NotBlank(message = "签名不能为空")
    private String sign;

    /**
     * API请求的ip
     */
    private String ip;

}
