package com.duomai.project.product.mengniuwawaji.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.product.general.dto.CardExchangeDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.entity.SysSettingAward;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysSettingAwardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * 首页 抽奖
 *
 * @author 王星齐
 * @description
 * @create 2020/11/19 15:13
 */
@Component
public class GameIndexLuckyDrawExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private SysSettingAwardRepository sysSettingAwardRepository;


    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        CardExchangeDto cardExchange = sysParm.getApiParameter().findBeautyAdmjson(CardExchangeDto.class);

        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        Assert.isTrue(BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveAuthorization()), "请先授权");

        List<SysLuckyChance> sysLuckyChances = luckyDrawHelper.cardComposition(cardExchange, buyerNick);
        List<SysSettingAward> thisTimeAwardPool = sysSettingAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.POOL);
        SysSettingAward winAward = luckyDrawHelper.luckyDraw(thisTimeAwardPool, sysLuckyChances,
                syscustom, sysParm.getRequestStartTime());

        /*只反馈有效数据*/
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("win", !Objects.isNull(winAward));
        resultMap.put("award", winAward);
        if (!Objects.isNull(winAward)) {
            winAward.setEname(null)
                    .setId(null)
                    .setRemainNum(null)
                    .setSendNum(null)
                    .setTotalNum(null)
                    .setLuckyValue(null)
                    .setUseWay(null)
                    .setMaxCanGet(null)
                    .setPoolLevel(null);
        }
        return YunReturnValue.ok(resultMap, "玩家成功进行抽奖");
    }
}
