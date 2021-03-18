package com.duomai.project.api.taobao;

import com.duomai.project.api.taobao.enums.TaoBaoTradeStatus;
import com.taobao.api.ApiException;
import com.taobao.api.response.AlibabaBenefitSendResponse;
import com.taobao.api.response.CrmMemberIdentityGetResponse;
import com.taobao.api.response.CrmPointChangeResponse;
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
    public CrmMemberIdentityGetResponse CrmMemberIdentityGet(String buyerNick) throws ApiException;

    /**
     * 查询是否为店铺会员
     *
     * @param buyerNick
     * @description 需要申请权限
     * @create by 王星齐
     * @time 2020-08-24 10:14:01
     */
    public boolean isMember(String buyerNick) throws ApiException;


    /**
     * 查询卖家已卖出的交易数据（商家应用使用）
     *
     * @param buyer_open_id
     * @param status
     * @param start_created
     * @param end_created
     * @description
     * @create by 王星齐
     * @time 2021-03-17 11:33:19
     **/
    List<OpenTradesSoldGetResponse.Trade> taobaoOpenTradesSoldGet(String buyer_open_id, TaoBaoTradeStatus status, Date start_created, Date end_created) throws ApiException;


    /**
     * 直发券,发放红包
     *
     * @param openId
     * @param ename
     * @description
     * @create by 王星齐
     * @time 2021-03-17 11:32:25
     */
    AlibabaBenefitSendResponse sendTaobaoCoupon(String openId, String ename) throws ApiException;


    /**
     * 积分变更
     *
     * @return
     * @throws Exception
     */
    CrmPointChangeResponse changePoint(String buyerNick, Long point) throws Exception;

}
