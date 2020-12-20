package com.duomai.project.product.general.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysSettingCommodity;
import com.duomai.project.product.general.entity.SysTaskBrowseLog;
import com.duomai.project.product.general.enums.CoachConstant;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @内容：任务页面 浏览商品操作
 * @创建人：lyj
 * @创建时间：2020.9.30
 */
@Component
public class TaskBrowseExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private SysSettingCommodityRepository sysSettingCommodityRepository;

    @Autowired
    private SysTaskBrowseLogRepository sysTaskBrowseLogRepository;
    @Autowired
    private SysLuckyChanceRepository sysLuckyChanceRepository;
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
        ActBaseSettingDto actSetting = projectHelper.actBaseSettingFind();
        //校验传参
        Date requestStartTime = sysParm.getRequestStartTime();
        String requestStartTimeString = CommonDateParseUtil.date2string(requestStartTime, "yyyy-MM-dd");
        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        Long numId = jsonObjectAdmjson.getLong("numId");
        Assert.notNull(numId, "商品id不能为空");
        SysSettingCommodity commodity = sysSettingCommodityRepository.findFirstByNumId(numId);
        Assert.notNull(commodity, "不存在的商品");
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");


        /*2.记录浏览日志*/
        //只记录今日未浏览的
        Date today = sysParm.getRequestStartTime();
        List<SysTaskBrowseLog> todayHasBrowseLogs = sysTaskBrowseLogRepository.findByBuyerNickAndBrowseTime(buyerNick
                , CommonDateParseUtil.date2string(today, "yyyy-MM-dd"));
        boolean hasBrowse = false;
        if (!CollectionUtils.isEmpty(todayHasBrowseLogs)) {
            for (SysTaskBrowseLog x : todayHasBrowseLogs) {
                if (x.getNumId().equals(numId)) {
                    hasBrowse = true;
                    break;
                }
            }
        }
        if (!hasBrowse) {
            SysTaskBrowseLog thisBrowse = sysTaskBrowseLogRepository.save(new SysTaskBrowseLog()
                    .setBuyerNick(buyerNick)
                    .setCreateTime(today)
                    .setBrowseTime(CommonDateParseUtil.date2string(today, "yyyy-MM-dd"))
                    .setNumId(numId));
            todayHasBrowseLogs.add(thisBrowse);


            //浏览，每日前3次发放星愿，最后一天前10次
            if (requestStartTime.before(actSetting.getActLastTime())) {
                if (todayHasBrowseLogs.size() <= CoachConstant.browse_limit_count) {
                    syscustom.setHaveBrowseGoods(BooleanConstant.BOOLEAN_YES);
                    syscustom.setStarValue(syscustom.getStarValue() + CoachConstant.browse_xingyuan);
                    syscustom = sysCustomRepository.save(syscustom);
                }
            }
        }


//        SysSettingAward winAward = null;
//        //第三次浏览抽奖
//        Integer taskBrowseShouldSee = 1;
//        SysGameBoardDaily todayGameBoard = projectHelper.findTodayGameBoard(syscustom, requestStartTime);
//        if (todayGameBoard.getGameDog() == 0 && taskBrowseShouldSee.equals(todayHasBrowseLogs.size())) {
//
//            todayGameBoard.setGameDog(todayGameBoard.getGameDog() + 1);
//            sysGameBoardDailyRepository.save(todayGameBoard);
//
//            //抽奖
//            List<SysSettingAward> awards = sysSettingAwardRepository.findByUseWayOrderByLuckyValueAsc(todayGameBoard.getFirstGameDog() > 0 ? AwardUseWayEnum.DOG_FIRST : AwardUseWayEnum.POOL);
//            winAward = luckyDrawHelper.luckyDraw(awards, syscustom, sysParm.getRequestStartTime(), "_dog");
//
//
//            //2.发放星愿，更新活动进度
//            syscustom.setStarValue(syscustom.getStarValue() + CoachConstant.dog_xingyuan);
//            if (syscustom.getCurrentAction().equals(PlayActionEnum.playwith_dog)) {
//                syscustom.setCurrentAction(PlayActionEnum.letter_party3);
//            }
//            sysCustomRepository.save(syscustom);
//
//            //4.记录互动日志
//            sysGameLogRepository.save(new SysGameLog()
//                    .setBuyerNick(buyerNick)
//                    .setCreateTime(requestStartTime)
//                    .setCreateTimeString(requestStartTimeString)
//                    .setPartner(PlayPartnerEnum.dog));
//
//        }
        /*只反馈有效数据*/
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
//        resultMap.put("win", !Objects.isNull(winAward));
//        resultMap.put("award", winAward);
//        resultMap.put("get_letter_party3", syscustom.getCurrentAction().equals(PlayActionEnum.letter_party3));
//        if (!Objects.isNull(winAward)) {
//            winAward.setEname(null)
//                    .setId(null)
//                    .setRemainNum(null)
//                    .setSendNum(null)
//                    .setTotalNum(null)
//                    .setLuckyValue(null)
//                    .setUseWay(null)
//                    .setType(null)
//                    .setPoolLevel(null);
//        }
//        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        return YunReturnValue.ok(resultMap, "浏览成功!");
    }
}
