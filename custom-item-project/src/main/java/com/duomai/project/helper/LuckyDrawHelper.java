package com.duomai.project.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.api.taobao.enums.TaoBaoSendCouponStatus;
import com.duomai.project.configuration.annotation.JoinMemcache;
import com.duomai.project.product.general.entity.*;
import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.product.mengniuwawaji.service.ICusOrderInfoService;
import com.duomai.project.tool.CommonDateParseUtil;
import com.duomai.project.tool.ProjectTools;
import com.taobao.api.response.AlibabaBenefitSendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 抽奖 常规操作
 *
 * @description 【帮助类目录】
 * 1.发放游戏机会
 * 2.剩余抽奖次数
 * 3.今日某个来源的赠送次数
 * 3.某个来源的赠送次数
 * 4.抽奖
 * 5.直接发放指定优惠券奖品
 * 6.获取当前奖池中，奖品
 * 7.当前奖池等级
 * @create by 王星齐
 * @date 2020-08-26 16:52
 */
@Component
public class LuckyDrawHelper {
    @Autowired
    private ITaobaoAPIService taobaoAPIService;
    @Autowired
    private SysLuckyChanceRepository sysLuckyChanceRepository;
    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private SysSettingAwardRepository sysSettingAwardRepository;
    @Autowired
    private SysTaskMemberOrFollowRepository sysTaskMemberOrFollowRepository;
    @Autowired
    private ICusOrderInfoService cusOrderInfoService;
    @Autowired
    private SysExchangeLogRepository sysExchangeLogRepository;


    /**
     * 1.发放游戏机会
     *
     * @param buyerNick  发给哪个玩家(必填)
     * @param chanceFrom 什么原因发的(必填)
     * @param number     发几个(必填)
     * @param tid        如果是来自于订单，哪个订单(非必填)
     * @description
     * @create by 王星齐
     * @time 2020-08-28 16:13:28
     */
    @Transactional
    public List<SysLuckyChance> sendLuckyChance(String buyerNick, LuckyChanceFromEnum chanceFrom, Integer number, String tid, String messageTitle, String messageContent) {
        Date sendTime = new Date();
        return sysLuckyChanceRepository.saveAll(
                IntStream.range(0, number).mapToObj((i) -> {
                    SysLuckyChance sysLuckyChance = new SysLuckyChance().setBuyerNick(buyerNick)
                            .setChanceFrom(chanceFrom)
                            .setGetTime(sendTime)
                            .setGetTimeString(CommonDateParseUtil.date2string(sendTime, "yyyy-MM-dd"))
                            .setTidTime(sendTime)
                            .setIsUse(BooleanConstant.BOOLEAN_NO)
                            .setTid(tid)
                            .setHaveNotification(BooleanConstant.BOOLEAN_YES)
                            .setNotificationTitle("---")
                            .setNotificationContent("---");
                    if (i == 0) {
                        sysLuckyChance.setNotificationTitle(messageTitle);
                        sysLuckyChance.setNotificationContent(messageContent);
                        sysLuckyChance.setHaveNotification(BooleanConstant.BOOLEAN_NO);
                    }
                    return sysLuckyChance;
                }).collect(Collectors.toList()));
    }

    @Transactional
    public List<SysLuckyChance> sendLuckyChance(String buyerNick, LuckyChanceFromEnum chanceFrom, Integer number, String messageTitle, String messageContent) {
        return sendLuckyChance(buyerNick, chanceFrom, number, null, messageTitle, messageContent);
    }

    @Transactional
    public List<SysLuckyChance> sendLuckyChance(List<SysLuckyChance> sysLuckyChances) {
        return sysLuckyChanceRepository.saveAll(sysLuckyChances);
    }


    /**
     * 2.剩余抽奖次数
     *
     * @param buyerNick
     * @description
     * @create by 王星齐
     * @time 2020-08-28 19:31:12
     */
    public long unUseLuckyChance(String buyerNick) {
        return sysLuckyChanceRepository.countByBuyerNickAndIsUse(buyerNick, BooleanConstant.BOOLEAN_NO);
    }

