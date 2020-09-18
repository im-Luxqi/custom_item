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
import com.duomai.project.tool.CommonDateParseUtil;
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
        //未注册的玩家，注册身份
        boolean first_from_xianyu = false;
        if (Objects.isNull(sysCustom)) {
            sysCustom = projectHelper.customInit(sysParm);
            sysCustomRepository.save(sysCustom);
            first_from_xianyu = true;
        } else if (StringUtils.isNotBlank(sysCustom.getZnick())) {
            /*3.查询订单是否属实，然后发放翻牌机会*/
            //查询咸鱼接口
            XyReturn ordersByOpenId = projectHelper.findOrdersByOpenId(System.currentTimeMillis(), sysParm.getApiParameter().getYunTokenParameter().getOpenUId(), sysCustom.getZnick(),
                    actBaseSetting.getActStartTime().getTime(), actBaseSetting.getActEndTime().getTime(), sysParm.getApiParameter().getYunTokenParameter().getBuyerNick(), sysParm.getRequestStartTime());
            if (ordersByOpenId.getCode().equals(0) && CollectionUtils.isNotEmpty(ordersByOpenId.getData())) {//接口调用成功执行发机会操作
                Set<String> collectDm_tid = new HashSet<String>();
                Set<String> collectDm_time = new HashSet<String>();
                List<SysLuckyChance> allByBuyerNick = sysLuckyChanceRepository.findAllByBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
                if (allByBuyerNick.size() > 0) {
                    collectDm_tid.addAll(allByBuyerNick.stream().map(SysLuckyChance::getTid).collect(Collectors.toList()));
                    collectDm_time.addAll(allByBuyerNick.stream().map(o -> CommonDateParseUtil.date2string(o.getTidTime(), "yyyy-MM-dd")).collect(Collectors.toList()));
                }
                //过滤掉已经发送过次数的订单
                Map<String, List<XyData>> collect = ordersByOpenId.getData().stream()
                        .filter(o -> !collectDm_tid.contains(o.getOrderSn()))
                        .collect(Collectors.groupingBy(XyData::getOrderTimeYYYYMMDD));

                List<XyData> xyData_shouldsend = new ArrayList<>();
                List<XyData> xyData_notsend = new ArrayList<>();
                long size = allByBuyerNick.stream().filter(o -> LuckyChanceFrom.ORDER_COMMIT.equals(o.getChanceFrom())).count();
                for (Map.Entry<String, List<XyData>> entry : collect.entrySet()) {
                    String mapKey = entry.getKey();
                    List<XyData> mapValue = entry.getValue();
                    if (!collectDm_time.contains(mapKey)) {
                        for (int i = 0; i < mapValue.size(); i++) {
                            if (i == 0 && size < 5) {//每天首单才送次数
                                xyData_shouldsend.add(mapValue.get(i));
                                size++;
                                continue;
                            }
                            xyData_notsend.add(mapValue.get(i));
                        }
                        continue;
                    }
                    xyData_notsend.addAll(mapValue);
                }

                List<SysLuckyChance> newChances = new ArrayList<>();
                Date sendTime = new Date();
                if (CollectionUtils.isNotEmpty(xyData_shouldsend)) {
                    newChances.addAll(xyData_shouldsend.stream().map((o) -> new SysLuckyChance()
                            .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                            .setChanceFrom(LuckyChanceFrom.ORDER_COMMIT)
                            .setGetTime(sendTime)
                            .setTidTime(new Date(Long.parseLong(o.getLogTime())))
                            .setIsUse(BooleanConstant.BOOLEAN_NO)
                            .setTid(o.getOrderSn())).collect(Collectors.toList()));
                }
                if (CollectionUtils.isNotEmpty(xyData_notsend)) {
                    newChances.addAll(xyData_notsend.stream().map((o) -> new SysLuckyChance()
                            .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                            .setChanceFrom(LuckyChanceFrom.ORDER_INVALID)
                            .setGetTime(sendTime)
                            .setTidTime(new Date(Long.parseLong(o.getLogTime())))
                            .setIsUse(BooleanConstant.BOOLEAN_UNDEFINED)
                            .setTid(o.getOrderSn())).collect(Collectors.toList()));
                }
                if (CollectionUtils.isNotEmpty(newChances)) {
                    luckyDrawHelper.sendLuckyChance(newChances);
                }
            }
        }

        /*4.数据展示*/
        Map result = new HashMap<>();
        //@-1.是否是咸鱼用户首次来
        result.put("first_from_xianyu", first_from_xianyu);
        //@0.客户详情
        result.put("custom", sysCustom.setId(null)
                .setCreateTime(null).setUpdateTime(null).setOpenId(null));
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
