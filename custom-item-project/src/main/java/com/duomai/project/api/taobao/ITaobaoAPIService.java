package com.duomai.project.api.taobao;

import com.duomai.project.api.taobao.enums.TaoBaoTradeStatus;
import com.taobao.api.ApiException;
import com.taobao.api.response.AlibabaBenefitSendResponse;
import com.taobao.api.response.CrmMemberIdentityGetResponse;
import com.taobao.api.response.OpenTradesSoldGetResponse;

import java.util.Date;
import java.util.List;

public interface ITaobaoAPIService {


    /**
     * 查询会员等级，
     * 需要申请权限
     *
     * @作者 何佳伟
     * @创建日期 2020-03-05
     */
    public CrmMemberIdentityGetResponse CrmMemberIdentityGet(String buyerNick, String sessionkey) throws ApiException;

    /*查询是否为店铺会员
     * @description 需要申请权限
     * @create by 王星齐
     * @time 2020-08-24 10:14:01
     * @param buyerNick
     * @param sessionkey
     **/
    public boolean isMember(String buyerNick, String sessionkey) throws ApiException;

    /*查询是否为店铺粉丝
     * @description 需要申请权限
     * @create by 王星齐
     * @time 2020-08-24 10:14:01
     * @param buyerNick
     * @param sessionkey
     **/
    public boolean isFans(String buyerNick,String sellernick) throws ApiException;


    /*查询卖家已卖出的交易数据（商家应用使用）
     * @description   需要申请权限
     * @create by 王星齐
     * @time 2020-05-14 09:29:48
     * @param buyer_open_id
     * @param status
     * @param start_created
     * @param end_created
     **/
    List<OpenTradesSoldGetResponse.Trade> taobaoOpenTradesSoldGet(String buyer_open_id, String buyer_nick, String sessionKey, TaoBaoTradeStatus status, Date start_created, Date end_created) throws ApiException;


    /*直发券
     * @description  需要申请权限
     * @create by 王星齐
     * @time 2020-06-30 14:09:54
     * @param mixNick
     * @param sessionKey
     **/
    AlibabaBenefitSendResponse sendTaobaoCoupon(String openId, String ename, String APP_NAME, String SESSION_KEY) throws ApiException;
}