    /**
     * 3.今日某个来源的赠送次数
     *
     * @param buyerNick
     * @description
     * @create by 王星齐
     * @time 2020-10-10 20:03:49
     */
    public long countTodayLuckyChanceFrom(String buyerNick, LuckyChanceFromEnum from) {
        Date today = new Date();
        return sysLuckyChanceRepository.countByBuyerNickAndChanceFromAndGetTimeString(buyerNick, from,
                CommonDateParseUtil.date2string(today, "yyyy-MM-dd"));
    }

    /**
     * 3.某个来源的赠送次数
     *
     * @param buyerNick
     * @description
     * @create by 王星齐
     * @time 2020-10-10 20:03:49
     */
    public long countLuckyChanceFrom(String buyerNick, LuckyChanceFromEnum from) {
        return sysLuckyChanceRepository.countByBuyerNickAndChanceFrom(buyerNick, from);
    }


    /**
     * 4.抽奖
     *
     * @param awards   当前奖池奖品
     * @param custom   哪个玩家
     * @param drawTime 抽象时间
     * @description
     * @create by 王星齐
     * @time 2020-08-28 17:04:19
     */
    @Transactional
    public SysSettingAward luckyDraw(List<SysSettingAward> awards, SysCustom custom, Date drawTime) throws Exception {
        return luckyDraw(awards, custom, drawTime, false);
    }

