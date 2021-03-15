package com.duomai.project.product.mengniuwawaji.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.entity.SysSettingAward;
import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.product.general.repository.SysSettingAwardRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class GameIndexLuckyPoolExecute implements IApiExecute {
    @Autowired
    private SysSettingAwardRepository sysSettingAwardRepository;
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
        //校验玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        //校验兑换的奖品
        /*2.*/
        //玩家手上所有的未使用的奶瓶
        List<SysLuckyDrawRecord> unUseBattles = sysLuckyDrawRecordRepository.findByPlayerBuyerNickAndAwardTypeAndIsWinAndHaveExchange(buyerNick, AwardTypeEnum.EXCHANGE,
                BooleanConstant.BOOLEAN_YES, BooleanConstant.BOOLEAN_NO);
//        Assert.isTrue(!CollectionUtils.isEmpty(unUseBattles), "奶瓶数量不足，无法兑换");


        //玩家已兑换的奖品
        List<SysLuckyDrawRecord> allExchangeAward = sysLuckyDrawRecordRepository.queryMybag(buyerNick);


        List<SysSettingAward> all = sysSettingAwardRepository.findByUseWay(AwardUseWayEnum.POOL);

        Map<String, List<SysLuckyDrawRecord>> allExchangeAwardCollect = allExchangeAward.stream()
                .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));

        Map<String, List<SysLuckyDrawRecord>> unUseBattlesCollect = unUseBattles.stream()
                .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));

        for (SysSettingAward award : all) {

            award.setAwardIsNull(Boolean.FALSE);
            award.setCanNotExchange(Boolean.FALSE);
            award.setTodayCanNotExchange(Boolean.FALSE);
            award.setHaveNotGetCondition(Boolean.FALSE);
            if (award.getRemainNum() < 1) {
                award.setAwardIsNull(true);
                continue;
            }

            //兑换5元无门槛会员券（50000张）——1-3个不同的瓶兑换
            if ("2001".equals(award.getId())) {
                //校验规则条件1.优惠券一天只能换一次2.优惠券最多能换几次
                if (!CollectionUtils.isEmpty(allExchangeAward)) {
//                    Map<String, List<SysLuckyDrawRecord>> collect = allExchangeAward.stream()
//                            .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));
                    List<SysLuckyDrawRecord> award2001 = allExchangeAwardCollect.get("2001");
                    if (!CollectionUtils.isEmpty(award2001)) {
                        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
                        Integer drawCouponNum = actBaseSettingDto.getDrawCouponNum();
//                        Assert.isTrue(award2001.size() < drawCouponNum, "优惠券最多兑换" + drawCouponNum + "次");
                        if (award2001.size() >= drawCouponNum) {
                            award.setCanNotExchange(true);
                            continue;
                        }

                        boolean todayHasWinCoupon = false;
                        for (SysLuckyDrawRecord sysLuckyDrawRecord : award2001) {
//                            if (sysLuckyDrawRecord.getExchangeTime().before(CommonDateParseUtil.getEndTimeOfDay(sysParm.getRequestStartTime()))
//                                    && sysLuckyDrawRecord.getExchangeTime().after(CommonDateParseUtil.getStartTimeOfDay(sysParm.getRequestStartTime()))) {
//                                todayHasWinCoupon = true;
//                                break;
//                            }
                        }
//                        Assert.isTrue(!todayHasWinCoupon, "优惠券一天只能换一次");
                        if (todayHasWinCoupon) {
                            award.setTodayCanNotExchange(true);
                            continue;
                        }
                    }
                }
                //校验自身奶瓶数量符不符合要求
                List<SysLuckyDrawRecord> commonBattles = new ArrayList<SysLuckyDrawRecord>();
                unUseBattles.forEach(x -> {
                    if ("1001,1002,1003,1004".contains(x.getAwardId())) {
                        commonBattles.add(x);
                    }
                });
//                Assert.isTrue(commonBattles.size() >= 3, "至少需要3个奶瓶才能兑换");
                if (commonBattles.size() < 3) {
                    award.setHaveNotGetCondition(true);
                    continue;
                }
            }

            //兑换MOS毛毛熊+纯甄MOS奶（20个）——4个不同的瓶兑换
            if ("3001".equals(award.getId())) {
                //校验规则条件,每ID限制实物中奖1次
                if (!CollectionUtils.isEmpty(allExchangeAward)) {
                    boolean canPlayFlag = true;
                    for (SysLuckyDrawRecord x : allExchangeAward) {
                        if (AwardTypeEnum.GOODS.equals(x.getAwardType())) {
                            canPlayFlag = false;
                            break;
                        }
                    }
//                    Assert.isTrue(canPlayFlag, "每ID限制实物中奖1次");
                    if (!canPlayFlag) {
                        award.setCanNotExchange(true);
                        continue;
                    }
                }
                //校验自身奶瓶数量符不符合要求
//                Map<String, List<SysLuckyDrawRecord>> collect = unUseBattles.stream()
//                        .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));


//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1001")), "1号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1002")), "2号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1003")), "3号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1004")), "4号奶瓶数量不足，无法兑换");
                if (CollectionUtils.isEmpty(unUseBattlesCollect.get("1001"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1002"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1003"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1004"))
                ) {
                    award.setHaveNotGetCondition(true);
                    continue;
                }

            }

//纯甄MOS奶+杯子（3份）——5个不同的瓶兑换
            if ("3002".equals(award.getId())) {
                //校验规则条件,每ID限制实物中奖1次
                if (!CollectionUtils.isEmpty(allExchangeAward)) {
                    boolean canPlayFlag = true;
                    for (SysLuckyDrawRecord x : allExchangeAward) {
                        if (AwardTypeEnum.GOODS.equals(x.getAwardType())) {
                            canPlayFlag = false;
                            break;
                        }
                    }
//                    Assert.isTrue(canPlayFlag, "每ID限制实物中奖1次");
                    if (!canPlayFlag) {
                        award.setCanNotExchange(true);
                        continue;
                    }
                }
                //校验自身奶瓶数量符不符合要求
//                Map<String, List<SysLuckyDrawRecord>> collect = unUseBattles.stream()
//                        .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));

//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1001")), "1号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1002")), "2号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1003")), "3号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1004")), "4号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1005")), "5号奶瓶数量不足，无法兑换");
                if (CollectionUtils.isEmpty(unUseBattlesCollect.get("1001"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1002"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1003"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1004"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1005"))
                ) {
                    award.setHaveNotGetCondition(true);
                    continue;
                }
            }
//MOS定制大礼包：MOS惊喜彩蛋+熊+杯子（1份）——6个不同的瓶兑换
            if ("3003".equals(award.getId())) {
                //校验规则条件,每ID限制实物中奖1次
                if (!CollectionUtils.isEmpty(allExchangeAward)) {
                    boolean canPlayFlag = true;
                    for (SysLuckyDrawRecord x : allExchangeAward) {
                        if (AwardTypeEnum.GOODS.equals(x.getAwardType())) {
                            canPlayFlag = false;
                            break;
                        }
                    }
//                    Assert.isTrue(canPlayFlag, "每ID限制实物中奖1次");
                    if (!canPlayFlag) {
                        award.setCanNotExchange(true);
                        continue;
                    }
                }
                //校验自身奶瓶数量符不符合要求
//                Map<String, List<SysLuckyDrawRecord>> collect = unUseBattles.stream()
//                        .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));

//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1001")), "1号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1002")), "2号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1003")), "3号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1004")), "4号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1005")), "5号奶瓶数量不足，无法兑换");
//                Assert.isTrue(!CollectionUtils.isEmpty(collect.get("1006")), "6号奶瓶数量不足，无法兑换");
                if (CollectionUtils.isEmpty(unUseBattlesCollect.get("1001"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1002"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1003"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1004"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1005"))
                        || CollectionUtils.isEmpty(unUseBattlesCollect.get("1006"))
                ) {
                    award.setHaveNotGetCondition(true);
                    continue;
                }
            }
        }

        all.forEach(x -> {
            x.setUseWay(null);
//            x.setAwardLevel(null);
//            x.setAwardLevelSign(null);
            x.setTotalNum(null);
            x.setRemainNum(null);
            x.setSendNum(null);
            x.setLuckyValue(null);
            x.setPoolLevel(null);
        });
        return YunReturnValue.ok(all, "本机奖品池");
    }
}
