package com.duomai.project.product.general.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.FinishTheTaskHelper;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysSettingCommodity;
import com.duomai.project.product.general.entity.SysTaskBrowseLog;
import com.duomai.project.product.general.entity.SysTaskDailyBoard;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysSettingCommodityRepository;
import com.duomai.project.product.general.repository.SysTaskBrowseLogRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private FinishTheTaskHelper finishTheTaskHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验*/
        //活动只能再活动期间
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate();
        //校验传参
        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        Long numId = jsonObjectAdmjson.getLong("numId");
        Assert.notNull(numId, "商品id不能为空");
        SysSettingCommodity commodity = sysSettingCommodityRepository.findFirstByNumId(numId);
        Assert.notNull(commodity, "不存在的商品");
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        Assert.isTrue(BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveAuthorization()), "请先授权");


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
        }


        //浏览送抽奖机会
        long l = luckyDrawHelper.countTodayLuckyChanceFrom(buyerNick, LuckyChanceFromEnum.BROWSE);
        Integer taskBrowseShouldSee = actBaseSettingDto.getTaskBrowseShouldSee();
        if (l == 0 && taskBrowseShouldSee.equals(todayHasBrowseLogs.size())) {
            Integer thisGet = 1;
            luckyDrawHelper.sendCard(syscustom, LuckyChanceFromEnum.BROWSE, thisGet,
                    "每日浏览，获得【有料品鉴官】一博送你的食力拼图*" + thisGet);
        }
        if (todayHasBrowseLogs.size() <= taskBrowseShouldSee) {
            SysTaskDailyBoard taskDailyBoard = finishTheTaskHelper.todayTaskBoard(buyerNick);
            taskDailyBoard.setHaveFinishBrowseToday(BooleanConstant.BOOLEAN_YES);
            taskDailyBoard.setBrowseProgress("(" + todayHasBrowseLogs.size() + "/" + taskBrowseShouldSee + ")");
            finishTheTaskHelper.updateTaskBoard(taskDailyBoard);
        }

        return YunReturnValue.ok("浏览成功!");
    }
}
