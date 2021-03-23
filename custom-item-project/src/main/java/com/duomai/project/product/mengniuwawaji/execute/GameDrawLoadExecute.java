package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.entity.SysSettingAward;
import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 游戏首页 加载
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameDrawLoadExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");


        //获得奖品并分类
        List<SysSettingAward> all = luckyDrawHelper.findAllAward();
        for (SysSettingAward award : all) {
            award.setId(null);
            award.setEname(null);
            award.setLuckyValue(null);
            award.setDescription(null);
            award.setPoolLevel(null);
            award.setRemainNum(null);
            award.setSendNum(null);
            award.setTotalNum(null);
            award.setHaveGetNum(null);
        }
        List<SysSettingAward> exchangeAward = new ArrayList<>();
        List<SysSettingAward> otherAward = new ArrayList<>();
        all.forEach(sysSettingAward -> {
            if (AwardTypeEnum.EXCHANGE.equals(sysSettingAward.getType())) {
                exchangeAward.add(sysSettingAward);
            } else {
                otherAward.add(sysSettingAward);
            }
        });
        //获得未使用的所有卡牌
        LinkedHashMap<AwardUseWayEnum, SysSettingAward> unUseCard = new LinkedHashMap<>();
        List<SysLuckyChance> sysLuckyChances = luckyDrawHelper.unUseLuckyChance(buyerNick);

        Map<AwardUseWayEnum, List<SysLuckyChance>> allCards = sysLuckyChances.stream().collect(Collectors.groupingBy(SysLuckyChance::getCardType));
        for (SysSettingAward award : exchangeAward) {
            award.setHaveGetNum(0);
            List<SysLuckyChance> cards = allCards.get(award.getUseWay());
            if (!CollectionUtils.isEmpty(cards)) {
                award.setHaveGetNum(cards.size());
            }
        }

        exchangeAward.stream().sorted(Comparator.comparing(SysSettingAward::getUseWay)).forEach(x -> {
            unUseCard.put(x.getUseWay(), x);
        });
        resultMap.put("card_show", unUseCard);

        resultMap.put("draw_log", luckyDrawHelper.drawLog());
        return YunReturnValue.ok(resultMap, "游戏首页");
    }
}




