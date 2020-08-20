package com.duomai.project.api.gateway.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * API请求
 * 接收API的参数，包含系统级参数和业务级参数，业务级参数中的值为一个json格式的字符串
 */
@Data
public class ApiSysParameter {

    /**
     * JsonRpc的版本号
     */
    private String jsonrpc = "2.0";

    /**
     * 调用方法名
     */
    private String method;

    //参数，包括公共参数和业务级参数
    /**
     *
     */
    private ApiParameter params;

    /**
     * 调用标识符
     */
    private String id;


    @JSONField(name = "params")
    public ApiParameter getApiParameter() {
        return params;
    }

    @JSONField(name = "params")
    public void setApiParameter(ApiParameter apiParameter) {
        this.params = apiParameter;
    }

    public void findYunTokenParameter(HttpServletRequest request) {
        String userNick = request.getParameter("user_nick");
        String mixNick = request.getParameter("mix_nick");
        String openUid = request.getParameter("open_id");
        String dmZNick = request.getParameter("dmZNick");
        String dmAvatar = request.getParameter("dmAvatar");

        YunTokenParameter yunTokenParameter = new YunTokenParameter();
        yunTokenParameter.setAppKey(this.params.getCommomParameter().getAppkey());
        yunTokenParameter.setBuyerNick(mixNick);
        yunTokenParameter.setUserNick(userNick);
        yunTokenParameter.setOpenUId(openUid);
        yunTokenParameter.setTimestamp(System.currentTimeMillis() + "");
        yunTokenParameter.setDmAvatar(dmAvatar);
        yunTokenParameter.setDmZNick(dmZNick);
    }
}