    @Transactional
    public SysSettingAward luckyDraw(List<SysSettingAward> awards, SysCustom custom, Date drawTime, Boolean freeFlag) throws Exception {
        //本次抽中的奖品
        SysSettingAward awardThisWin = null;
        SysLuckyChance thisChance = null;
        if (!freeFlag) {
            /*消耗一次抽奖次数*/
            thisChance = sysLuckyChanceRepository.findFirstByBuyerNickAndIsUse(custom.getBuyerNick(), BooleanConstant.BOOLEAN_NO);
            if (Objects.isNull(thisChance)) {
                throw new Exception("抽奖次数不足");
            }
            sysLuckyChanceRepository.save(thisChance.setIsUse(BooleanConstant.BOOLEAN_YES)
                    .setUseTime(drawTime));
        }

        /*整理抽奖日志*/
        SysLuckyDrawRecord drawRecord = new SysLuckyDrawRecord()
                .setLuckyChance(thisChance == null ? null : thisChance.getId())
                .setIsWin(BooleanConstant.BOOLEAN_NO)
                .setIsFill(BooleanConstant.BOOLEAN_NO)
                .setHaveExchange(BooleanConstant.BOOLEAN_NO)
                .setDrawTime(drawTime)
                .setPlayerHeadImg(custom.getHeadImg())
                .setPlayerBuyerNick(custom.getBuyerNick())
                .setPlayerZnick(custom.getZnick());

        try {

            /*1.整理历史抽奖记录*/
//            List<SysLuckyDrawRecord> historyWin = sysLuckyDrawRecordRepository.findByPlayerBuyerNickAndIsWin(custom.getBuyerNick(), BooleanConstant.BOOLEAN_YES);
            List<SysLuckyDrawRecord> historyWin = sysLuckyDrawRecordRepository.queryMybag(custom.getBuyerNick());
            StringBuffer historySignsBuffer = new StringBuffer();
            AtomicReference<Integer> historyGoodsHasGetAto = new AtomicReference<>(0);
            if (CollectionUtils.isNotEmpty(historyWin)) {
                historyWin.forEach((record) -> {
                    //非虚拟
                    if (!AwardTypeEnum.EXCHANGE.equals(record.getAwardType())) {
                        historySignsBuffer.append(record.getAwardName()).append(",");
                    }
                    //实物
                    if (AwardTypeEnum.GOODS.equals(record.getAwardType())) {
                        historyGoodsHasGetAto.updateAndGet(v -> v + 1);
                    }
                });
            }

            final String historySigns_awardName_hasWin = historySignsBuffer.toString();
            Integer historyGoodsHasGet = historyGoodsHasGetAto.get();


            /*2.开始随机抽奖,模拟选出本次抽奖中的奖品*/
            Integer maxWinGoodNum = ProjectTools.findMaxWinGoodNum();
            for (SysSettingAward award : awards) {
                //1.奖品数量不足;2.本活动最大实物中奖限制；3.已抽中过本奖品
                if (award.getRemainNum() < 1 ||
                        (AwardTypeEnum.GOODS.equals(award.getType()) && historyGoodsHasGet >= maxWinGoodNum) ||
                        historySigns_awardName_hasWin.contains(award.getName())) {
                    continue;
                }
                //奖品中奖概率
                if (Math.random() < Double.parseDouble(award.getLuckyValue())) {
                    awardThisWin = award;
                    break;
                }
            }

            /*3.发奖，发到手才算中奖*/
            //非酋退出
            if (awardThisWin == null) {
                return null;
            }

            /*已中奖的补充抽奖信息*/
            drawRecord.setIsWin(BooleanConstant.BOOLEAN_NO)
                    .setAwardId(awardThisWin.getId())
                    .setAwardImg(awardThisWin.getImg())
//                    .setAwardLevel(awardThisWin.getAwardLevel())
                    .setAwardName(awardThisWin.getName())
                    .setAwardType(awardThisWin.getType());

            //欧皇落泪  尝试扣减奖品库存，库存不够不中奖
            if (sysSettingAwardRepository.tryReduceOne(awardThisWin.getId()) != 1) {
                drawRecord.setSendError("尝试扣减奖品库存，库存不够不中奖");
                return null;
            }
            //2.如果中的是优惠券，发放优惠券
            if (AwardTypeEnum.COUPON.equals(awardThisWin.getType())) {
                try {
                    AlibabaBenefitSendResponse alibabaBenefitSendResponse = taobaoAPIService.sendTaobaoCoupon(custom.getOpenId(), awardThisWin.getEname());
                    if (alibabaBenefitSendResponse.getResultSuccess() == null || !alibabaBenefitSendResponse.getResultSuccess()) {
                        //发放失败,算未中奖
                        awardThisWin = null;

                        //发放失败
                        switch (alibabaBenefitSendResponse.getResultCode()) {
                            case "COUPON_INVALID_OR_DELETED":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.COUPON_INVALID_OR_DELETED.getInfo());
                                break;
                            case "APPLY_OWNSELF_COUPON":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.APPLY_OWNSELF_COUPON.getInfo());
                                break;
                            case "APPLY_SINGLE_COUPON_COUNT_EXCEED_LIMIT":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.APPLY_SINGLE_COUPON_COUNT_EXCEED_LIMIT.getInfo());
                                break;
                            case "权益已下线":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.OFF_LINE.getInfo());
                                break;
                            case "311权益已经被锁定":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.LOCK311.getInfo());
                                break;
                            case "APPLY_ONE_SELLER_COUNT_EXCEED_LIMIT":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.APPLY_ONE_SELLER_COUNT_EXCEED_LIMIT.getInfo());
                                break;
                            case "ERRORserver-invoke-mtee-exception":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORSERVER_INVOKE_MTEE_EXCEPTION.getInfo());
                                break;
                            case "NO_RIGHT_QUANTITY":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.NO_RIGHT_QUANTITY.getInfo());
                                break;
                            case "ERROR A_3_567":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORA_3_567.getInfo());
                                break;
                            case "ERROR A_3_00_005":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORA_3_00_005.getInfo());
                                break;
                            case "ERROR A_3_00_002":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORA_3_00_002.getInfo());
                                break;
                            default:
                                drawRecord.setSendError("发放优惠券失败：-->" + alibabaBenefitSendResponse.getResultCode() + ":-->" + alibabaBenefitSendResponse.getResultMsg());
                        }
                        drawRecord.setIsWin(BooleanConstant.BOOLEAN_NO);
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //发放异常,算未中奖
                    awardThisWin = null;
                    drawRecord.setSendError("发放优惠券异常：" + e.getMessage());
                    return null;
                }
            }
            drawRecord.setIsWin(BooleanConstant.BOOLEAN_YES);
            return awardThisWin;
        } finally {
            SysLuckyDrawRecord save = sysLuckyDrawRecordRepository.save(drawRecord);
            if (awardThisWin != null) {
                awardThisWin.setLogId(save.getId());
                if (AwardTypeEnum.EXCHANGE.equals(awardThisWin.getType())) {
                    SysLuckyExchangeLog sysLuckyExchangeLog = new SysLuckyExchangeLog()
                            .setAwardId(awardThisWin.getId())
                            .setAwardImg(awardThisWin.getImg())
                            .setAwardName(awardThisWin.getName())
                            .setCreateTime(drawTime)
                            .setBuyerNick(custom.getBuyerNick())
                            .setWinOrUse(BooleanConstant.BOOLEAN_YES);
                    sysExchangeLogRepository.save(sysLuckyExchangeLog);
                }
            }
        }
    }


    /**
     * 5.直接发放指定优惠券奖品
     *
     * @param award    奖品
     * @param custom   发给谁
     * @param sendTime 发放时间
     * @description
     * @create by 王星齐
     * @time 2020-11-08 19:13:48
     */
    @Transactional
    public boolean directSendCoupon(SysSettingAward award, SysCustom custom, Date sendTime, String remark) {
        Assert.isTrue(sysSettingAwardRepository.tryReduceOne(award.getId()) > 0, "奖品库存库存不足");
        /*整理抽奖日志*/
        boolean success = true;
        SysLuckyDrawRecord drawRecord = new SysLuckyDrawRecord()
                .setIsWin(BooleanConstant.BOOLEAN_YES)
                .setIsFill(BooleanConstant.BOOLEAN_NO)
                .setDrawTime(sendTime)
                .setPlayerHeadImg(custom.getHeadImg())
                .setPlayerBuyerNick(custom.getBuyerNick())
                .setPlayerZnick(custom.getZnick())
                .setAwardId(award.getId())
                .setAwardImg(award.getImg())
                .setAwardName(award.getName())
                .setAwardType(award.getType())
                .setRemark(remark);

        try {
            AlibabaBenefitSendResponse alibabaBenefitSendResponse = taobaoAPIService.sendTaobaoCoupon(custom.getOpenId(), award.getEname());
            if (alibabaBenefitSendResponse.getResultSuccess() == null || !alibabaBenefitSendResponse.getResultSuccess()) {
                //发放失败
                drawRecord.setSendError("发放优惠券失败：-->" + alibabaBenefitSendResponse.getResultCode() + ":-->" + alibabaBenefitSendResponse.getResultMsg());
                drawRecord.setIsWin(BooleanConstant.BOOLEAN_NO);
            }
            success = false;
        } catch (Exception e) {
            e.printStackTrace();
            //发放异常
            drawRecord.setIsWin(BooleanConstant.BOOLEAN_NO);
            drawRecord.setSendError("发放优惠券异常：" + e.getMessage());
            success = false;
        }
        sysLuckyDrawRecordRepository.save(drawRecord);
        return success;
    }

    /**
     * 6.获取当前奖池中，奖品
     *
     * @param sysCustom
     * @description
     * @create by 王星齐
     * @time 2020-11-08 19:13:22
     */
