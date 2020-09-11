package com.duomai.project.product.recycle.execute;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.dto.ActBaseSetting;
import com.duomai.project.product.general.dto.XyData;
import com.duomai.project.product.general.dto.XyReturn;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.enums.LuckyChanceFrom;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.tool.LuckyDrawHelper;
import com.duomai.project.tool.ProjectHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/* 抽奖页--load
 * @description
 * @create by 王星齐
 * @time 2020-08-27 16:11:27
 **/
@Component
public class LuckyDrawLoadExecute implements IApiExecute {
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private SysAwardRepository sysAwardRepository;
    @Autowired
    private SysLuckyChanceRepository sysLuckyChanceRepository;
    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.活动配置查询，活动期间才可访问接口*/
        ActBaseSetting actBaseSetting = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate(actBaseSetting);

        /*2.确认当前玩家身份*/
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(
                sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
        if (Objects.isNull(sysCustom)) {
            return YunReturnValue.fail("不存在该玩家");
        }
        /*3.查询订单是否属实，然后发放翻牌机会*/
        if (StringUtils.isNotBlank(sysCustom.getZnick())) {
            //未授权不查
            XyReturn ordersByOpenId = projectHelper.findOrdersByOpenId(System.currentTimeMillis(), sysParm.getApiParameter().getYunTokenParameter().getOpenUId(), sysCustom.getZnick(),
                    actBaseSetting.getActStartTime().getTime(), actBaseSetting.getActEndTime().getTime(), sysParm.getApiParameter().getYunTokenParameter().getBuyerNick(), sysParm.getRequestStartTime());
            if (ordersByOpenId.getCode().equals(0) && CollectionUtils.isNotEmpty(ordersByOpenId.getData())) {
                List<String> collectDm = new ArrayList<>();
                List<SysLuckyChance> allByBuyerNick = sysLuckyChanceRepository.findAllByBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
                if (allByBuyerNick.size() > 0) {
                    collectDm.addAll(allByBuyerNick.stream().map(SysLuckyChance::getTid).collect(Collectors.toList()));
                }
                List<String> collectXy = ordersByOpenId.getData().stream().map(XyData::getOrderSn).filter(o -> !collectDm.contains(o)).collect(Collectors.toList());
                if (collectXy.size() > 0) {
                    Date sendTime = new Date();
                    List<SysLuckyChance> newChances = collectXy.stream().map((tid) -> new SysLuckyChance()
                            .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                            .setChanceFrom(LuckyChanceFrom.ORDER_COMMIT)
                            .setGetTime(sendTime)
                            .setIsUse(BooleanConstant.BOOLEAN_NO)
                            .setTid(tid)).collect(Collectors.toList());
                    luckyDrawHelper.sendLuckyChance(newChances);
                }
            }
        }
        /*4.数据展示*/
        Map result = new HashMap<>();
        //@1.未使用的抽奖机会
        result.put("act_lucky_chance_num", luckyDrawHelper.unUseLuckyChance(sysCustom.getBuyerNick()));
        //@2.奖品预览
        result.put("act_award_info", sysAwardRepository.findAwardInfo());
        //@3.活动规则
        result.put("act_base_setting", actBaseSetting);
        //@4.我的奖品
        List<SysLuckyDrawRecord> byPlayerBuyerNickAndIsWin = sysLuckyDrawRecordRepository.findByPlayerBuyerNickAndIsWin(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick(), BooleanConstant.BOOLEAN_YES);
        byPlayerBuyerNickAndIsWin.forEach((x) -> {
            x.setId(null);
            x.setLuckyChance(null);
            x.setPlayerBuyerNick(null);
            x.setIsWin(null);
        });
        result.put("my_lucky_bag", byPlayerBuyerNickAndIsWin);
        return YunReturnValue.ok(result, "玩家成功登陆抽奖页");
    }
}
