package com.duomai.project.helper;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.tool.ApplicationUtils;
import com.taobao.api.response.AlibabaBenefitSendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 抽奖辅助 service
 *
 * @description
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
    private SysAwardRepository sysAwardRepository;

    /* 发放游戏机会
     * @description
     * @create by 王星齐
     * @time 2020-08-28 16:13:28
     * @param buyerNick  发给哪个玩家(必填)
     * @param chanceFrom  什么原因发的(必填)
     * @param number  发几个(必填)
     * @param tid  哪个订单(非必填)
     **/
    @Transactional
    public List<SysLuckyChance> sendLuckyChance(String buyerNick, LuckyChanceFromEnum chanceFrom, Integer number, String tid) {
        Date sendTime = new Date();
        return sysLuckyChanceRepository.saveAll(
                IntStream.range(0, number).mapToObj((i) -> new SysLuckyChance()
                        .setBuyerNick(buyerNick)
                        .setChanceFrom(chanceFrom)
                        .setGetTime(sendTime)
                        .setTidTime(sendTime)
                        .setIsUse(BooleanConstant.BOOLEAN_NO)
                        .setTid(tid)).collect(Collectors.toList()));
    }

    /* 发放游戏机会
     * @description
     * @create by 王星齐
     * @time 2020-08-28 16:13:28
     **/
    @Transactional
    public List<SysLuckyChance> sendLuckyChance(List<SysLuckyChance> sysLuckyChances) {
        return sysLuckyChanceRepository.saveAll(sysLuckyChances);
    }


    /* 剩余抽奖次数
     * @description
     * @create by 王星齐
     * @time 2020-08-28 19:31:12
     * @param buyerNick
     **/
    public long unUseLuckyChance(String buyerNick) {
        return sysLuckyChanceRepository.countByBuyerNickAndIsUse(buyerNick, BooleanConstant.BOOLEAN_NO);
    }


    /* 抽奖
     * @description
     * @create by 王星齐
     * @time 2020-08-28 17:04:19
     * @param awards  当前奖池奖品
     * @param maxWinGoodNum 玩家最大实物中奖数
     * @param custom 哪个玩家
     **/
    @Transactional
    public SysAward luckyDraw(List<SysAward> awards, SysCustom custom, Date drawTime) throws Exception {
        SysAward awardThisWin = null;//本次抽中的奖品

        /*消耗一次抽奖次数*/
        SysLuckyChance thisChance = sysLuckyChanceRepository.findFirstByBuyerNickAndIsUse(custom.getBuyerNick(), BooleanConstant.BOOLEAN_NO);
        if (Objects.isNull(thisChance)) {
            throw new Exception("抽奖次数不足");
        }
        sysLuckyChanceRepository.save(thisChance.setIsUse(BooleanConstant.BOOLEAN_YES)
                .setUseTime(drawTime));

        /*整理抽奖日志*/
        SysLuckyDrawRecord drawRecord = new SysLuckyDrawRecord()
                .setLuckyChance(thisChance.getId())
                .setIsWin(BooleanConstant.BOOLEAN_NO)
                .setIsFill(BooleanConstant.BOOLEAN_NO)
                .setDrawTime(drawTime)
                .setPlayerHeadImg(custom.getHeadImg())
                .setPlayerBuyerNick(custom.getBuyerNick())
                .setPlayerZnick(custom.getZnick());


        try {
            /*1.整理历史抽奖记录*/
            List<SysLuckyDrawRecord> historyWin = sysLuckyDrawRecordRepository.findByPlayerBuyerNickAndIsWin(custom.getBuyerNick(), BooleanConstant.BOOLEAN_YES);
            StringBuffer historySignsBuffer = new StringBuffer();
            AtomicReference<Integer> historyGoodsHasGetAto = new AtomicReference<>(0);
            if (CollectionUtils.isNotEmpty(historyWin))
                historyWin.forEach((record) -> {
                    historySignsBuffer.append(record.getAwardName()).append(",");
                    if (AwardTypeEnum.GOODS.equals(record.getAwardType()))//实物
                        historyGoodsHasGetAto.updateAndGet(v -> v + 1);
                });
            final String historySigns_awardName_hasWin = historySignsBuffer.toString();
            Integer historyGoodsHasGet = historyGoodsHasGetAto.get();


            /*2.开始随机抽奖,模拟选出本次抽奖中的奖品*/
            Integer maxWinGoodNum = this.findMaxWinGoodNum();
            for (SysAward award : awards) {
                //1.奖品数量不足;2.本活动最大实物中奖限制；3.已抽中过本奖品
                if (award.getRemainNum() < 1 ||
                        (AwardTypeEnum.GOODS.equals(award.getType()) && historyGoodsHasGet >= maxWinGoodNum) ||
                        historySigns_awardName_hasWin.contains(award.getName()))
                    continue;
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
                    .setAwardLevel(awardThisWin.getAwardLevel())
                    .setAwardName(awardThisWin.getName())
                    .setAwardType(awardThisWin.getType());

            //欧皇落泪  尝试扣减奖品库存，库存不够不中奖
            if (sysAwardRepository.tryReduceOne(awardThisWin.getId()) != 1) {
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
                        drawRecord.setSendError("发放优惠券失败：" + JSON.toJSONString(alibabaBenefitSendResponse));
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


    @Transient
    public void directSendCoupon(SysAward award, SysCustom custom, Date sendTime) {
        Assert.isTrue(sysAwardRepository.tryReduceOne(award.getId()) > 0, "奖品库存库存不足");
        /*整理抽奖日志*/
        SysLuckyDrawRecord drawRecord = new SysLuckyDrawRecord()
                .setIsWin(BooleanConstant.BOOLEAN_NO)
                .setIsFill(BooleanConstant.BOOLEAN_NO)
                .setDrawTime(sendTime)
                .setPlayerHeadImg(custom.getHeadImg())
                .setPlayerBuyerNick(custom.getBuyerNick())
                .setPlayerZnick(custom.getZnick())
                .setIsWin(BooleanConstant.BOOLEAN_NO)
                .setAwardId(award.getId())
                .setAwardImg(award.getImg())
                .setAwardLevel(award.getAwardLevel())
                .setAwardName(award.getName())
                .setAwardType(award.getType())
                .setRemark("邀请三个好友发放优惠券");

        try {
            AlibabaBenefitSendResponse alibabaBenefitSendResponse = taobaoAPIService.sendTaobaoCoupon(custom.getOpenId(), award.getEname());
            if (alibabaBenefitSendResponse.getResultSuccess() == null || !alibabaBenefitSendResponse.getResultSuccess()) {
                //发放失败
                drawRecord.setSendError("发放优惠券失败：" + JSON.toJSONString(alibabaBenefitSendResponse));
            }
        } catch (Exception e) {
            e.printStackTrace();
            //发放异常
            drawRecord.setSendError("发放优惠券异常：" + e.getMessage());
        }
        sysLuckyDrawRecordRepository.save(drawRecord);
    }

    public Integer findMaxWinGoodNum() {
        String property = ApplicationUtils.getContext().getEnvironment().getProperty("spring.profiles.active");
        if ("prod".equals(property)) {
            return 1;
        }
        return 999;
    }

    @Transient
    public List<SysAward> findCustomTimeAwardPool(SysCustom sysCustom) {
        long l = sysLuckyDrawRecordRepository.countByPlayerBuyerNickAndLuckyChanceIsNull(sysCustom.getBuyerNick());
        if (l > 0) {
            //todo:等待落实奖池升级规则
            return sysAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.POOL);
        }
        return sysAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.FIRSTLUCKY);
    }
}
