package com.duomai.common.dto;


import lombok.Data;

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
    private String appkey;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * API请求签名
     */
    private String sign;

    /**
     * 用户账号信息id
     */
    private String siid;

    /**
     * 选择的门店id
     */
    private String storeId;

    /**
     * 令牌
     */
    private String sessionKey;
    /**
     * 返回数据类型R2(混淆数据)和R1(明文数据)
     */
    private String returnDataType;

    private Long userId;

//    private TblUser user;

    private String admjsonStr;

    private String ip;

}
