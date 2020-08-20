package com.duomai.common.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 公共参数
 **/
@Data
public class CommomParameter {

    /**
     * 随机码
     */
    private String nonceStr;

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
     * 用户账号信息id
     */
    private String siid;


    /**
     * 令牌
     */
    private String sessionKey;
    /**
     * 返回数据类型R2(混淆数据)和R1(明文数据)
     */
    private String returnDataType;

    private String admjsonStr;

    private String ip;

}
