package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
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
 * 场景2 点灯
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GamePlayLampExecute implements IApiExecute {

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
    @Autowired
    private SysTaskShareLogRepository sysTaskShareLogRepository;

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
        Assert.isTrue(todayGameBoard.getGameLamp() == 0, "每天玩一次哦");


        //首次点灯
        if (todayGameBoard.getFirstGameLamp().equals(BooleanConstant.BOOLEAN_YES)) {
            long l = sysTaskShareLogRepository.countByMixSharer(buyerNick);
            Assert.isTrue(l >= 3, "分享3位好友");
        }


        //2.发放星愿，更新活动进度
        syscustom.setStarValue(syscustom.getStarValue() + CoachConstant.lamp_xingyuan);
        if (syscustom.getCurrentAction().equals(PlayActionEnum.playwith_lamp)) {
            syscustom.setCurrentAction(PlayActionEnum.playwith_dog);
        }
        sysCustomRepository.save(syscustom);

        //3.增加今日互动次数
        todayGameBoard.setGameLamp(todayGameBoard.getGameLamp() + 1);
        sysGameBoardDailyRepository.save(todayGameBoard);

        //4.记录互动日志
        sysGameLogRepository.save(new SysGameLog()
                .setBuyerNick(buyerNick)
                .setCreateTime(requestStartTime)
                .setCreateTimeString(requestStartTimeString)
                .setPartner(PlayPartnerEnum.lamp)
        );

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        return YunReturnValue.ok(resultMap,"和灯玩");
    }
}




