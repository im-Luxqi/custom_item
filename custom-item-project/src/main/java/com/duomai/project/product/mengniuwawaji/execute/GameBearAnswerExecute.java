package com.duomai.project.product.mengniuwawaji.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGameBoardDaily;
import com.duomai.project.product.general.entity.SysGameLog;
import com.duomai.project.product.general.entity.SysSettingAward;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.enums.CoachConstant;
import com.duomai.project.product.general.enums.PlayActionEnum;
import com.duomai.project.product.general.enums.PlayPartnerEnum;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * 场景1 白熊 回答
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameBearAnswerExecute implements IApiExecute {

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
    private SysSettingAwardRepository sysSettingAwardRepository;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        projectHelper.actTimeValidate();


        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        Boolean win = jsonObjectAdmjson.getBoolean("win");
        Assert.notNull(win, "本次答题结果是胜利还是失败，不能为空");

        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Date requestStartTime = sysParm.getRequestStartTime();
        String requestStartTimeString = CommonDateParseUtil.date2string(requestStartTime, "yyyy-MM-dd");
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        SysGameBoardDaily todayGameBoard = sysGameBoardDailyRepository.findFirstByBuyerNickAndCreateTimeString(buyerNick, requestStartTimeString);
        Assert.isTrue(todayGameBoard.getGameBear() == 0, "每天玩一次哦");
        Assert.isTrue(todayGameBoard.getBearQuestionChance() > 0, "答题次数不足");

        //2.扣减答题次数
        todayGameBoard.setBearQuestionChance(todayGameBoard.getBearQuestionChance() - 1);
        todayGameBoard = sysGameBoardDailyRepository.save(todayGameBoard);

        if (!win) {
            return YunReturnValue.ok("答题错误");
        }

        //2.发放星愿，更新活动进度
        syscustom.setStarValue(syscustom.getStarValue() + CoachConstant.bear_xingyuan);
        if (syscustom.getCurrentAction().equals(PlayActionEnum.playwith_bear)) {
            syscustom.setCurrentAction(PlayActionEnum.letter_party2);
        }
        sysCustomRepository.save(syscustom);

        //3.增加今日互动次数
        todayGameBoard.setGameBear(todayGameBoard.getGameBear() + 1);
        sysGameBoardDailyRepository.save(todayGameBoard);

        //4.记录互动日志
        sysGameLogRepository.save(new SysGameLog()
                .setBuyerNick(buyerNick)
                .setCreateTime(requestStartTime)
                .setCreateTimeString(requestStartTimeString)
                .setPartner(PlayPartnerEnum.bear)
        );


        //抽奖
        List<SysSettingAward> awards = sysSettingAwardRepository.findByUseWay(AwardUseWayEnum.POOL);
        SysSettingAward winAward = luckyDrawHelper.luckyDraw(awards, syscustom, requestStartTime, "_bear");
        /*只反馈有效数据*/
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("win", !Objects.isNull(winAward));
        resultMap.put("award", winAward);
        resultMap.put("get_letter_party2", syscustom.getCurrentAction().equals(PlayActionEnum.letter_party2));
        if (!Objects.isNull(winAward)) {
            winAward.setEname(null)
                    .setId(null)
                    .setRemainNum(null)
                    .setSendNum(null)
                    .setTotalNum(null)
                    .setLuckyValue(null)
                    .setUseWay(null)
                    .setType(null)
                    .setPoolLevel(null);
        }
        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        return YunReturnValue.ok(resultMap, "答题正确");
    }
}




