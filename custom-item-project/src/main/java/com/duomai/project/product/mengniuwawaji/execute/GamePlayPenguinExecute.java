package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGameBoardDaily;
import com.duomai.project.product.general.entity.SysGameLog;
import com.duomai.project.product.general.enums.CoachConstant;
import com.duomai.project.product.general.enums.PlayActionEnum;
import com.duomai.project.product.general.enums.PlayPartnerEnum;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 场景1 和企鹅玩
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GamePlayPenguinExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private SysGameLogRepository sysGameLogRepository;
    @Autowired
    private SysGameBoardDailyRepository sysGameBoardDailyRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        projectHelper.actTimeValidate();
        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Date requestStartTime = sysParm.getRequestStartTime();
        String requestStartTimeString = CommonDateParseUtil.date2string(requestStartTime, "yyyy-MM-dd");
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        SysGameBoardDaily todayGameBoard = projectHelper.findTodayGameBoard(syscustom, requestStartTime);
        Assert.isTrue(todayGameBoard.getGamePenguin() == 0, "每天玩一次哦");


        //2.发放星愿，更新活动进度
        syscustom.setStarValue(syscustom.getStarValue() + CoachConstant.penguin_xingyuan);
        if (syscustom.getCurrentAction().equals(PlayActionEnum.playwith_penguin)) {
            syscustom.setCurrentAction(PlayActionEnum.playwith_bear);
        }
        sysCustomRepository.save(syscustom);

        //3.增加今日互动次数
        todayGameBoard.setGamePenguin(todayGameBoard.getGamePenguin() + 1);
        sysGameBoardDailyRepository.save(todayGameBoard);

        //4.记录互动日志
        sysGameLogRepository.save(new SysGameLog()
                .setBuyerNick(buyerNick)
                .setCreateTime(requestStartTime)
                .setCreateTimeString(requestStartTimeString)
                .setPartner(PlayPartnerEnum.penguin)
        );

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        resultMap.put("current_action", syscustom.getCurrentAction());
        return YunReturnValue.ok(resultMap,"和企鹅玩");
    }
}




