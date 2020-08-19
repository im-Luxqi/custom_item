package com.duomai.new_custom_base.api.product.core.pojo;

import lombok.Data;

/**
 * 云应用公共参数
 */
@Data
public class YunTokenParameter {

    private String appKey;

    private String openUId;

    private String timestamp;

    private String userId;

    private String userNick;

    private String buyerNick;

    private String dmZNick;
    private String dmAvatar;

}
