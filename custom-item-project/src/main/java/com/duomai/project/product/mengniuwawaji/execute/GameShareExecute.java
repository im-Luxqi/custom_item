package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
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
                "可选值[share_bear,share_tent,share_other]");
        org.springframework.util.Assert.isTrue("share_bear".equals(sharePlace) ||
                "share_tent".equals(sharePlace) ||
                "share_other".equals(sharePlace), "无效的奖品位置");


        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Date requestStartTime = sysParm.getRequestStartTime();
        String requestStartTimeString = CommonDateParseUtil.date2string(requestStartTime, "yyyy-MM-dd");
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        SysGameBoardDaily todayGameBoard = sysGameBoardDailyRepository.findFirstByBuyerNickAndCreateTimeString(buyerNick, requestStartTimeString);
        Assert.isTrue(todayGameBoard.getGameSnowman() == 0, "每天送一次哦");
        ActBaseSettingDto actSetting = projectHelper.actBaseSettingFind();


        //记录分享日志
        sysTaskShareLogRepository.save(new SysTaskShareLog()
                .setSharer(buyerNick)
                .setCreateTime(requestStartTime)
                .setShareTime(requestStartTimeString)
                .setRemark(sharePlace)
        );

        //发放星愿,分平时和最后一天
        long l = sysTaskShareLogRepository.countByMixSharerdAndShareTime(buyerNick, requestStartTimeString);
        long limit = requestStartTime.after(actSetting.getActLastTime()) ? CoachConstant.share_limit_count_last : CoachConstant.share_limit_count;
        Integer winStar = requestStartTime.after(actSetting.getActLastTime()) ? CoachConstant.share_xingyuan_last : CoachConstant.share_xingyuan;
        if (l <= limit) {
            syscustom.setStarValue(syscustom.getStarValue() + winStar);
            sysCustomRepository.save(syscustom);
        }


        return YunReturnValue.ok("分享");
    }
}




