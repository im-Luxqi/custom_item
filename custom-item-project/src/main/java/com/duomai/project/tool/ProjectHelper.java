package com.duomai.project.tool;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.product.general.constants.ActSettingConstant;
import com.duomai.project.product.general.dto.ActBaseSetting;
import com.duomai.project.product.general.dto.XyReturn;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysKeyValue;
import com.duomai.project.product.general.repository.SysKeyValueRepository;
import com.duomai.project.product.recycle.domain.XyRequest;
import com.duomai.project.product.recycle.service.IXyRequestService;
import com.duomai.starter.SysProperties;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * act，customer常规操作
 *
 * @description
 * @create by 王星齐
 * @date 2020-08-26 16:52
 */
@Component
public class ProjectHelper {
    @Autowired
    private ITaobaoAPIService taobaoAPIService;
    @Autowired
    private SysProperties sysProperties;
    @Autowired
    private SysKeyValueRepository sysKeyValueRepository;
    @Autowired
    private IXyRequestService xyRequestService;


    /* 活动配置--信息获取
     * @description
     * @create by 王星齐
     * @time 2020-08-26 20:03:20
     **/
    public ActBaseSetting actBaseSettingFind() {
        List<SysKeyValue> byType = sysKeyValueRepository.findByType(ActSettingConstant.TYPE_ACT_SETTING);
        Map<String, String> collect = byType.stream().collect(Collectors.toMap(SysKeyValue::getK, SysKeyValue::getV));
        return new ActBaseSetting().setActRule(collect.get(ActSettingConstant.ACT_RULE))
                .setActStartTime(CommonDateParseUtil.string2date(collect.get(ActSettingConstant.ACT_START_TIME), CommonDateParseUtil.YYYY_MM_DD))
                .setActEndTime(CommonDateParseUtil.getEndTimeOfDay(CommonDateParseUtil.string2date(collect.get(ActSettingConstant.ACT_END_TIME), CommonDateParseUtil.YYYY_MM_DD)));
    }

    /* 活动配置--检验时候为活动期间
     * @description ，不在活动范围，抛出异常不往下执行
     * @create by 王星齐
     * @time 2020-08-26 20:11:04
     * @param actBaseSetting
     **/
    public void actTimeValidate(ActBaseSetting actBaseSetting) throws Exception {
        Date now = new Date();
        if (now.before(actBaseSetting.getActStartTime()))
            throw new Exception("活动尚未开始，尽情期待！");
        if (now.after(actBaseSetting.getActEndTime()))
            throw new Exception("活动已结束！");
    }

    /* 活动配置--检验时候为活动期间
     * @description ，处于活动期间返回true
     * @create by 王星齐
     * @time 2020-08-26 20:11:04
     * @param actBaseSetting
     **/
    public boolean actTimeValidateFlag(ActBaseSetting actBaseSetting) {
        Date now = new Date();
        boolean liveFlag = true;
        if (now.before(actBaseSetting.getActStartTime()) || now.after(actBaseSetting.getActEndTime()))
            liveFlag = false;
        return liveFlag;
    }


    /* 客户--信息初始化
     * @description
     * @create by 王星齐
     * @time 2020-08-26 17:15:05
     * @param sysParm  系统参数
     * @param hasAttention  是否已关注店铺（因为是否老粉丝由前端查询）
     **/
    public SysCustom customInit(ApiSysParameter sysParm) throws ApiException {
        SysCustom sysCustom = new SysCustom();

        return sysCustom.setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                .setCreateTime(sysParm.getRequestStartTime())
                .setOpenId(sysParm.getApiParameter().getYunTokenParameter().getOpenUId())
                .setOldFans(BooleanConstant.BOOLEAN_UNDEFINED)//由于关注权限在前端查询，此处默认未知状态(-1),等待首次更新
                .setOldMember(taobaoAPIService.isMember(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick(),
                        sysProperties.getCustomConfig().getSessionkey()) ? BooleanConstant.BOOLEAN_YES : BooleanConstant.BOOLEAN_NO)
                .setFans(sysCustom.getOldFans().equals(BooleanConstant.BOOLEAN_YES) ? BooleanConstant.BOOLEAN_YES : BooleanConstant.BOOLEAN_NO)
                .setMember(sysCustom.getOldMember().equals(BooleanConstant.BOOLEAN_YES) ? BooleanConstant.BOOLEAN_YES : BooleanConstant.BOOLEAN_NO);
    }


    /* 根据订单号查询订单信息
     * @description
     * @create by 王星齐
     * @time 2020-08-31 16:27:04
     **/
    public XyReturn findOrderBySn(Long timestamp, String orderSn) {
        final String appId = "adidas";
        final String appSecret = "4usEfQ3B5G9TEj*g";
        String format = String.format("appId=%s&appSecret=%s&orderSn=%s&timestamp=%s", appId, appSecret, orderSn, timestamp);
        String sign = SecureUtil.md5(format);
//        String sign = DigestUtils.md5DigestAsHex(format.getBytes(Consts.UTF_8));
        String s = HttpClientUtil.doGet(String.format("https://vues.dd1x.cn/api/web_orders/get_order_info?appId=%s&timestamp=%s&orderSn=%s&sign=%s"
                , appId, timestamp, orderSn, sign));
        return JSONObject.parseObject(s, XyReturn.class);
    }


    /* 根据用户id查询订单信息
     * @description
     * @create by 王星齐
     * @time 2020-08-31 16:27:04
     **/
    public XyReturn findOrdersByOpenId(Long timestamp, String openId, Long startTime, Long endTime, String buyerNick, Date requestTime) {
        final String appId = "adidas";
        final String appSecret = "4usEfQ3B5G9TEj*g";
        String format = String.format("appId=%s&appSecret=%s&openid=%s&timestamp=%s", appId, appSecret, openId, timestamp);
        String sign = SecureUtil.md5(format);
        String s = HttpClientUtil.doGet(String.format("https://vues.dd1x.cn/api/web_orders/get_order_info_by_openid?appId=%s&timestamp=%s&openid=%s&sign=%s&startTime=%s&endTime=%s"
                , appId, timestamp, openId, sign, startTime, endTime));
        XyReturn xyReturn = JSONObject.parseObject(s, XyReturn.class);

        Map<String, Object> requestData = new HashMap();
        requestData.put("appId", appId);
        requestData.put("appSecret", appSecret);
        requestData.put("openId", openId);
        requestData.put("timestamp", timestamp);
        requestData.put("startTime", startTime);
        requestData.put("sign", sign);
        requestData.put("endTime", endTime);
        xyRequestService.save(new XyRequest().setBuyerNick(buyerNick).setRequestTime(requestTime)
                .setRequestData(JSONObject.toJSONString(requestData))
                .setResponseData(JSONObject.toJSONString(xyReturn))
                .setSuccesss(xyReturn.getCode().equals(0) ? BooleanConstant.BOOLEAN_YES : BooleanConstant.BOOLEAN_NO));
        return xyReturn;
    }


}
