package com.duomai.project.product.mengniuwawaji.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysSettingAward;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.product.general.repository.SysSettingAwardRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * 开礼盒
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameOpenLuckyBoxExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private SysSettingAwardRepository sysSettingAwardRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        projectHelper.actTimeValidate();

        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        String awardPlace = jsonObjectAdmjson.getString("awardPlace");
        Assert.hasLength(awardPlace, "奖品位置awardPlace不能为空，" +
                "可选值[award_penguin,award_party2,award_tent]");
        Assert.isTrue("award_penguin".equals(awardPlace) ||
                "award_party2".equals(awardPlace) ||
                "award_tent".equals(awardPlace), "无效的奖品位置");

        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Date requestStartTime = sysParm.getRequestStartTime();
        String requestStartTimeString = CommonDateParseUtil.date2string(requestStartTime, "yyyy-MM-dd");
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        AwardUseWayEnum useWay = null;

        //企鹅奖品
        if ("award_penguin".equals(awardPlace)) {
            useWay = AwardUseWayEnum.PENGUIN;
        }
        //party2开场奖品
        if ("award_party2".equals(awardPlace)) {
            useWay = AwardUseWayEnum.PARTY2;
        }
        //帐篷奖品
        if ("award_tent".equals(awardPlace)) {
            useWay = AwardUseWayEnum.TENT;
        }

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        long l = sysLuckyDrawRecordRepository.countByPlayerBuyerNickAndLuckyChance(buyerNick, useWay.getValue());
        Assert.isTrue(l == 0, "仅限一次");
        List<SysSettingAward> awards = sysSettingAwardRepository.findByUseWay(useWay);
        SysSettingAward winAward = luckyDrawHelper.luckyDraw(awards, syscustom, requestStartTime, useWay.getValue());
        /*只反馈有效数据*/
        resultMap.put("win", !Objects.isNull(winAward));
        resultMap.put("award", winAward);
        if (!Objects.isNull(winAward)) {
            winAward.setEname(null)
                    .setId(null)
                    .setRemainNum(null)
                    .setSendNum(null)
                    .setTotalNum(null)
                    .setLuckyValue(null)
                    .setUseWay(null)
                    .setType(null)
                    .setPoolLevel(null);
        }
        return YunReturnValue.ok(resultMap, "开礼盒");
    }
}




