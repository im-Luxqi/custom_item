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
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author cjw
 * @description 阿迪双十一小程序二楼 点击浏览
 * @date 2020-10-03
 */
@Service
public class DmClickToBrowseExecute implements IApiExecute {

    @Resource
    private SysBrowseLogRepository browseLogRepository;
    @Resource
    private SysLuckyChanceRepository luckyChanceRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {

        //取参
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        Date date = sysParm.getRequestStartTime();
//        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        String buyerNick = "小明";
        Long numId = object.getLong("numId");

        //获取该粉丝当天浏览记录
        SysBrowseLog browseLog = new SysBrowseLog();
        List<SysBrowseLog> browseLogs = browseLogRepository.findAll(Example.of(browseLog.setBuyerNick(buyerNick)
                .setCreateTime(CommonDateParseUtil.date2date(date, CommonDateParseUtil.YYYY_MM_DD))));

        Integer num = browseLogs.size();
        if (!browseLogs.isEmpty()) {
            AtomicReference<Integer> isB = new AtomicReference<>(0);
            browseLogs.stream().forEach(o -> {
                if (o.getNumId().longValue() == numId.longValue()) {
                    isB.set(1);
                }
            });
            if (isB.get() != 1) {
                num += 1;
                browseLog.setNumId(numId);
                browseLogRepository.save(browseLog);
            }
        } else {
            num += 1;
            browseLog.setNumId(numId);
            browseLogRepository.save(browseLog);
        }

        //查询该粉丝今天获得了几次
        long luckyNum = luckyChanceRepository.countByBuyerNickAndChanceFromAndGetTimeBetween(buyerNick, LuckyChanceFromEnum.BROWSE,
                CommonDateParseUtil.getStartTimeOfDay(date),CommonDateParseUtil.getEndTimeOfDay(date)
        );
        int sendNum = (int) (luckyNum * 2);

        //todo 浏览几次送抽签机会？
        if (num - sendNum >= 2) {

            SysLuckyChance luckyChance = new SysLuckyChance();
            luckyChanceRepository.save(luckyChance.setBuyerNick(buyerNick)
                    .setGetTime(CommonDateParseUtil.date2date(date, CommonDateParseUtil.YYYY_MM_DD_HH_MM_SS))
                    .setChanceFrom(LuckyChanceFromEnum.BROWSE)
                    .setIsUse(BooleanConstant.BOOLEAN_NO)
            );
            //todo 是否有进阶


        }
        return YunReturnValue.ok(CommonExceptionEnum.OPERATION_SUCCESS.getMsg());
    }
}
