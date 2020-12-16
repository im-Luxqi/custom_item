package com.duomai.project.product.general.execute;

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
 * @内容：任务页面 浏览商品操作
 * @创建人：lyj
 * @创建时间：2020.9.30
 */
@Component
public class TaskFifteenExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysTaskBrowseLogRepository sysTaskBrowseLogRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private SysSettingAwardRepository sysSettingAwardRepository;
    @Autowired
    private SysGameBoardDailyRepository sysGameBoardDailyRepository;
    @Autowired
    private SysGameLogRepository sysGameLogRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验*/
        //活动只能再活动期间
        projectHelper.actTimeValidate();
        Date requestStartTime = sysParm.getRequestStartTime();
        String requestStartTimeString = CommonDateParseUtil.date2string(requestStartTime, "yyyy-MM-dd");
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        SysSettingAward winAward = null;
        SysGameBoardDaily todayGameBoard = projectHelper.findTodayGameBoard(syscustom, requestStartTime);
//        Assert.isTrue(, "每天一次哦");
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        if (todayGameBoard.getGameDog() == 0) {
            todayGameBoard.setGameDog(todayGameBoard.getGameDog() + 1);
            sysGameBoardDailyRepository.save(todayGameBoard);
            //抽奖
            List<SysSettingAward> awards = sysSettingAwardRepository.findByUseWayOrderByLuckyValueAsc(todayGameBoard.getFirstGameDog() > 0 ? AwardUseWayEnum.DOG_FIRST : AwardUseWayEnum.POOL);
            winAward = luckyDrawHelper.luckyDraw(awards, syscustom, sysParm.getRequestStartTime(), "_dog");

            //2.发放星愿，更新活动进度
            syscustom.setStarValue(syscustom.getStarValue() + CoachConstant.dog_xingyuan);
            if (syscustom.getCurrentAction().equals(PlayActionEnum.playwith_dog)) {
                syscustom.setCurrentAction(PlayActionEnum.letter_party3);
            }
            sysCustomRepository.save(syscustom);

            //4.记录互动日志
            sysGameLogRepository.save(new SysGameLog()
                    .setBuyerNick(buyerNick)
                    .setCreateTime(requestStartTime)
                    .setCreateTimeString(requestStartTimeString)
                    .setPartner(PlayPartnerEnum.dog));
            /*只反馈有效数据*/

            resultMap.put("win", !Objects.isNull(winAward));
            resultMap.put("award", winAward);
            resultMap.put("get_letter_party3", syscustom.getCurrentAction().equals(PlayActionEnum.letter_party3) &&
                    todayGameBoard.getFirstGameDog() == 1);
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
        } else {
            resultMap.put("get_letter_party3", false);
        }

        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        return YunReturnValue.ok(resultMap, "浏览成功!");
    }
}
