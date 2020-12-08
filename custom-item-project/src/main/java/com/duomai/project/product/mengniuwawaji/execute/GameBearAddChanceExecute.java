package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGameBoardDaily;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 场景1 白熊 增加答题次数
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameBearAddChanceExecute implements IApiExecute {

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

        SysGameBoardDaily todayGameBoard = sysGameBoardDailyRepository.findFirstByBuyerNickAndCreateTimeString(buyerNick, requestStartTimeString);
        Assert.isTrue(todayGameBoard.getGameSnowman() == 0, "每天送一次哦");


        //增加与白熊答题
        todayGameBoard.setBearQuestionChance(todayGameBoard.getBearQuestionChance() + 1);
        sysGameBoardDailyRepository.save(todayGameBoard);

        //3.增加今日互动次数
        todayGameBoard.setGameSnowman(todayGameBoard.getGameSnowman() + 1);
        sysGameBoardDailyRepository.save(todayGameBoard);

        return YunReturnValue.ok("白熊 增加答题次数");
    }
}




