//package com.duomai.project.product.mengniuwawaji.execute;
//
//import cn.hutool.core.lang.Assert;
//import com.alibaba.fastjson.JSONObject;
//import com.duomai.common.base.execute.IApiExecute;
//import com.duomai.common.constants.BooleanConstant;
//import com.duomai.common.dto.ApiSysParameter;
//import com.duomai.common.dto.YunReturnValue;
//import com.duomai.project.helper.LuckyDrawHelper;
//import com.duomai.project.product.general.entity.SysCustom;
//import com.duomai.project.product.general.entity.SysLuckyChance;
//import com.duomai.project.product.general.entity.SysSettingAward;
//import com.duomai.project.product.general.enums.AwardTypeEnum;
//import com.duomai.project.product.general.enums.AwardUseWayEnum;
//import com.duomai.project.product.general.enums.BottleTypeEnum;
//import com.duomai.project.product.general.repository.SysCustomRepository;
//import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
//import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
//import com.duomai.project.product.general.repository.SysSettingAwardRepository;
//import com.duomai.project.tool.CommonDateParseUtil;
//import com.duomai.project.tool.ProjectTools;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Objects;
//
///**
// * 首页 抓娃娃
// *
// * @author 王星齐
// * @description
// * @create 2020/11/19 15:13
// */
//@Component
//public class GameIndexLuckyDrawExecute implements IApiExecute {
//    @Autowired
//    private SysCustomRepository sysCustomRepository;
//    @Autowired
//    private LuckyDrawHelper luckyDrawHelper;
//    @Autowired
//    private SysSettingAwardRepository sysSettingAwardRepository;
//    @Autowired
//    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
//    @Autowired
//    private SysLuckyChanceRepository sysLuckyChanceRepository;
//
//
//    @Override
//    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//
//        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
//        BottleTypeEnum bottle = ProjectTools.enumValueOf(BottleTypeEnum.class, jsonObjectAdmjson.getString("bottle"));
//        Assert.notNull(bottle, "奶瓶不能为空");
//
//
//        /*1.校验是否存在玩家*/
//        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
//        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
//        Assert.notNull(syscustom, "无效的玩家");
//        Assert.isTrue(BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveAuthorization()), "请先授权");
//
//
//        //一人每天只能抽15次
//        long todayHasDraw = sysLuckyChanceRepository.countByBuyerNickAndIsUseAndUseTimeBetween(buyerNick,BooleanConstant.BOOLEAN_YES,
//                CommonDateParseUtil.getStartTimeOfDay(sysParm.getRequestStartTime()), CommonDateParseUtil.getEndTimeOfDay(sysParm.getRequestStartTime()));
////        long todayHasDraw = sysLuckyDrawRecordRepository.countByPlayerBuyerNickAndAwardTypeAndDrawTimeBetween(buyerNick, AwardTypeEnum.EXCHANGE,
////                CommonDateParseUtil.getStartTimeOfDay(sysParm.getRequestStartTime()), CommonDateParseUtil.getEndTimeOfDay(sysParm.getRequestStartTime()));
//        Assert.isTrue(todayHasDraw < 15, "今日抽奖次数已达到上限，明天再来看吧");
//        //抽6号瓶子需要先抽5号瓶子
//        if (BottleTypeEnum.SPECIAL_SIX.equals(bottle)) {
//            List<SysSettingAward> byUseWay = sysSettingAwardRepository.findByUseWay(AwardUseWayEnum.SPECIAL_FIVE);
//            SysSettingAward firstSysSettingAward = byUseWay.get(0);
//            long l = sysLuckyDrawRecordRepository.countByPlayerBuyerNickAndAwardIdAndIsWinAndHaveExchange(buyerNick, firstSysSettingAward.getId(), BooleanConstant.BOOLEAN_YES, BooleanConstant.BOOLEAN_NO);
//            if (l == 0) {
//                LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
//                /*消耗一次抽奖次数,但不中奖*/
//                SysLuckyChance thisChance = sysLuckyChanceRepository.findFirstByBuyerNickAndIsUse(syscustom.getBuyerNick(), BooleanConstant.BOOLEAN_NO);
//                if (Objects.isNull(thisChance)) {
//                    throw new Exception("抽奖次数不足");
//                }
//                sysLuckyChanceRepository.save(thisChance.setIsUse(BooleanConstant.BOOLEAN_YES)
//                        .setUseTime(sysParm.getRequestStartTime()));
//                resultMap.put("win", false);
//                resultMap.put("award", null);
//                return YunReturnValue.ok(resultMap, "玩家成功进行抽奖");
//            }
//        }
//
//
//        List<SysSettingAward> thisTimeAwardPool = sysSettingAwardRepository.findByUseWay(ProjectTools.enumValueOf(AwardUseWayEnum.class, bottle.getValue()));
//        SysSettingAward winAward = luckyDrawHelper.luckyDraw(thisTimeAwardPool, syscustom, sysParm.getRequestStartTime());
//
//        /*只反馈有效数据*/
//        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
//        resultMap.put("win", !Objects.isNull(winAward));
//        resultMap.put("award", winAward);
//        if (!Objects.isNull(winAward)) {
//            winAward.setEname(null)
//                    .setId(null)
//                    .setRemainNum(null)
//                    .setSendNum(null)
//                    .setTotalNum(null)
//                    .setLuckyValue(null)
//                    .setUseWay(null)
//                    .setLogId(null)
//                    .setType(null)
////                    .setAwardLevel(null)
////                    .setAwardLevelSign(null)
//                    .setPoolLevel(null);
//        }
//        return YunReturnValue.ok(resultMap, "玩家成功进行抽奖");
//    }
//}
