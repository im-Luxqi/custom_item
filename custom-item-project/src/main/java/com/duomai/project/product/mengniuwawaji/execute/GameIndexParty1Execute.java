package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGameBoardDaily;
import com.duomai.project.product.general.entity.SysPagePvLog;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.enums.PlayActionEnum;
import com.duomai.project.product.general.enums.PvPageEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.product.general.repository.SysPagePvLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 场景1 load
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameIndexParty1Execute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;
    @Autowired
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {



        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Date requestStartTime = sysParm.getRequestStartTime();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        /*保存pv*/
        sysPagePvLogRepository.save(new SysPagePvLog()
                .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                .setCreateTime(sysParm.getRequestStartTime())
                .setId(sysParm.getApiParameter().getCommomParameter().getIp())
                .setPage(PvPageEnum.PAGE_PARTY1));


        long l = sysLuckyDrawRecordRepository.countByPlayerBuyerNickAndLuckyChance(buyerNick, AwardUseWayEnum.PENGUIN.getValue());
        SysGameBoardDaily daily = projectHelper.findTodayGameBoard(syscustom, requestStartTime);


        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        //1.是否开启过礼盒
        resultMap.put("have_open_award_penguin", l > 0);
        resultMap.put("get_letter_party2", syscustom.getCurrentAction().equals(PlayActionEnum.letter_party2));
        resultMap.put("first_play_snowman", daily.getFirstGameSnowman().equals(BooleanConstant.BOOLEAN_YES));
        resultMap.put("first_play_penguin", daily.getFirstGamePenguin().equals(BooleanConstant.BOOLEAN_YES));
        resultMap.put("first_play_Bear", daily.getFirstGameBear().equals(BooleanConstant.BOOLEAN_YES));
        resultMap.put("today_have_play_snowman", daily.getGameSnowman() > 0);
        resultMap.put("today_have_play_penguin", daily.getGamePenguin() > 0);
        resultMap.put("today_have_play_Bear", daily.getGameBear() > 0);
        resultMap.put("current_action", syscustom.getCurrentAction());
//        resultMap.put("bear_question_chance", daily.getBearQuestionChance());

        return YunReturnValue.ok(resultMap, "场景1" +
                "get_letter_party2 = true ---> 表示玩家得到邀请函，尚未开启" +
                "first_play_snowman = true ---> 表示玩家首次或尚未与雪人互动过，第一天与雪人互动" +
                "today_have_play_snowman = true ---> 表示玩家今日已互动过" +
                "bear_question_chance  ---> 当前和熊答题的机会(取消)"
        );
    }
}




