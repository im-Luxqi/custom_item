package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGameBoardDaily;
import com.duomai.project.product.general.entity.SysTaskShareLog;
import com.duomai.project.product.general.enums.CoachConstant;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 分享
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameShareExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private SysGameLogRepository sysGameLogRepository;
    @Autowired
    private SysGameBoardDailyRepository sysGameBoardDailyRepository;
    @Autowired
    private SysTaskShareLogRepository sysTaskShareLogRepository;


    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        projectHelper.actTimeValidate();


        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        String sharePlace = jsonObjectAdmjson.getString("sharePlace");
        org.springframework.util.Assert.hasLength(sharePlace, "分享地点sharePlace不能为空，" +
                "可选值[share_bear,share_lamp,share_other]");
        org.springframework.util.Assert.isTrue("share_bear".equals(sharePlace) ||
                "share_lamp".equals(sharePlace) ||
                "share_other".equals(sharePlace), "无效的奖品位置");


        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Date requestStartTime = sysParm.getRequestStartTime();
        String requestStartTimeString = CommonDateParseUtil.date2string(requestStartTime, "yyyy-MM-dd");
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        //记录分享日志
        sysTaskShareLogRepository.save(new SysTaskShareLog()
                .setMixSharer(buyerNick)
                .setSharer(syscustom.getZnick())
                .setCreateTime(requestStartTime)
                .setShareTime(requestStartTimeString)
                .setRemark(sharePlace)
        );

        ActBaseSettingDto actSetting = projectHelper.actBaseSettingFind();


        //分享，每日前3次发放星愿，最后一天前10次
        long l = sysTaskShareLogRepository.countByMixSharerAndShareTime(buyerNick, requestStartTimeString);
        long limit = requestStartTime.after(actSetting.getActLastTime()) ? CoachConstant.share_limit_count_last : CoachConstant.share_limit_count;
        Integer winStar = requestStartTime.after(actSetting.getActLastTime()) ? CoachConstant.share_xingyuan_last : CoachConstant.share_xingyuan;
        if (l <= limit) {
            if(BooleanConstant.BOOLEAN_NO.equals(syscustom.getHaveInviteFriend())){
                syscustom.setHaveInviteFriend(BooleanConstant.BOOLEAN_YES);
            }
            syscustom.setStarValue(syscustom.getStarValue() + winStar);
            sysCustomRepository.save(syscustom);
        }

        //分享给熊加一次游戏机会
//        if("share_bear".equals(sharePlace)){
//            SysGameBoardDaily todayGameBoard = projectHelper.findTodayGameBoard(syscustom, requestStartTime);
//            //增加与白熊答题机会
//            todayGameBoard.setBearQuestionChance(todayGameBoard.getBearQuestionChance() + 1);
//            sysGameBoardDailyRepository.save(todayGameBoard);
//        }

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        return YunReturnValue.ok(resultMap,"记录分享日志");
    }
}