//    @Transactional
//    public List<SysAward> findCustomTimeAwardPool(SysCustom sysCustom) {
//        PoolLevelEnum currentPoolLevel = findCurrentPoolLevel(sysCustom).getCurrentPoolLevel();
//        return sysAwardRepository.findByUseWayAndPoolLevelLessThanEqualOrderByLuckyValueAsc(AwardUseWayEnum.POOL, currentPoolLevel.getValue());
//    }

    /**
     * 7.当前奖池等级
     *
     * @param sysCustom
     * @description todo:等待落实奖池升级规则
     * @create by 王星齐
     * @time 2020-11-08 19:12:41
     */
//    @Transactional
//    public CustomPlayProgressDto findCurrentPoolLevel(SysCustom sysCustom) {
//        CustomPlayProgressDto customPlayProgressDto = new CustomPlayProgressDto();
//        //累计签到天数
//        long sign_num = 0;
////        long sign_num = sysTaskMemberOrFollowRepository.countByBuyerNickAndTaskType(sysCustom.getBuyerNick(), TaskTypeEnum.SIGN);
//
//        //下单数
//        Integer orderNum = cusOrderInfoService.countTidsByBuyerNick(sysCustom.getBuyerNick());
//
//        PoolLevelEnum level = PoolLevelEnum.LEVEL_1;
//        if (sign_num >= 1) {
//            level = PoolLevelEnum.LEVEL_1;
//        }
//        if (sign_num >= 3) {
//            level = PoolLevelEnum.LEVEL_2;
//        }
//        if (sign_num >= 5 && orderNum >= 1) {
//            level = PoolLevelEnum.LEVEL_3;
//        }
//        if (sign_num >= 7 && orderNum >= 1) {
//            level = PoolLevelEnum.LEVEL_4;
//        }
//        if (sign_num >= 11 && orderNum >= 2) {
//            level = PoolLevelEnum.LEVEL_5;
//        }
//        customPlayProgressDto.setCurrentPoolLevel(level);
//        customPlayProgressDto.setOrderNum(orderNum);
//        customPlayProgressDto.setSignNum((int) sign_num);
//        return customPlayProgressDto;
//    }


    /**
     * @param unUseBattles
     * @param custom
     * @param award
     * @param sendTime
     */
    @Transactional
    public SysLuckyDrawRecord directExchangeAward(List<SysLuckyDrawRecord> unUseBattles, SysCustom custom, SysSettingAward award, Date sendTime) {
        Assert.isTrue(sysSettingAwardRepository.tryReduceOne(award.getId()) > 0, "你来晚了,奖品已兑完");

        StringBuffer stringBuffer = new StringBuffer();
        unUseBattles.forEach(x -> {
            stringBuffer.append(x.getId()).append(",");
        });

        /*整理抽奖日志*/
        SysLuckyDrawRecord drawRecord = new SysLuckyDrawRecord()
                .setIsWin(BooleanConstant.BOOLEAN_YES)
                .setIsFill(BooleanConstant.BOOLEAN_NO)
                .setDrawTime(sendTime)
                .setHaveExchange(BooleanConstant.BOOLEAN_NO)
                .setExchangeTime(sendTime)
                .setPlayerHeadImg(custom.getHeadImg())
                .setPlayerBuyerNick(custom.getBuyerNick())
                .setPlayerZnick(custom.getZnick())
                .setAwardId(award.getId())
                .setAwardImg(award.getImg())
//                .setAwardLevel(award.getAwardLevel())
                .setAwardName(award.getName())
                .setAwardType(award.getType())
                .setRemark("消耗:" + stringBuffer.toString() + "兑换:" + award.getName());

        if (AwardTypeEnum.COUPON.equals(award.getType())) {
            try {
                AlibabaBenefitSendResponse alibabaBenefitSendResponse = taobaoAPIService.sendTaobaoCoupon(custom.getOpenId(), award.getEname());
                if (alibabaBenefitSendResponse.getResultSuccess() == null || !alibabaBenefitSendResponse.getResultSuccess()) {
                    //发放失败
                    switch (alibabaBenefitSendResponse.getResultCode()) {
                        case "COUPON_INVALID_OR_DELETED":
                            drawRecord.setSendError(TaoBaoSendCouponStatus.COUPON_INVALID_OR_DELETED.getInfo());
                            break;
                        case "APPLY_OWNSELF_COUPON":
                            drawRecord.setSendError(TaoBaoSendCouponStatus.APPLY_OWNSELF_COUPON.getInfo());
                            break;
                        case "APPLY_SINGLE_COUPON_COUNT_EXCEED_LIMIT":
                            drawRecord.setSendError(TaoBaoSendCouponStatus.APPLY_SINGLE_COUPON_COUNT_EXCEED_LIMIT.getInfo());
                            break;
                        case "权益已下线":
                            drawRecord.setSendError(TaoBaoSendCouponStatus.OFF_LINE.getInfo());
                            break;
                        case "311权益已经被锁定":
                            drawRecord.setSendError(TaoBaoSendCouponStatus.LOCK311.getInfo());
                            break;
                        case "APPLY_ONE_SELLER_COUNT_EXCEED_LIMIT":
                            drawRecord.setSendError(TaoBaoSendCouponStatus.APPLY_ONE_SELLER_COUNT_EXCEED_LIMIT.getInfo());
                            break;
                        case "ERRORserver-invoke-mtee-exception":
                            drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORSERVER_INVOKE_MTEE_EXCEPTION.getInfo());
                            break;
                        case "NO_RIGHT_QUANTITY":
                            drawRecord.setSendError(TaoBaoSendCouponStatus.NO_RIGHT_QUANTITY.getInfo());
                            break;
                        case "ERROR A_3_567":
                            drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORA_3_567.getInfo());
                            break;
                        case "ERROR A_3_00_005":
                            drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORA_3_00_005.getInfo());
                            break;
                        case "ERROR A_3_00_002":
                            drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORA_3_00_002.getInfo());
                            break;
                        default:
                            drawRecord.setSendError("发放优惠券失败：-->" + alibabaBenefitSendResponse.getResultCode() + ":-->" + alibabaBenefitSendResponse.getResultMsg());
                    }
                    drawRecord.setIsWin(BooleanConstant.BOOLEAN_NO);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //发放异常
                drawRecord.setIsWin(BooleanConstant.BOOLEAN_NO);
                drawRecord.setSendError("发放优惠券异常：" + e.getMessage());
            }
        }

        if (BooleanConstant.BOOLEAN_YES.equals(drawRecord.getIsWin())) {
            //标记已兑换的瓶子
            String[] split = stringBuffer.toString().split(",");
            sysLuckyDrawRecordRepository.exchangeAward(split, sendTime);

            //记录瓶子兑换日志
            List<SysLuckyExchangeLog> collect = unUseBattles.stream().map(x -> new SysLuckyExchangeLog()
                    .setAwardId(x.getAwardId())
                    .setAwardImg(x.getAwardImg())
                    .setAwardName(x.getAwardName())
                    .setCreateTime(sendTime)
                    .setBuyerNick(custom.getBuyerNick())
                    .setWinOrUse(BooleanConstant.BOOLEAN_NO)).collect(Collectors.toList());
            sysExchangeLogRepository.saveAll(collect);
        }
        //保存兑换记录
        sysLuckyDrawRecordRepository.save(drawRecord);
        return drawRecord;
    }


    @Transactional
    public SysSettingAward luckyDraw(List<SysSettingAward> awards, SysCustom custom, Date drawTime, String chance) throws Exception {
        //本次抽中的奖品
        SysSettingAward awardThisWin = null;

        /*整理抽奖日志*/
        String drawTimeString = CommonDateParseUtil.date2string(drawTime, "yyyy-MM-dd");
        SysLuckyDrawRecord drawRecord = new SysLuckyDrawRecord()
                .setLuckyChance(chance)
                .setIsWin(BooleanConstant.BOOLEAN_NO)
                .setIsFill(BooleanConstant.BOOLEAN_NO)
                .setDrawTime(drawTime)
                .setDrawTimeString(drawTimeString)
                .setPlayerHeadImg(custom.getHeadImg())
                .setPlayerBuyerNick(custom.getBuyerNick())
                .setHaveExchange(BooleanConstant.BOOLEAN_UNDEFINED)
                .setPlayerZnick(custom.getZnick());


        try {
            /*1.整理历史抽奖记录*/
            List<String> todayHasLuckyWin = sysLuckyDrawRecordRepository.todayHasLuckyWin(custom.getBuyerNick(), drawTimeString);
//            List<SysLuckyDrawRecord> historyWin = sysLuckyDrawRecordRepository.queryMybag(custom.getBuyerNick());
//            StringBuffer historySignsBuffer = new StringBuffer();
//            AtomicReference<Integer> historyGoodsHasGetAto = new AtomicReference<>(0);
//            if (CollectionUtils.isNotEmpty(historyWin)) {
//                historyWin.forEach((record) -> {
//                    historySignsBuffer
//                    if (AwardTypeEnum.GOODS.equals(record.getAwardType())) {
//                        historyGoodsHasGetAto.updateAndGet(v -> v + 1);
//                    }
//                });
//            }

//            final String historySigns_awardName_hasWin = historySignsBuffer.toString();
//            Integer historyGoodsHasGet = historyGoodsHasGetAto.get();


            /*2.开始随机抽奖,模拟选出本次抽奖中的奖品*/
//            Integer maxWinGoodNum = ProjectTools.findMaxWinGoodNum();
            for (SysSettingAward award : awards) {
                //1.奖品数量不足;2.本活动最大实物中奖限制；3.已抽中过本奖品
                if (award.getRemainNum() < 1
                        ||
//                        (AwardTypeEnum.GOODS.equals(award.getType()) && historyGoodsHasGet >= maxWinGoodNum) ||
                        todayHasLuckyWin.contains(award.getId())
                ) {
                    continue;
                }
                //奖品中奖概率
                if (Math.random() < Double.parseDouble(award.getLuckyValue())) {
                    awardThisWin = award;
                    break;
                }
            }

            /*3.发奖，发到手才算中奖*/
            //非酋退出
            if (awardThisWin == null) {
                return null;
            }

            /*已中奖的补充抽奖信息*/
            drawRecord.setIsWin(BooleanConstant.BOOLEAN_NO)
                    .setAwardId(awardThisWin.getId())
                    .setAwardImg(awardThisWin.getImg())
                    .setAwardName(awardThisWin.getName())
                    .setAwardType(awardThisWin.getType());

            //欧皇落泪  尝试扣减奖品库存，库存不够不中奖
            if (sysSettingAwardRepository.tryReduceOne(awardThisWin.getId()) != 1) {
                drawRecord.setSendError("尝试扣减奖品库存，库存不够不中奖");
                return null;
            }
            //单品券
            if(AwardTypeEnum.ITEM.equals(awardThisWin.getType())){
                drawRecord.setRemark(awardThisWin.getWinImg());
            }


            //2.如果中的是优惠券，发放优惠券
            if (AwardTypeEnum.COUPON.equals(awardThisWin.getType())) {
                try {
                    AlibabaBenefitSendResponse alibabaBenefitSendResponse = taobaoAPIService.sendTaobaoCoupon(custom.getOpenId(), awardThisWin.getEname());

                    if (alibabaBenefitSendResponse.getResultSuccess() == null || !alibabaBenefitSendResponse.getResultSuccess()) {
                        //发放失败,算未中奖
                        awardThisWin = null;

                        //发放失败
                        switch (alibabaBenefitSendResponse.getResultCode()) {
                            case "COUPON_INVALID_OR_DELETED":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.COUPON_INVALID_OR_DELETED.getInfo());
                                break;
                            case "APPLY_OWNSELF_COUPON":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.APPLY_OWNSELF_COUPON.getInfo());
                                break;
                            case "APPLY_SINGLE_COUPON_COUNT_EXCEED_LIMIT":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.APPLY_SINGLE_COUPON_COUNT_EXCEED_LIMIT.getInfo());
                                break;
                            case "权益已下线":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.OFF_LINE.getInfo());
                                break;
                            case "311权益已经被锁定":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.LOCK311.getInfo());
                                break;
                            case "APPLY_ONE_SELLER_COUNT_EXCEED_LIMIT":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.APPLY_ONE_SELLER_COUNT_EXCEED_LIMIT.getInfo());
                                break;
                            case "ERRORserver-invoke-mtee-exception":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORSERVER_INVOKE_MTEE_EXCEPTION.getInfo());
                                break;
                            case "NO_RIGHT_QUANTITY":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.NO_RIGHT_QUANTITY.getInfo());
                                break;
                            case "ERROR A_3_567":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORA_3_567.getInfo());
                                break;
                            case "ERROR A_3_00_005":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORA_3_00_005.getInfo());
                                break;
                            case "ERROR A_3_00_002":
                                drawRecord.setSendError(TaoBaoSendCouponStatus.ERRORA_3_00_002.getInfo());
                                break;
                            default:
                                drawRecord.setSendError("发放优惠券失败：-->" + alibabaBenefitSendResponse.getResultCode() + ":-->" + alibabaBenefitSendResponse.getResultMsg());
                        }
                        drawRecord.setIsWin(BooleanConstant.BOOLEAN_NO);
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //发放异常,算未中奖
                    awardThisWin = null;
                    drawRecord.setSendError("发放优惠券异常：" + e.getMessage());
                    return null;
                }
            }
            drawRecord.setIsWin(BooleanConstant.BOOLEAN_YES);
            return awardThisWin;
        } finally {
            SysLuckyDrawRecord save = sysLuckyDrawRecordRepository.save(drawRecord);
            if (awardThisWin != null) {
                awardThisWin.setLogId(save.getId());
            }
        }
    }


    /**
     * 弹幕
     *
     * @return
     */
    @JoinMemcache(refreshTime = 10)
    public List<SysLuckyDrawRecord> luckyBarrage() {
        List<SysLuckyDrawRecord> temp = new ArrayList<>();
        List<Map> maps = sysLuckyDrawRecordRepository.queryExchangeLog();
        if (!CollectionUtils.isEmpty(maps)) {
            temp = JSONArray.parseArray(JSON.toJSONString(maps), SysLuckyDrawRecord.class);
        }
        return temp;
    }
}
