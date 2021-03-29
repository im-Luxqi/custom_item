package com.duomai.project.helper;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.api.taobao.enums.TaoBaoSendCouponStatus;
import com.duomai.project.configuration.annotation.JoinMemcache;
import com.duomai.project.product.general.dto.CardExchangeDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.entity.SysSettingAward;
import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.product.general.repository.SysSettingAwardRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import com.taobao.api.response.AlibabaBenefitSendResponse;
import com.taobao.api.response.CrmPointChangeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
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

    @Transactional
    public List<SysLuckyChance> sendLuckyChance(List<SysLuckyChance> sysLuckyChances) {
        return sysLuckyChanceRepository.saveAll(sysLuckyChances);
    }


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
    public List<SysLuckyChance> sendLuckyChance(Boolean success, String buyerNick, LuckyChanceFromEnum chanceFrom, AwardUseWayEnum cardType, Integer number, String tid, String cardImg, String messageContent) {
        Date sendTime = new Date();

        if (!success) {
            SysLuckyChance sysLuckyChance = new SysLuckyChance()
                    .setBuyerNick(buyerNick)
                    .setChanceFrom(chanceFrom)
                    .setGetTime(sendTime)
                    .setGetTimeString(CommonDateParseUtil.date2string(sendTime, "yyyy-MM-dd"))
                    .setTidTime(sendTime)
                    .setTid(tid)
                    .setCardType(cardType)
                    .setCardImg(cardImg)
                    .setHaveSuccess(BooleanConstant.BOOLEAN_NO)
                    .setNotificationContent("你未能获得碎片")
                    .setIsUse(BooleanConstant.BOOLEAN_YES)
                    .setHaveNotification(BooleanConstant.BOOLEAN_YES)
                    .setHaveNotification(BooleanConstant.BOOLEAN_NO);
            sysLuckyChanceRepository.save(sysLuckyChance);
            return null;
        }

        List<SysLuckyChance> collect = IntStream.range(0, number).mapToObj((i) -> {
            SysLuckyChance sysLuckyChance = new SysLuckyChance().setBuyerNick(buyerNick)
                    .setHaveSuccess(BooleanConstant.BOOLEAN_YES)
                    .setChanceFrom(chanceFrom)
                    .setGetTime(sendTime)
                    .setGetTimeString(CommonDateParseUtil.date2string(sendTime, "yyyy-MM-dd"))
                    .setTidTime(sendTime)
                    .setIsUse(BooleanConstant.BOOLEAN_NO)
                    .setTid(tid)
                    .setCardType(cardType)
                    .setHaveNotification(BooleanConstant.BOOLEAN_YES)
                    .setCardImg(cardImg)
                    .setNotificationImg("---")
                    .setNotificationContent("---");
            if (i == 0 && !LuckyChanceFromEnum.SHARE.equals(chanceFrom) && !LuckyChanceFromEnum.SHARE_FOLLOW.equals(chanceFrom) && !LuckyChanceFromEnum.SHARE_MEMBER.equals(chanceFrom)) {
                sysLuckyChance.setNotificationContent(messageContent);
                sysLuckyChance.setHaveNotification(BooleanConstant.BOOLEAN_NO);
                sysLuckyChance.setNotificationImg(cardImg);
            }
            return sysLuckyChance;
        }).collect(Collectors.toList());
        return sysLuckyChanceRepository.saveAll(collect);
    }

    @Transactional
    public List<SysLuckyChance> sendLuckyChance(String buyerNick, LuckyChanceFromEnum taskType, List<AwardUseWayEnum> cardTypes, String tid, String message) {

        List<SysSettingAward> all = findAllAward();
        Map<AwardUseWayEnum, List<SysSettingAward>> collect = all.stream().collect(Collectors.groupingBy(SysSettingAward::getUseWay));
        Date sendTime = new Date();

        List<SysLuckyChance> chances = new ArrayList<>();
        SysLuckyChance notificationChance = null;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < cardTypes.size(); i++) {
            AwardUseWayEnum awardUseWayEnum = cardTypes.get(i);
            SysLuckyChance sysLuckyChance = new SysLuckyChance().setBuyerNick(buyerNick)
                    .setHaveSuccess(BooleanConstant.BOOLEAN_YES)
                    .setChanceFrom(taskType)
                    .setGetTime(sendTime)
                    .setGetTimeString(CommonDateParseUtil.date2string(sendTime, "yyyy-MM-dd"))
                    .setTidTime(sendTime)
                    .setIsUse(BooleanConstant.BOOLEAN_NO)
                    .setTid(tid)
                    .setCardType(awardUseWayEnum)
                    .setHaveNotification(BooleanConstant.BOOLEAN_YES)
                    .setCardImg(collect.get(awardUseWayEnum).get(0).getImg())
                    .setNotificationContent("---");
            if (i == 0 && !LuckyChanceFromEnum.SHARE.equals(taskType) && !LuckyChanceFromEnum.SHARE_FOLLOW.equals(taskType) && !LuckyChanceFromEnum.SHARE_MEMBER.equals(taskType)) {
                sysLuckyChance.setNotificationContent(message);
                sysLuckyChance.setHaveNotification(BooleanConstant.BOOLEAN_NO);
                notificationChance = sysLuckyChance;
            }
            chances.add(sysLuckyChance);
            stringBuilder.append(sysLuckyChance.getCardImg()).append(",");
        }
        notificationChance.setNotificationImg(stringBuilder.toString());

        return sysLuckyChanceRepository.saveAll(chances);
    }


    /**
     * 2.剩余抽奖次数
     *
     * @param buyerNick
     * @description
     * @create by 王星齐
     * @time 2020-08-28 19:31:12
     */
    public List<SysLuckyChance> unUseLuckyChance(String buyerNick) {
        return sysLuckyChanceRepository.findByHaveSuccessAndBuyerNickAndIsUse(BooleanConstant.BOOLEAN_YES, buyerNick, BooleanConstant.BOOLEAN_NO);
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
        return sysLuckyChanceRepository.countByHaveSuccessAndBuyerNickAndChanceFromAndGetTimeString(BooleanConstant.BOOLEAN_YES,
                buyerNick, from, CommonDateParseUtil.date2string(today, "yyyy-MM-dd"));
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
        return sysLuckyChanceRepository.countByHaveSuccessAndBuyerNickAndChanceFrom(BooleanConstant.BOOLEAN_YES, buyerNick, from);
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
    public SysSettingAward luckyDraw(List<SysSettingAward> awards, List<SysLuckyChance> chances, SysCustom custom, Date drawTime) throws Exception {
        return luckyDraw(awards, chances, custom, drawTime, false);
    }


    @Transactional
    public SysSettingAward luckyDraw(List<SysSettingAward> awards, List<SysLuckyChance> chances, SysCustom custom, Date drawTime, Boolean freeFlag) throws Exception {
        //本次抽中的奖品
        SysSettingAward awardThisWin = null;
        if (!freeFlag) {
            /*消耗抽奖次数*/
            chances.forEach(x -> {
                x.setIsUse(BooleanConstant.BOOLEAN_YES);
                x.setUseTime(drawTime);
            });
            sysLuckyChanceRepository.saveAll(chances);
        }
        Date todayFirstTime = CommonDateParseUtil.getStartTimeOfDay(drawTime);
        /*整理抽奖日志*/
        SysLuckyDrawRecord drawRecord = new SysLuckyDrawRecord()
                .setIsWin(BooleanConstant.BOOLEAN_NO)
                .setIsFill(BooleanConstant.BOOLEAN_NO)
                .setDrawTime(drawTime)
                .setPlayerHeadImg(custom.getHeadImg())
                .setPlayerBuyerNick(custom.getBuyerNick())
                .setPlayerZnick(custom.getZnick());

        try {

            /*1.整理历史抽奖记录*/
            List<SysLuckyDrawRecord> historyWin = sysLuckyDrawRecordRepository.queryMybag(custom.getBuyerNick());

            Map<String, List<SysLuckyDrawRecord>> collect = historyWin.stream().collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardName));

            /*2.开始随机抽奖,模拟选出本次抽奖中的奖品*/
            for (SysSettingAward award : awards) {
                boolean todayHasGet = false; // 该奖品  今日是否获得
                Integer maxCanGet = award.getMaxCanGet();  //该奖品 活动期间最多可获得次数
                Integer maxHasGet = 0;//该奖品 活动期间已经获得的次数
                List<SysLuckyDrawRecord> sysLuckyDrawRecords = collect.get(award.getName());
                if (CollectionUtils.isNotEmpty(sysLuckyDrawRecords)) {
                    for (SysLuckyDrawRecord history : sysLuckyDrawRecords) {
                        if (history.getDrawTime().after(todayFirstTime)) {
                            todayHasGet = true;
                            break;
                        }
                    }
                    maxHasGet = sysLuckyDrawRecords.size();
                }
                //1.奖品数量不足;2.最大中奖限制；3.已抽中过本奖品
                if (award.getRemainNum() < 1 || maxHasGet >= maxCanGet || todayHasGet) {
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
            } else if (AwardTypeEnum.MEMBER_POINTS.equals(awardThisWin.getType())) {
                //发放积分
                try {
                    CrmPointChangeResponse rsp = taobaoAPIService.changePoint(custom.getBuyerNick(), Long.valueOf(awardThisWin.getPrice()));
                    if (rsp == null || !rsp.isSuccess()) {
                        awardThisWin = null;
                        drawRecord.setSendError(rsp != null ? rsp.getMsg() : "rsp is null");
                        drawRecord.setIsWin(BooleanConstant.BOOLEAN_NO);
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    awardThisWin = null;
                    drawRecord.setSendError("发放积分异常：" + e.getMessage());
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
    public void directSendCoupon(SysSettingAward award, SysCustom custom, Date sendTime) {
        Assert.isTrue(sysSettingAwardRepository.tryReduceOne(award.getId()) > 0, "奖品库存库存不足");
        /*整理抽奖日志*/
        SysLuckyDrawRecord drawRecord = new SysLuckyDrawRecord()
                .setIsWin(BooleanConstant.BOOLEAN_YES)
                .setIsFill(BooleanConstant.BOOLEAN_NO)
                .setDrawTime(sendTime)
                .setPlayerHeadImg(custom.getHeadImg())
                .setPlayerBuyerNick(custom.getBuyerNick())
                .setPlayerZnick(custom.getZnick())
                .setAwardId(award.getId())
                .setAwardImg(award.getImg())
//                .setAwardLevel(award.getAwardLevel())
                .setAwardName(award.getName())
                .setAwardType(award.getType())
                .setRemark("邀请五个好友发放优惠券");

        try {
            AlibabaBenefitSendResponse alibabaBenefitSendResponse = taobaoAPIService.sendTaobaoCoupon(custom.getOpenId(), award.getEname());
            if (alibabaBenefitSendResponse.getResultSuccess() == null || !alibabaBenefitSendResponse.getResultSuccess()) {
                //发放失败
                drawRecord.setSendError("发放优惠券失败：-->" + alibabaBenefitSendResponse.getResultCode() + ":-->" + alibabaBenefitSendResponse.getResultMsg());
                drawRecord.setIsWin(BooleanConstant.BOOLEAN_NO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //发放异常
            drawRecord.setIsWin(BooleanConstant.BOOLEAN_NO);
            drawRecord.setSendError("发放优惠券异常：" + e.getMessage());
        }
        sysLuckyDrawRecordRepository.save(drawRecord);
    }

    /**
     * 任务对应的卡片类型
     *
     * @param taskType
     * @description
     * @create by 王星齐
     * @time 2021-03-15 10:21:57
     **/
    public AwardUseWayEnum taskType2CardType(LuckyChanceFromEnum taskType) {
        AwardUseWayEnum cardType;
        switch (taskType) {
            case FREE:
//                cardType = AwardUseWayEnum.CARD_ONE;
                cardType = AwardUseWayEnum.CARD_ONE;
                break;
            case SIGN:
//                cardType = AwardUseWayEnum.CARD_TWO;
                cardType = AwardUseWayEnum.CARD_THREE;
                break;
            case MEMBER:
//                cardType = AwardUseWayEnum.CARD_THREE;
                cardType = AwardUseWayEnum.CARD_NINE;
                break;
            case FOLLOW:
//                cardType = AwardUseWayEnum.CARD_FOUR;
                cardType = AwardUseWayEnum.CARD_SIX;
                break;
            case SHARE:
//                cardType = AwardUseWayEnum.CARD_FIVE;
                cardType = AwardUseWayEnum.CARD_EIGHT;
                break;
            case TV:
                cardType = AwardUseWayEnum.CARD_TWO;
//                cardType = AwardUseWayEnum.CARD_SIX;
                break;
            case SHARE_MEMBER:
//                cardType = AwardUseWayEnum.CARD_SEVEN;
                cardType = AwardUseWayEnum.CARD_FIVE;
                break;
            case BROWSE:
//                cardType = AwardUseWayEnum.CARD_EIGHT;
                cardType = AwardUseWayEnum.CARD_FOUR;
                break;
            case SHARE_FOLLOW:
//                cardType = AwardUseWayEnum.CARD_NINE;
                cardType = AwardUseWayEnum.CARD_SEVEN;
                break;
            case ORDER:
                cardType = AwardUseWayEnum.randomType(AwardUseWayEnum.values());
                break;
            default:
                cardType = null;
                break;
        }
        Assert.isTrue(cardType != null, "无对应的卡片类型");
        return cardType;
    }




    /*  做任务抽取卡片
     * @description
     * @create by 王星齐
     * @time 2021-03-12 19:24:21
     * @param sysCustom
     **/

    public void sendCard(String buyerNick, LuckyChanceFromEnum taskType, int sendNum, String message, String tid) {
        if (sendNum <= 0) {
            return;
        }

        //订单单独处理
        if (taskType.equals(LuckyChanceFromEnum.ORDER)) {

            List<AwardUseWayEnum> cardTypes = new ArrayList<>();
            while (sendNum > 0) {
                cardTypes.add(taskType2CardType(taskType));
                sendNum--;
            }
            sendLuckyChance(buyerNick, taskType, cardTypes, tid, message);
        }


        AwardUseWayEnum cardType = taskType2CardType(taskType);
        /*本次抽奖中的奖品*/
        SysSettingAward award = sysSettingAwardRepository.findFirstByUseWay(cardType);

        boolean success = false;
        if (Math.random() < Double.parseDouble(award.getLuckyValue())) {
            success = true;
        }
        sendLuckyChance(success, buyerNick, taskType, cardType, sendNum, tid,
                award.getImg(), message);

    }


    public void sendCard(String buyerNick, LuckyChanceFromEnum taskType, int sendNum, String message) {
        sendCard(buyerNick, taskType, sendNum, message, null);
    }

    /**
     * 卡片效验
     */
    public List<SysLuckyChance> cardComposition(CardExchangeDto cardExchangeDto, String buyerNick) throws Exception {
        Assert.notNull(cardExchangeDto, "很遗憾，碎片不足");
        // 所有未使用的卡片
        List<SysLuckyChance> list = unUseLuckyChance(buyerNick);
        if (list.size() < 5) {
            Assert.isTrue(false, "卡片数量不足");
        }
        Integer frontCards = 0;
        List<SysLuckyChance> backCards = new ArrayList<>();

        Map<AwardUseWayEnum, List<SysLuckyChance>> collect = list.stream().collect(Collectors.groupingBy(SysLuckyChance::getCardType));


        if (cardExchangeDto.getCARD_ONE() > 0) {
            // 给的卡片数量
            List<SysLuckyChance> sysLuckyChances = collect.get(AwardUseWayEnum.CARD_ONE);
            Assert.isTrue(sysLuckyChances.size() >= cardExchangeDto.getCARD_ONE(), AwardUseWayEnum.CARD_ONE.getValue() + "数量不足");
            int i = 0;
            while (i < cardExchangeDto.getCARD_ONE()) {
                backCards.add(sysLuckyChances.get(i));
                i++;
            }
            frontCards += cardExchangeDto.getCARD_ONE();

        }
        if (cardExchangeDto.getCARD_TWO() > 0) {
            // 给的卡片数量
            List<SysLuckyChance> sysLuckyChances = collect.get(AwardUseWayEnum.CARD_TWO);
            Assert.isTrue(sysLuckyChances.size() >= cardExchangeDto.getCARD_TWO(), AwardUseWayEnum.CARD_ONE.getValue() + "数量不足");
            int i = 0;
            while (i < cardExchangeDto.getCARD_TWO()) {
                backCards.add(sysLuckyChances.get(i));
                i++;
            }
            frontCards += cardExchangeDto.getCARD_TWO();
        }
        if (cardExchangeDto.getCARD_THREE() > 0) {
            // 给的卡片数量
            List<SysLuckyChance> sysLuckyChances = collect.get(AwardUseWayEnum.CARD_THREE);
            Assert.isTrue(sysLuckyChances.size() >= cardExchangeDto.getCARD_THREE(), AwardUseWayEnum.CARD_THREE.getValue() + "数量不足");
            int i = 0;
            while (i < cardExchangeDto.getCARD_THREE()) {
                backCards.add(sysLuckyChances.get(i));
                i++;
            }
            frontCards += cardExchangeDto.getCARD_THREE();
        }
        if (cardExchangeDto.getCARD_FOUR() > 0) {
            // 给的卡片数量

            List<SysLuckyChance> sysLuckyChances = collect.get(AwardUseWayEnum.CARD_FOUR);
            Assert.isTrue(sysLuckyChances.size() >= cardExchangeDto.getCARD_FOUR(), AwardUseWayEnum.CARD_FOUR.getValue() + "数量不足");
            int i = 0;
            while (i < cardExchangeDto.getCARD_FOUR()) {
                backCards.add(sysLuckyChances.get(i));
                i++;
            }
            frontCards += cardExchangeDto.getCARD_FOUR();
        }
        if (cardExchangeDto.getCARD_FIVE() > 0) {
            // 给的卡片数量

            List<SysLuckyChance> sysLuckyChances = collect.get(AwardUseWayEnum.CARD_FIVE);
            Assert.isTrue(sysLuckyChances.size() >= cardExchangeDto.getCARD_FIVE(), AwardUseWayEnum.CARD_FIVE.getValue() + "数量不足");
            int i = 0;
            while (i < cardExchangeDto.getCARD_FIVE()) {
                backCards.add(sysLuckyChances.get(i));
                i++;
            }
            frontCards += cardExchangeDto.getCARD_FIVE();
        }
        if (cardExchangeDto.getCARD_SIX() > 0) {
            // 给的卡片数量

            List<SysLuckyChance> sysLuckyChances = collect.get(AwardUseWayEnum.CARD_SIX);
            Assert.isTrue(sysLuckyChances.size() >= cardExchangeDto.getCARD_SIX(), AwardUseWayEnum.CARD_SIX.getValue() + "数量不足");
            int i = 0;
            while (i < cardExchangeDto.getCARD_SIX()) {
                backCards.add(sysLuckyChances.get(i));
                i++;
            }
            frontCards += cardExchangeDto.getCARD_SIX();
        }
        if (cardExchangeDto.getCARD_SEVEN() > 0) {
            // 给的卡片数量
            List<SysLuckyChance> sysLuckyChances = collect.get(AwardUseWayEnum.CARD_SEVEN);
            Assert.isTrue(sysLuckyChances.size() >= cardExchangeDto.getCARD_SEVEN(), AwardUseWayEnum.CARD_SEVEN.getValue() + "数量不足");
            int i = 0;
            while (i < cardExchangeDto.getCARD_SEVEN()) {
                backCards.add(sysLuckyChances.get(i));
                i++;
            }
            frontCards += cardExchangeDto.getCARD_SEVEN();
        }
        if (cardExchangeDto.getCARD_EIGHT() > 0) {
            // 给的卡片数量

            List<SysLuckyChance> sysLuckyChances = collect.get(AwardUseWayEnum.CARD_EIGHT);
            Assert.isTrue(sysLuckyChances.size() >= cardExchangeDto.getCARD_EIGHT(), AwardUseWayEnum.CARD_EIGHT.getValue() + "数量不足");
            int i = 0;
            while (i < cardExchangeDto.getCARD_EIGHT()) {
                backCards.add(sysLuckyChances.get(i));
                i++;
            }
            frontCards += cardExchangeDto.getCARD_EIGHT();
        }
        if (cardExchangeDto.getCARD_NINE() > 0) {
            // 给的卡片数量

            List<SysLuckyChance> sysLuckyChances = collect.get(AwardUseWayEnum.CARD_NINE);
            Assert.isTrue(sysLuckyChances.size() >= cardExchangeDto.getCARD_NINE(), AwardUseWayEnum.CARD_NINE.getValue() + "数量不足");
            int i = 0;
            while (i < cardExchangeDto.getCARD_NINE()) {
                backCards.add(sysLuckyChances.get(i));
                i++;
            }
            frontCards += cardExchangeDto.getCARD_NINE();
        }
        if (frontCards < 5) {
            throw new Exception("卡片数量不足");
        }

        if (frontCards > 5) {
            throw new Exception("只需选择五张卡片");
        }

        return backCards;

    }


    /**
     * 卡片效验
     */
    public List<SysLuckyChance> jigsawCheck(List<SysLuckyChance> unUseLuckyChances,List<SysLuckyChance> backCards) {
        // 所有未使用的卡片
        Map<AwardUseWayEnum, List<SysLuckyChance>> collect = unUseLuckyChances.stream().collect(Collectors.groupingBy(SysLuckyChance::getCardType));
        if (collect.size() != 9) {
            return null;
        }
//        List<SysLuckyChance> backCards = new ArrayList<>();
        backCards.add(collect.get(AwardUseWayEnum.CARD_ONE).get(0));
        backCards.add(collect.get(AwardUseWayEnum.CARD_TWO).get(0));
        backCards.add(collect.get(AwardUseWayEnum.CARD_THREE).get(0));
        backCards.add(collect.get(AwardUseWayEnum.CARD_FOUR).get(0));
        backCards.add(collect.get(AwardUseWayEnum.CARD_FIVE).get(0));
        backCards.add(collect.get(AwardUseWayEnum.CARD_SIX).get(0));
        backCards.add(collect.get(AwardUseWayEnum.CARD_SEVEN).get(0));
        backCards.add(collect.get(AwardUseWayEnum.CARD_EIGHT).get(0));
        backCards.add(collect.get(AwardUseWayEnum.CARD_NINE).get(0));
//        unUseLuckyChances.removeAll(backCards);
        return backCards;
    }


    @JoinMemcache()
    public List<Map<Object, Object>> drawLog() {
        List<Map<Object, Object>> objects = new ArrayList<>();
        List<Map> maps = sysLuckyDrawRecordRepository.queryDrawLog();
        if (CollectionUtils.isNotEmpty(maps)) {
            maps.forEach(x -> {
                objects.add(new HashMap<>(x));
            });
        }
        return objects;
    }


    /**
     * 所有奖品
     *
     * @param
     * @description
     * @create by 王星齐
     * @time 2021-03-16 14:13:48
     */
    @JoinMemcache()
    public List<SysSettingAward> findAllAward() {
        return sysSettingAwardRepository.findAll();
    }

}
