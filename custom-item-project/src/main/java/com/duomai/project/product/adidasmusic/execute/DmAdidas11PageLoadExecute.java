package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.FinishTheTaskHelper;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.adidasmusic.util.CommonHanZiUtil;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.*;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.enums.CommonExceptionEnum;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.CommonDateParseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author cjw
 * @description 阿迪双十一小程序二楼 活动load
 * @date 2020-10-02
 */
@Service
public class DmAdidas11PageLoadExecute implements IApiExecute {

    @Resource
    private ProjectHelper projectHelper;
    @Resource
    private LuckyDrawHelper drawHelper;
    @Resource
    private FinishTheTaskHelper taskHelper;
    @Resource
    private SysCustomRepository customRepository;
    @Resource
    private SysInviteLogRepository inviteLogRepository;
    @Resource
    private SysLuckyDrawRecordRepository drawRecordRepository;
    @Resource
    private SysLuckyChanceRepository luckyChanceRepository;
    @Resource
    private SysAwardRepository awardRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request
            , HttpServletResponse response) throws Exception {

        //校验活动是否在活动时间内
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate(actBaseSettingDto);

        //取参
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        Date date = sysParm.getRequestStartTime();
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Assert.hasLength(buyerNick, CommonExceptionEnum.BUYER_NICK_ERROR.getMsg());
        //被邀请人昵称
        String inviteeNick = object.getString("inviteeNick");

        //预防并发
        projectHelper.checkoutMultipleCommit(sysParm, this::ApiExecute);

        //获取粉丝信息
        SysCustom sysCustom = customRepository.findByBuyerNick(buyerNick);
        //为空初始化粉丝数据
        if (sysCustom == null) {
            sysCustom = projectHelper.customInit(sysParm);
            customRepository.save(sysCustom);

            //todo 是否送抽奖次数 每天抽奖次数是否刷新
            SysLuckyChance luckyChance = new SysLuckyChance();
            luckyChanceRepository.save(luckyChance.setIsUse(BooleanConstant.BOOLEAN_NO)
                    .setChanceFrom(LuckyChanceFromEnum.FIRST)
                    .setBuyerNick(buyerNick)
                    .setGetTime(CommonDateParseUtil.date2date(date, CommonDateParseUtil.YYYY_MM_DD))
            );

        } else {
            //查询当天是否送过抽奖机会
            SysLuckyChance luckyChance = new SysLuckyChance();
            List<SysLuckyChance> luckyChances = luckyChanceRepository.findAll(Example.of(luckyChance.setBuyerNick(buyerNick)
                    .setGetTime(CommonDateParseUtil.date2date(date, CommonDateParseUtil.YYYY_MM_DD))
                    .setChanceFrom(LuckyChanceFromEnum.FIRST)
            ));

            //为空送一次抽奖机会
            if (luckyChances.isEmpty()) {
                //获取未使用的机会
                List<SysLuckyChance> ontUses = luckyChanceRepository.findAll(Example.of(luckyChance.setGetTime(null)
                        .setIsUse(BooleanConstant.BOOLEAN_NO)
                ));
                //抹除之前首次未使用的机会
                ontUses.stream().forEach(o-> o.setIsUse(BooleanConstant.BOOLEAN_YES));
                luckyChanceRepository.saveAll(ontUses);

                //保存今天首次抽奖机会
                luckyChanceRepository.save(luckyChance.setIsUse(BooleanConstant.BOOLEAN_NO)
                        .setGetTime(CommonDateParseUtil.date2date(date, CommonDateParseUtil.YYYY_MM_DD))
                );
            }

        }

        //如果邀请人昵称不为空
        if (StringUtils.isNotBlank(inviteeNick)) {

            if(inviteeNick.equals(buyerNick)){
                return YunReturnValue.fail("亲、自己无法邀请自己哦!");
            }

            //查询该粉丝是否被人邀请过
            long inviteLogNum = inviteLogRepository.countByInviter(buyerNick);
            if (inviteLogNum == 0) {//为空记录邀请日志
                SysInviteLog inviteLog = new SysInviteLog();
                inviteLogRepository.save(inviteLog.setCreateTime(date)
                        .setInvitee(buyerNick)
                        .setInviter(inviteeNick)
                );
            } else {
                return YunReturnValue.fail(CommonExceptionEnum.HELPED_INVITEE_ERROR.getMsg());
            }

        }
        //查询出当前粉丝的邀请记录
        List<SysInviteLog> inviteLogs;
        SysInviteLog log = new SysInviteLog();
        inviteLogs = inviteLogRepository.findAll(Example.of(log.setInviter(buyerNick)));

        //获取邀请奖品信息
        SysAward awardInvite = awardRepository.findFirstByUseWay(AwardUseWayEnum.INVITE);
        //获取该粉丝获取日志
        SysLuckyDrawRecord drawRecord = new SysLuckyDrawRecord();
        List<SysLuckyDrawRecord> sendLog = drawRecordRepository.findAll(Example.of(drawRecord.setPlayerBuyerNick(buyerNick)
                .setAwardId(awardInvite.getId())
        ));
        if(!sendLog.isEmpty()){
            awardInvite.setLogId(sendLog.get(0).getId());
        }

        //返回参数
        LinkedHashMap linkedHashMap = new LinkedHashMap();

        //活动基本信息
        linkedHashMap.put("actBaseSettingDto", actBaseSettingDto);
        //粉丝信息
        linkedHashMap.put("sysCustom", sysCustom);
        //邀请日志记录
        linkedHashMap.put("inviteLogs", inviteLogs);
        //邀请奖品信息
        linkedHashMap.put("awardInvite", awardInvite);

        //获取目前剩余抽奖次数
        long drawNum = drawHelper.unUseLuckyChance(buyerNick);
        linkedHashMap.put("drawNum", drawNum);

        //获取目前签到次数
        long signNum = taskHelper.getFinishTheTaskNum(buyerNick);
        linkedHashMap.put("signNum", signNum);

        //中奖弹幕 展示50条
        List<Map> luckyDrawRecords = drawRecordRepository.queryLuckyDrawLog();

        //如果没有50条数据，造假数据填补
        if (luckyDrawRecords.size() < 50) {
            for (int i = 0; i < 50 - luckyDrawRecords.size(); i++) {
                Map map = new HashMap();
                map.put("awardName", "满减优惠券");//奖品默认名称
                map.put("playerBuyerNick", CommonHanZiUtil.randomGetUnicodeHanZi() + "***");//粉丝昵称
                luckyDrawRecords.add(map);
            }
        }
        linkedHashMap.put("luckyDrawRecords", luckyDrawRecords);

        return YunReturnValue.ok(linkedHashMap, CommonExceptionEnum.OPERATION_SUCCESS.getMsg());
    }
}
