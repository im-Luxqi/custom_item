package com.duomai.project.product.mengniuwawaji.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.MemcacheTools;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActTreeWinDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysCustomRanking;
import com.duomai.project.product.general.entity.SysSettingAward;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.repository.SysCustomRankingRepository;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.product.general.repository.SysSettingAwardRepository;
import com.duomai.project.tool.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * //点亮圣诞树
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameWinTreeExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Resource
    private SysCustomRankingRepository sysCustomRankingRepository;
    @Resource
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Resource
    private SysSettingAwardRepository sysSettingAwardRepository;
    @Resource
    private LuckyDrawHelper luckyDrawHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {


        projectHelper.actTimeValidate();
        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        ActTreeWinDto actTreeWinDto = projectHelper.treeWinSettingFind();
        Date requestStartTime = sysParm.getRequestStartTime();
        Assert.isTrue(requestStartTime.after(actTreeWinDto.getTimeTreeLimit()), actTreeWinDto.getTimeTreeLimit() + "再来吧");
        Assert.isTrue(syscustom.getStarValue() >= actTreeWinDto.getStarValueTreeLimit(), "星愿值要满足" + actTreeWinDto.getStarValueTreeLimit());


//        long hasGet = sysLuckyDrawRecordRepository.countByPlayerBuyerNickAndLuckyChance(buyerNick, "_ranging");
        long l = sysCustomRankingRepository.countByBuyerNick(buyerNick);

//        Assert.isTrue(hasGet == 0, "已经点亮过了");
        Assert.isTrue(l == 0, "已经点亮过了");

        SysCustomRanking sysCustomRanking = new SysCustomRanking();
        sysCustomRanking.setBuyerNick(buyerNick);
        sysCustomRanking.setHeadImg(syscustom.getHeadImg());
        sysCustomRanking.setZnick(syscustom.getZnick());
        SysCustomRanking temp = sysCustomRankingRepository.save(sysCustomRanking);
        String property = ApplicationUtils.getContext().getEnvironment().getProperty("spring.profiles.active");
        String rankingKey = property + "ranking_value_xxx";
        Object ranking_value_xxx = MemcacheTools.loadData(rankingKey);
        long rankingValue = 0;
        if (ranking_value_xxx != null) {
            long tempRank = (long) ranking_value_xxx;
            if (tempRank > 10000) {
                rankingValue = tempRank;
                MemcacheTools.cacheData(rankingKey, tempRank, 1000);
            } else {
                rankingValue = sysCustomRankingRepository.rankingWhere(temp.getId());
                MemcacheTools.cacheData(rankingKey, rankingValue, 1000);
            }
        } else {
            rankingValue = sysCustomRankingRepository.rankingWhere(temp.getId());
            MemcacheTools.cacheData(rankingKey, rankingValue, 1000);
        }


        String ls = "," + rankingValue + ",";
        List<SysSettingAward> awards = null;
        //1.发放DREAM IT REAL 限量Tote包（1,1225）
        if (actTreeWinDto.getTreeAwardOne().contains(ls)) {
            awards = sysSettingAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.RANKING1);
        } else if (actTreeWinDto.getTreeAwardTwo().contains(ls)) {
            awards = sysSettingAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.RANKING2);
        } else if (actTreeWinDto.getTreeAwardThree().contains(ls)) {
            awards = sysSettingAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.RANKING3);
        } else if (actTreeWinDto.getTreeAwardFour().contains(ls)) {
            awards = sysSettingAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.RANKING4);
        } else if (rankingValue <= 7000 && (rankingValue % 50 == 0)) {
            awards = sysSettingAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.RANKING5);
        } else if (rankingValue >= 8000 && rankingValue <= 10000 && (rankingValue % 200 == 0)) {
            awards = sysSettingAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.RANKING5);
        } else {
            awards = sysSettingAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.RANKING6);
        }

        SysSettingAward winAward = luckyDrawHelper.luckyDraw(awards, syscustom, requestStartTime, "_ranging");
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();

        temp.setWinAwardId(awards.get(0).getId());
        temp.setWinAwardName(awards.get(0).getName());
        temp.setWinAwardRank((int) rankingValue);
        temp.setSendSuccess(!Objects.isNull(winAward) ? BooleanConstant.BOOLEAN_YES : BooleanConstant.BOOLEAN_NO);
        sysCustomRankingRepository.save(temp);

        /*只反馈有效数据*/
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
                    .setPoolLevel(null);
        }
        return YunReturnValue.ok(resultMap, "点亮圣诞树");
    }
}




