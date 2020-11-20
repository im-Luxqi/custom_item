package com.duomai.project.product.mengniuwawaji.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 首页奶瓶兑换
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @time 2020-09-29 14:52:42
 */
@Component
public class GameIndexLuckyExchangeExecute implements IApiExecute {
    @Autowired
    private SysAwardRepository sysAwardRepository;
    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;


    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {



        /*1.校验*/
        //校验活动时间
        projectHelper.actTimeValidate();
        //校验玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        Assert.isTrue(BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveAuthorization()), "请先授权");
        //校验兑换的奖品
        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        String exchange_award_id = jsonObjectAdmjson.getString("exchange_award_id");
        Assert.hasLength(exchange_award_id, "exchange_award_id不能为空");
        Optional<SysAward> awards = sysAwardRepository.findById(exchange_award_id);
        Assert.isTrue(awards.isPresent(), "无效的奖品Id");
        SysAward award = awards.get();
        Assert.isTrue(AwardUseWayEnum.POOL.equals(award.getUseWay()), "无效的奖品");
        Assert.isTrue(award.getRemainNum() > 0, award.getName() + "数量不足,无法兑换");




        /*2.*/
        //玩家手上所有的未使用的奶瓶
        List<SysLuckyDrawRecord> unUseBattles = sysLuckyDrawRecordRepository.findByPlayerBuyerNickAndAwardTypeAndIsWinAndHaveExchange(buyerNick, AwardTypeEnum.EXCHANGE,
                BooleanConstant.BOOLEAN_YES, BooleanConstant.BOOLEAN_NO);
        Assert.isTrue(!CollectionUtils.isEmpty(unUseBattles), "奶瓶数量不足，无法兑换");


        //玩家已兑换的奖品
        List<SysLuckyDrawRecord> allExchangeAward = sysLuckyDrawRecordRepository.queryMybag(buyerNick);


        //
        SysLuckyDrawRecord thisExchangeRecord = null;
        switch (award.getId()) {
            //兑换5元无门槛会员券（50000张）——1-3个不同的瓶兑换
            case "2001": {

                //校验规则条件1.优惠券一天只能换一次2.优惠券最多能换几次
                if (!CollectionUtils.isEmpty(allExchangeAward)) {
                    Map<String, List<SysLuckyDrawRecord>> collect = allExchangeAward.stream()
                            .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));
                    List<SysLuckyDrawRecord> award2001 = collect.get("2001");
                    if (!CollectionUtils.isEmpty(award2001)) {
                        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
                        Integer drawCouponNum = actBaseSettingDto.getDrawCouponNum();
                        Assert.isTrue(award2001.size() < drawCouponNum, "优惠券最多兑换" + drawCouponNum + "次");

                        boolean todayHasWinCoupon = false;
                        for (SysLuckyDrawRecord sysLuckyDrawRecord : award2001) {
                            if (sysLuckyDrawRecord.getExchangeTime().before(CommonDateParseUtil.getEndTimeOfDay(sysParm.getRequestStartTime()))
                                    && sysLuckyDrawRecord.getExchangeTime().after(CommonDateParseUtil.getStartTimeOfDay(sysParm.getRequestStartTime()))) {
                                todayHasWinCoupon = true;
                                break;
                            }
                        }
                        Assert.isTrue(!todayHasWinCoupon, "优惠券一天只能换一次");
                    }
                }

                //校验自身奶瓶数量符不符合要求
                List<SysLuckyDrawRecord> commonBattles = new ArrayList<SysLuckyDrawRecord>();
                unUseBattles.forEach(x -> {
                    if ("1001,1002,1003,1004".contains(x.getAwardId())) {
                        commonBattles.add(x);
                    }
                });
                Assert.isTrue(commonBattles.size() >= 3, "至少需要3个奶瓶才能兑换");

                //用最合理的算法消耗已拥有的奶瓶（优先消耗重复的，低价值的奶瓶）
                List<SysLuckyDrawRecord> shouldCost = new ArrayList<SysLuckyDrawRecord>();
                Map<String, List<SysLuckyDrawRecord>> collect = commonBattles.stream()
                        .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));
                Map<String, List<SysLuckyDrawRecord>> sortMap = new TreeMap<>(String::compareTo);
                sortMap.putAll(collect);
                boolean mulValue;
                while (shouldCost.size() < 3) {
                    mulValue = false;
                    for (String key : sortMap.keySet()) {
                        if (sortMap.get(key).size() > 1) {
                            mulValue = true;
                        }
                    }
                    for (String key : sortMap.keySet()) {
                        if (shouldCost.size() >= 3) {
                            break;
                        }
                        List<SysLuckyDrawRecord> sysLuckyDrawRecords = sortMap.get(key);
                        if (mulValue && sysLuckyDrawRecords.size() > 1) {
                            shouldCost.add(sysLuckyDrawRecords.get(0));
                            sysLuckyDrawRecords.remove(0);
                        } else if (!mulValue) {
                            shouldCost.add(sysLuckyDrawRecords.get(0));
                            sysLuckyDrawRecords.remove(0);
                        }
                    }
                }

                //奶瓶兑换奖品
                thisExchangeRecord = luckyDrawHelper.directExchangeAward(shouldCost, syscustom, award, sysParm.getRequestStartTime());
                break;
            }
            //兑换MOS毛毛熊+纯甄MOS奶（20个）——4个不同的瓶兑换
            case "3001": {
                //校验规则条件,每ID限制实物中奖1次
                if (!CollectionUtils.isEmpty(allExchangeAward)) {
                    boolean canPlayFlag = true;
                    for (SysLuckyDrawRecord x : allExchangeAward) {
                        if (AwardTypeEnum.GOODS.equals(x.getAwardType())) {
                            canPlayFlag = false;
                            break;
                        }
                    }
                    Assert.isTrue(canPlayFlag, "每ID限制实物中奖1次");
                }
                //校验自身奶瓶数量符不符合要求
                Map<String, List<SysLuckyDrawRecord>> collect = unUseBattles.stream()
                        .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));

                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1001")), "1号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1002")), "2号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1003")), "3号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1004")), "4号奶瓶数量不足，无法兑换");

                List<SysLuckyDrawRecord> shouldCost = new ArrayList<SysLuckyDrawRecord>();
                shouldCost.add(collect.get("1001").get(0));
                shouldCost.add(collect.get("1002").get(0));
                shouldCost.add(collect.get("1003").get(0));
                shouldCost.add(collect.get("1004").get(0));

                //奶瓶兑换奖品
                thisExchangeRecord = luckyDrawHelper.directExchangeAward(shouldCost, syscustom, award, sysParm.getRequestStartTime());
                break;
            }
            //纯甄MOS奶+杯子（3份）——5个不同的瓶兑换
            case "3002": {
                //校验规则条件,每ID限制实物中奖1次
                if (!CollectionUtils.isEmpty(allExchangeAward)) {
                    boolean canPlayFlag = true;
                    for (SysLuckyDrawRecord x : allExchangeAward) {
                        if (AwardTypeEnum.GOODS.equals(x.getAwardType())) {
                            canPlayFlag = false;
                            break;
                        }
                    }
                    Assert.isTrue(canPlayFlag, "每ID限制实物中奖1次");
                }
                //校验自身奶瓶数量符不符合要求
                Map<String, List<SysLuckyDrawRecord>> collect = unUseBattles.stream()
                        .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));

                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1001")), "1号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1002")), "2号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1003")), "3号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1004")), "4号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1005")), "5号奶瓶数量不足，无法兑换");

                List<SysLuckyDrawRecord> shouldCost = new ArrayList<SysLuckyDrawRecord>();
                shouldCost.add(collect.get("1001").get(0));
                shouldCost.add(collect.get("1002").get(0));
                shouldCost.add(collect.get("1003").get(0));
                shouldCost.add(collect.get("1004").get(0));
                shouldCost.add(collect.get("1005").get(0));

                //奶瓶兑换奖品
                thisExchangeRecord = luckyDrawHelper.directExchangeAward(shouldCost, syscustom, award, sysParm.getRequestStartTime());
                break;
            }
            //MOS定制大礼包：MOS惊喜彩蛋+熊+杯子（1份）——6个不同的瓶兑换
            case "3003": {
                //校验规则条件,每ID限制实物中奖1次
                if (!CollectionUtils.isEmpty(allExchangeAward)) {
                    boolean canPlayFlag = true;
                    for (SysLuckyDrawRecord x : allExchangeAward) {
                        if (AwardTypeEnum.GOODS.equals(x.getAwardType())) {
                            canPlayFlag = false;
                            break;
                        }
                    }
                    Assert.isTrue(canPlayFlag, "每ID限制实物中奖1次");
                }
                //校验自身奶瓶数量符不符合要求
                Map<String, List<SysLuckyDrawRecord>> collect = unUseBattles.stream()
                        .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));

                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1001")), "1号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1002")), "2号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1003")), "3号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1004")), "4号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1005")), "5号奶瓶数量不足，无法兑换");
                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1006")), "6号奶瓶数量不足，无法兑换");

                List<SysLuckyDrawRecord> shouldCost = new ArrayList<SysLuckyDrawRecord>();
                shouldCost.add(collect.get("1001").get(0));
                shouldCost.add(collect.get("1002").get(0));
                shouldCost.add(collect.get("1003").get(0));
                shouldCost.add(collect.get("1004").get(0));
                shouldCost.add(collect.get("1005").get(0));
                shouldCost.add(collect.get("1006").get(0));

                //奶瓶兑换奖品
                thisExchangeRecord = luckyDrawHelper.directExchangeAward(shouldCost, syscustom, award, sysParm.getRequestStartTime());
                break;
            }
            default:
                break;
        }


        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();

        boolean isWin = BooleanConstant.BOOLEAN_YES.equals(thisExchangeRecord.getIsWin());
        resultMap.put("isWin",isWin);
        resultMap.put("award",award);
        resultMap.put("errorMsg",thisExchangeRecord.getSendError());
        return YunReturnValue.ok(resultMap, "奶瓶兑换操作");
    }
}
