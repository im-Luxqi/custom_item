package com.duomai.project.tool;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.enums.AwardType;
import com.duomai.project.product.general.enums.LuckyChanceFrom;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.starter.SysProperties;
import com.taobao.api.response.AlibabaBenefitSendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * act，customer常规操作
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
    private SysProperties sysProperties;
    @Autowired
    private SysLuckyChanceRepository sysLuckyChanceRepository;
    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;

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
    public List<SysLuckyChance> sendLuckyChance(String buyerNick, LuckyChanceFrom chanceFrom, Integer number, String tid) {
        Date sendTime = new Date();
        return sysLuckyChanceRepository.saveAll(
                IntStream.range(0, number).mapToObj((i) -> new SysLuckyChance()
                        .setBuyerNick(buyerNick)
                        .setChanceFrom(chanceFrom)
                        .setGetTime(sendTime)
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
    public SysAward luckyDraw(List<SysAward> awards, Integer maxWinGoodNum,
                              SysCustom custom, Date drawTime) throws Exception {
        SysAward awardThisWin = null;//本次抽中的奖品

        /*消耗一次抽奖次数*/
        SysLuckyChance thisChance = sysLuckyChanceRepository.findFirstByBuyerNickAndIsUse(custom.getBuyerNick(), BooleanConstant.BOOLEAN_NO);
        if (Objects.isNull(thisChance)) {
            throw new Exception("抽奖次数不足");
        }
        sysLuckyChanceRepository.save(thisChance.setIsUse(BooleanConstant.BOOLEAN_YES)
                .setUseTime(drawTime));


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
                    historySignsBuffer.append(record.getAwardLevel()).append(",");
                    if (AwardType.GOODS.equals(record.getAwardType()))//实物
                        historyGoodsHasGetAto.updateAndGet(v -> v + 1);
                });
            final String historySigns = historySignsBuffer.toString();
            Integer historyGoodsHasGet = historyGoodsHasGetAto.get();

            /*2.开始随机抽奖,模拟选出本次抽奖中的奖品*/
            for (SysAward award : awards) {
                //1.奖品数量不足;2.本活动最大实物中奖限制；3.已抽中过本奖品
                if (award.getRemainNum() < 1 ||
                        (AwardType.GOODS.equals(award.getType()) && historyGoodsHasGet >= maxWinGoodNum) ||
                        historySigns.contains(award.getAwardLevel()))
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
            //欧皇落泪  尝试扣减奖品库存，库存不够不中奖
            if (sysLuckyChanceRepository.tryReduceOne(awardThisWin.getId()) != 1) {
                return null;
            }
            //2.如果中的是优惠券，发放优惠券
            if (AwardType.COUPON.equals(awardThisWin.getType())) {
                try {
                    AlibabaBenefitSendResponse alibabaBenefitSendResponse = taobaoAPIService.sendTaobaoCoupon(custom.getOpenId(), awardThisWin.getEname(), sysProperties.getCustomConfig().getAppName(), sysProperties.getCustomConfig().getSessionkey());
                    if (alibabaBenefitSendResponse.getResultSuccess() == null || !alibabaBenefitSendResponse.getResultSuccess()) {
                        //发放失败,算未中奖
                        awardThisWin = null;
                        drawRecord.setSendError("发放失败：" + JSON.toJSONString(alibabaBenefitSendResponse));
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //发放异常,算未中奖
                    awardThisWin = null;
                    drawRecord.setSendError("发放异常：" + e.getMessage());
                    return null;
                }
            }
            drawRecord.setIsWin(BooleanConstant.BOOLEAN_YES)
                    .setAwardId(awardThisWin.getId())
                    .setAwardImg(awardThisWin.getImg())
                    .setAwardLevel(awardThisWin.getAwardLevel())
                    .setAwardName(awardThisWin.getName())
                    .setAwardType(awardThisWin.getType());
            return awardThisWin;
        } finally {
            sysLuckyDrawRecordRepository.save(drawRecord);
        }
    }

}
