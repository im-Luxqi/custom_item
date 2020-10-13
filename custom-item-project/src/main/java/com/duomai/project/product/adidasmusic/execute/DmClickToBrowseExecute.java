package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysBrowseLog;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.enums.CommonExceptionEnum;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.repository.SysBrowseLogRepository;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author cjw
 * @description 阿迪双十一小程序二楼 点击浏览
 * @time 2020-10-03
 */
@Service
public class DmClickToBrowseExecute implements IApiExecute {

    @Resource
    private SysBrowseLogRepository browseLogRepository;
    @Resource
    private SysLuckyChanceRepository luckyChanceRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) {

        //取参
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        Date date = sysParm.getRequestStartTime();
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Long numId = object.getLong("numId");

        //获取该粉丝当天浏览记录
        SysBrowseLog browseLogs = browseLogRepository.findFirstByBuyerNickAndCreateTimeBetweenAndNumId(buyerNick,
                CommonDateParseUtil.getStartTimeOfDay(date), CommonDateParseUtil.getEndTimeOfDay(date),numId);

        //为空保存浏览日志
        if(browseLogs == null){
            browseLogs = new SysBrowseLog();
            browseLogRepository.save(
                    browseLogs.setNumId(numId)
                    .setBuyerNick(buyerNick)
                    .setCreateTime(date)
            );
        }

        //查询该粉丝今天获得了几次
        long luckyNum = luckyChanceRepository.countByBuyerNickAndChanceFromAndGetTimeBetween(buyerNick, LuckyChanceFromEnum.BROWSE,
                CommonDateParseUtil.getStartTimeOfDay(date), CommonDateParseUtil.getEndTimeOfDay(date)
        );

        Assert.isTrue(luckyNum == 1, "亲，您已经获得过一次抽奖机会了哦!");

        SysLuckyChance luckyChance = new SysLuckyChance();
        luckyChanceRepository.save(luckyChance.setBuyerNick(buyerNick)
                .setGetTime(CommonDateParseUtil.date2date(date, CommonDateParseUtil.YYYY_MM_DD_HH_MM_SS))
                .setChanceFrom(LuckyChanceFromEnum.BROWSE)
                .setIsUse(BooleanConstant.BOOLEAN_NO)
        );
        return YunReturnValue.ok(CommonExceptionEnum.OPERATION_SUCCESS.getMsg());
    }
}
