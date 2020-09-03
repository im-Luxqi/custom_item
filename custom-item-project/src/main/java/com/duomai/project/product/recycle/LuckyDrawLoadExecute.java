package com.duomai.project.product.recycle;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.dto.ActBaseSetting;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.enums.LuckyChanceFrom;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.tool.LuckyDrawHelper;
import com.duomai.project.tool.ProjectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        /*todo:3.查询订单是否属实，然后发放翻牌机会*/
//        String orderSn = request.getParameter("orderSn");
        String orderSn = "101112";

        long l = sysLuckyChanceRepository.countByTid(orderSn);
        if (l == 0) {
//            XyReturn orderBySn = projectHelper.findOrderBySn(System.currentTimeMillis(), orderSn);
//            XyReturn ordersByOpenId = projectHelper.findOrdersByOpenId(System.currentTimeMillis(), sysParm.getApiParameter().getYunTokenParameter().getOpenUId());
//            if (orderBySn.getCode().equals(-1)) {
//                //根据订单号查询订单信息接口异常，记录
//            }
//            if (ordersByOpenId.getCode().equals(-1)) {
//                //根据用户id查询订单信息接口异常记录
//            }
            luckyDrawHelper.sendLuckyChance(sysCustom.getBuyerNick(), LuckyChanceFrom.ORDER_COMMIT, 1, orderSn);
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
        result.put("my_lucky_bag", byPlayerBuyerNickAndIsWin);
        return YunReturnValue.ok(result, "玩家成功登陆抽奖页");
    }
}
