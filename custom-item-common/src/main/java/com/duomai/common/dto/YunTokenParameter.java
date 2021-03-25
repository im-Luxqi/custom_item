package com.duomai.common.dto;

import com.duomai.common.annotation.ParamName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 云应用公共参数
 */
@Data
public class YunTokenParameter {

    @ParamName("app_key")
    private String appKey;
    @NotBlank(message = "open_id不能为空")
    @ParamName("open_id")
    private String openUId;
    @NotBlank(message = "mix_nick不能为空")
    @ParamName("mix_nick")
    private String buyerNick;
    @ParamName("user_nick")
    private String userNick;
    @ParamName("access_token")
    private String accessToken;
    private String dmZNick;
    private String dmAvatar;

}
