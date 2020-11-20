package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysInviteLog;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.entity.SysShareLog;
import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysInviteLogRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.product.general.repository.SysShareLogRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 游戏首页 加载
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
public class GameIndexLoadExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;


    @Autowired
    private SysInviteLogRepository sysInviteLogRepository;

    @Autowired
    private SysShareLogRepository sysShareLogRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;


    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Autowired
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("alter_for_shared_flag", false);
        resultMap.put("alter_for_invitee_flag", false);


        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");


        boolean actLive = projectHelper.actTimeValidateFlag();
        if (actLive) {
            /*2.被邀请助力或者被邀请入会*/
            JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
            String sharer = jsonObjectAdmjson.getString("sharer");
            String inviter = jsonObjectAdmjson.getString("inviter");
            //分享助力
            if (StringUtils.isNotBlank(sharer) && !sharer.equals(buyerNick)) {
                //同一个人每天只能为他人助力一次
                SysCustom sharerCustom = sysCustomRepository.findByBuyerNick(sharer);
                Assert.notNull(sharerCustom, "无效的分享者");

                SysShareLog sysInviteLog = new SysShareLog().setCreateTime(sysParm.getRequestStartTime())
                        .setHaveSuccess(BooleanConstant.BOOLEAN_YES)
                        .setMixShareder(syscustom.getBuyerNick())
                        .setShareder(syscustom.getZnick())
                        .setSharederImg(syscustom.getHeadImg())
                        .setMixSharer(sharerCustom.getBuyerNick())
                        .setSharer(sharerCustom.getZnick())
                        .setSharerImg(sharerCustom.getHeadImg());
                long today_has_help = sysShareLogRepository.countByMixSharederAndCreateTimeBetween(syscustom.getBuyerNick(),
                        CommonDateParseUtil.getStartTimeOfDay(sysParm.getRequestStartTime()), CommonDateParseUtil.getEndTimeOfDay(sysParm.getRequestStartTime()));
                if (today_has_help > 0) {
                    resultMap.put("alter_for_shared_flag", true);
                    resultMap.put("alter_for_shared_msg", "您今日已经为好友助力过，无法再为好友助力");
                    sysInviteLog.setHaveSuccess(BooleanConstant.BOOLEAN_NO);
                }

                sysShareLogRepository.save(sysInviteLog);
                if (BooleanConstant.BOOLEAN_YES.equals(sysInviteLog.getHaveSuccess())) {
                    luckyDrawHelper.sendLuckyChance(sharerCustom.getBuyerNick(), LuckyChanceFromEnum.SHARE, 1,
                            "分享" + syscustom.getZnick(), "任务完成,获取" + 1 + "次机会");
                }
                resultMap.put("alter_for_shared_flag", true);
                resultMap.put("alter_for_shared_msg", "恭喜你，助力成功");
            }
            //邀请入会
            if (StringUtils.isNotBlank(inviter) && !inviter.equals(buyerNick)) {
                SysCustom inviterCustom = sysCustomRepository.findByBuyerNick(inviter);
                Assert.notNull(inviterCustom, "无效的邀请者");

                SysInviteLog sysInviteLog = new SysInviteLog().setCreateTime(sysParm.getRequestStartTime())
                        .setHaveSuccess(BooleanConstant.BOOLEAN_YES)
                        .setMixInvitee(syscustom.getBuyerNick())
                        .setInvitee(syscustom.getZnick())
                        .setInviteeImg(syscustom.getHeadImg())
                        .setMixInviter(inviterCustom.getBuyerNick())
                        .setInviter(inviterCustom.getZnick())
                        .setInviterImg(inviterCustom.getHeadImg());
                //您已是店铺会员，无法为好友助力
                if (!MemberWayFromEnum.NON_MEMBER.equals(syscustom.getMemberWayFrom())) {
                    resultMap.put("alter_for_invitee_flag", true);
                    resultMap.put("alter_for_invitee_msg", "您已是店铺会员，无法为好友助力");
                    sysInviteLog.setHaveSuccess(BooleanConstant.BOOLEAN_NO);
                }
                //记录邀请日志，成功发放抽奖机会
                sysInviteLogRepository.save(sysInviteLog);
                if (BooleanConstant.BOOLEAN_YES.equals(sysInviteLog.getHaveSuccess())) {
                    sysCustomRepository.save(syscustom.setMemberWayFrom(MemberWayFromEnum.INVITEE_JOIN_MEMBER));
                    luckyDrawHelper.sendLuckyChance(inviterCustom.getBuyerNick(), LuckyChanceFromEnum.INVITE_MEMBER, 1,
                            "邀请入会" + syscustom.getZnick(), "任务完成,获取" + 1 + "次机会");
                }

                resultMap.put("alter_for_invitee_flag", true);
                resultMap.put("alter_for_invitee_msg", "恭喜你，助力成功");
            }


            //首次登陆游戏免费送一次
            long l = luckyDrawHelper.countTodayLuckyChanceFrom(buyerNick, LuckyChanceFromEnum.FREE);
            if (l == 0) {
                luckyDrawHelper.sendLuckyChance(buyerNick, LuckyChanceFromEnum.FREE, 1,
                        "首次登陆", "首次登陆,获取" + 1 + "次机会");
            }
        }


        //1.活动规则
        resultMap.put("game_rule", projectHelper.actBaseSettingFind());
        //2.抓娃娃机会次数
        resultMap.put("lucky_chance_num", luckyDrawHelper.unUseLuckyChance(buyerNick));
        List<SysLuckyDrawRecord> unUseBattles = sysLuckyDrawRecordRepository.findByPlayerBuyerNickAndAwardTypeAndIsWinAndHaveExchange(buyerNick, AwardTypeEnum.EXCHANGE,
                BooleanConstant.BOOLEAN_YES, BooleanConstant.BOOLEAN_NO);
        unUseBattles.forEach((x) -> {
            x.setId(null);
            x.setLuckyChance(null);
            x.setDrawTime(null);
            x.setPlayerHeadImg(null);
            x.setPlayerBuyerNick(null);
            x.setPlayerZnick(null);
            x.setAwardId(null);
            x.setAwardLevel(null);
            x.setAwardType(null);
            x.setIsWin(null);
            x.setIsFill(null);
            x.setHaveExchange(null);
        });
        //3.我的战利品
        resultMap.put("lucky_win_bottle", unUseBattles);
        //4.兑换弹幕
        resultMap.put("lucky_exchange_barrage", sysLuckyDrawRecordRepository.queryExchangeLog());

        return YunReturnValue.ok(resultMap, "游戏首页");
    }
}




