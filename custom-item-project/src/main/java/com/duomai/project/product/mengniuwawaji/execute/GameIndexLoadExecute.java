package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.entity.SysPagePvLog;
import com.duomai.project.product.general.entity.SysSettingAward;
import com.duomai.project.product.general.enums.*;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.ProjectTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 游戏首页 加载
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameIndexLoadExecute implements IApiExecute {

    @Resource
    private ITaobaoAPIService taobaoAPIService;
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysTaskInviteLogRepository sysTaskInviteLogRepository;

    @Autowired
    private SysSettingAwardRepository sysSettingAwardRepository;

    @Autowired
    private SysTaskShareLogRepository sysTaskShareLogRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;


    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;


    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Autowired
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        String inviter = jsonObjectAdmjson.getString("inviter");
        InviteTypeEnum inviteType = ProjectTools.enumValueOf(InviteTypeEnum.class, jsonObjectAdmjson.getString("inviteType"));
        if (StringUtils.isNotBlank(inviter)) {
            Assert.notNull(inviteType, "邀请类型不能为空");
        }

        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");



        /*保存pv*/
        sysPagePvLogRepository.save(new SysPagePvLog()
                .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                .setCreateTime(sysParm.getRequestStartTime())
                .setId(sysParm.getApiParameter().getCommomParameter().getIp())
                .setPage(PvPageEnum.PAGE_INDEX));


        boolean actLive = projectHelper.actTimeValidateFlag();
        if (actLive) {
 /*           //2.被邀请助力或者被邀请入会
            JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
            Boolean joinMemberBack = jsonObjectAdmjson.getBoolean("joinMemberBack");
            String sharer = jsonObjectAdmjson.getString("sharer");
            String inviter = jsonObjectAdmjson.getString("inviter");
            Assert.isTrue(!(StringUtils.isNotBlank(sharer) && StringUtils.isNotBlank(inviter)), "非法链接");

            String successPic = "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/modal/zhulisuccess.png";
            String errorPic = "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/modal/failimg2.png";
            //分享助力
            if (StringUtils.isNotBlank(sharer)) {
                sharer = sharer.replaceAll(" ", "+");
                if (sharer.equals(buyerNick)) {
                    resultMap.put("alter_for_shared_flag", true);
                    resultMap.put("alter_for_shared_msg", "无法帮自己助力");
                    resultMap.put("alter_for_shared_pic", errorPic);
                } else {
                    //同一个人每天只能为他人助力一次
                    SysCustom sharerCustom = sysCustomRepository.findByBuyerNick(sharer);
                    Assert.notNull(sharerCustom, "无效的分享者");

                    String todayString = CommonDateParseUtil.date2string(sysParm.getRequestStartTime(), "yyyy-MM-dd");
                    SysTaskShareLog sysInviteLog = new SysTaskShareLog().setCreateTime(sysParm.getRequestStartTime())
                            .setHaveSuccess(BooleanConstant.BOOLEAN_YES)
                            .setShareTime(todayString)
                            .setMixShareder(syscustom.getBuyerNick())
                            .setShareder(syscustom.getZnick())
                            .setSharederImg(syscustom.getHeadImg())
                            .setMixSharer(sharerCustom.getBuyerNick())
                            .setSharer(sharerCustom.getZnick())
                            .setSharerImg(sharerCustom.getHeadImg());

                    long today_has_help = sysTaskShareLogRepository.countByMixSharederAndHaveSuccessAndShareTime(syscustom.getBuyerNick(), BooleanConstant.BOOLEAN_YES,
                            todayString);
                    if (today_has_help > 0) {
                        resultMap.put("alter_for_shared_flag", true);
                        resultMap.put("alter_for_shared_msg", "您今日已经为好友助力过，无法再为好友助力");
                        resultMap.put("alter_for_shared_pic", errorPic);
                        sysInviteLog.setHaveSuccess(BooleanConstant.BOOLEAN_NO);
                    }
                    sysTaskShareLogRepository.save(sysInviteLog);
                    if (BooleanConstant.BOOLEAN_YES.equals(sysInviteLog.getHaveSuccess())) {
//                        luckyDrawHelper.sendLuckyChance(sharerCustom.getBuyerNick(), LuckyChanceFromEnum.SHARE, 1,
//                                "分享" + syscustom.getZnick(), "分享任务，获得了游戏机会");
                        resultMap.put("alter_for_shared_flag", true);
                        resultMap.put("alter_for_shared_msg", "恭喜你，助力成功");
                        resultMap.put("alter_for_shared_pic", successPic);
                    }
                }
            }

            //邀请入会
            if (StringUtils.isNotBlank(inviter)) {
                inviter = inviter.replaceAll(" ", "+");
                SysCustom inviterCustom = sysCustomRepository.findByBuyerNick(inviter);
                Assert.notNull(inviterCustom, "无效的邀请者");


                String todayString = CommonDateParseUtil.date2string(sysParm.getRequestStartTime(), "yyyy-MM-dd");
                SysTaskInviteLog sysTaskInviteLog = new SysTaskInviteLog().setCreateTime(sysParm.getRequestStartTime())
                        .setMixInvitee(syscustom.getBuyerNick())
                        .setInviteTime(todayString)
                        .setInvitee(syscustom.getZnick())
                        .setInviteeImg(syscustom.getHeadImg())
                        .setMixInviter(inviterCustom.getBuyerNick())
                        .setInviter(inviterCustom.getZnick())
                        .setInviterImg(inviterCustom.getHeadImg());

                //邀请非回调进页面
                if (joinMemberBack == null || !joinMemberBack) {
                    resultMap.put("alter_for_invitee_flag", true);
                    resultMap.put("alter_for_invitee_msg", "未入会");
                    resultMap.put("alter_for_invitee_pic", errorPic);
                    resultMap.put("alter_for_inviter_img", inviterCustom.getHeadImg());
                    if (inviter.equals(buyerNick)) {
                        resultMap.put("alter_for_invitee_msg", "无法帮自己助力");
                        sysTaskInviteLog.setHaveSuccess(BooleanConstant.BOOLEAN_NO);
                    } else {
                        if (!MemberWayFromEnum.NON_MEMBER.equals(syscustom.getMemberWayFrom())
                                || taobaoAPIService.isMember(buyerNick)) {
                            resultMap.put("alter_for_invitee_msg", "您不是店铺新会员，无法为好友助力");
                            sysTaskInviteLog.setHaveSuccess(BooleanConstant.BOOLEAN_NO);
                        }
                    }
                } else {
                    sysTaskInviteLog.setHaveSuccess(BooleanConstant.BOOLEAN_YES);
                    if (inviter.equals(buyerNick) ||
                            !taobaoAPIService.isMember(buyerNick) ||
                            BooleanConstant.BOOLEAN_NO.equals(syscustom.getHaveAuthorization()) ||
                            !MemberWayFromEnum.NON_MEMBER.equals(syscustom.getMemberWayFrom())) {
                        sysTaskInviteLog.setHaveSuccess(BooleanConstant.BOOLEAN_NO);
                    }
                    //邀请成功,发放抽奖机会
                    if (BooleanConstant.BOOLEAN_YES.equals(sysTaskInviteLog.getHaveSuccess())) {
                        sysCustomRepository.save(syscustom.setMemberWayFrom(MemberWayFromEnum.INVITEE_JOIN_MEMBER));
//                        luckyDrawHelper.sendLuckyChance(inviterCustom.getBuyerNick(), LuckyChanceFromEnum.FOLLOW, 1,
//                                "邀请入会" + syscustom.getZnick(), "邀请任务，获得了游戏机会");
                        resultMap.put("alter_for_invitee_flag", true);
                        resultMap.put("alter_for_invitee_msg", "恭喜你，助力成功");
                        resultMap.put("alter_for_invitee_pic", successPic);
                        resultMap.put("alter_for_inviter_img", inviterCustom.getHeadImg());
                    }
                }
                //记录邀请日志
                if (sysTaskInviteLog.getHaveSuccess() != null) {
                    sysTaskInviteLogRepository.save(sysTaskInviteLog);
                }
            }
*/


            if (StringUtils.isNotBlank(inviter)) {
                SysCustom inviterCustom = sysCustomRepository.findByBuyerNick(inviter);
                resultMap.put("alter_for_inviter_name", inviterCustom.getZnick());
                resultMap.put("alter_for_inviter_img", inviterCustom.getHeadImg());
                resultMap.put("alter_for_invitee_flag", true);
                resultMap.put("alter_for_invitee_type", inviteType.getValue());
                resultMap.put("alter_for_invitee_msg", "我正在参加蒙牛的收集拼图活动，需要1位好友助力，请帮我助力赢大奖~");
            }


            //首次登录游戏免费送一次
            long l = luckyDrawHelper.countLuckyChanceFrom(buyerNick, LuckyChanceFromEnum.FREE);
            if (l == 0) {
                int getNum = 1;
                luckyDrawHelper.sendCard(buyerNick, LuckyChanceFromEnum.FREE, getNum,
                        "首次登录，获得【有料品鉴官】一博送你的食力拼图*" + getNum);
            }
        }


        //1.活动规则
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        actBaseSettingDto.setDrawCouponNum(null);
        actBaseSettingDto.setTaskBrowseShouldSee(null);
        resultMap.put("game_rule", actBaseSettingDto);

        //获得奖品并分类
        List<SysSettingAward> all = luckyDrawHelper.findAllAward();
        for (SysSettingAward award : all) {
            award.setId(null);
            award.setEname(null);
            award.setLuckyValue(null);
            award.setDescription(null);
            award.setPoolLevel(null);
            award.setRemainNum(null);
            award.setSendNum(null);
            award.setTotalNum(null);
            award.setHaveGetNum(null);
        }
        List<SysSettingAward> exchangeAward = new ArrayList<>();
        List<SysSettingAward> otherAward = new ArrayList<>();
        all.forEach(sysSettingAward -> {
            if (AwardTypeEnum.EXCHANGE.equals(sysSettingAward.getType())) {
                exchangeAward.add(sysSettingAward);
            } else {
                otherAward.add(sysSettingAward);
            }
        });
        resultMap.put("award_show", otherAward);
        //获得未使用的所有卡牌
        List<SysLuckyChance> sysLuckyChances = luckyDrawHelper.unUseLuckyChance(buyerNick);
        Map<AwardUseWayEnum, List<SysLuckyChance>> allCards = sysLuckyChances.stream().collect(Collectors.groupingBy(SysLuckyChance::getCardType));
        for (SysSettingAward award : exchangeAward) {
            award.setHaveGetNum(0);
            List<SysLuckyChance> cards = allCards.get(award.getUseWay());
            if (!CollectionUtils.isEmpty(cards)) {
                award.setHaveGetNum(cards.size());
            }
        }
        LinkedHashMap<AwardUseWayEnum, SysSettingAward> unUseCard = new LinkedHashMap<>();
        exchangeAward.stream().sorted(Comparator.comparing(SysSettingAward::getUseWay)).forEach(x -> {
            unUseCard.put(x.getUseWay(), x);
        });
        resultMap.put("card_show", unUseCard);


        return YunReturnValue.ok(resultMap, "游戏首页");
    }
}




