package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.entity.SysPagePvLog;
import com.duomai.project.product.general.entity.SysSettingAward;
import com.duomai.project.product.general.enums.*;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.product.general.repository.SysPagePvLogRepository;
import com.duomai.project.product.general.repository.SysSettingAwardRepository;
import com.duomai.project.tool.ProjectTools;
import org.apache.commons.lang3.StringUtils;
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
public class GameIndexLoadExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;

    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Autowired
    private SysSettingAwardRepository sysSettingAwardRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;

    @Autowired
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        String inviter = jsonObjectAdmjson.getString("inviter");
        InviteTypeEnum inviteType = ProjectTools.enumValueOf(InviteTypeEnum.class, jsonObjectAdmjson.getString("inviteType"));
        if (StringUtils.isNotBlank(inviter)) {
            Assert.notNull(inviteType, "邀请类型不能为空");
        }

        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");



        /*保存pv*/
        sysPagePvLogRepository.save(new SysPagePvLog()
                .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                .setCreateTime(sysParm.getRequestStartTime())
                .setId(sysParm.getApiParameter().getCommomParameter().getIp())
                .setPage(PvPageEnum.PAGE_INDEX));


        List<SysLuckyChance> sysLuckyChances = new ArrayList<>();
        boolean actLive = projectHelper.actTimeValidateFlag();
        if (actLive) {
            if (StringUtils.isNotBlank(inviter)) {
                SysCustom inviterCustom = sysCustomRepository.findByBuyerNick(inviter);
                resultMap.put("alter_for_invitee_flag", true);
                resultMap.put("alter_for_inviter_name", inviterCustom.getZnick());
                resultMap.put("alter_for_inviter_img", inviterCustom.getHeadImg());
                resultMap.put("alter_for_invitee_type", inviteType.getValue());
                resultMap.put("alter_for_invitee_msg", "我正在参加蒙牛的收集拼图活动，需要1位好友助力，请帮我助力赢大奖~");
            }

            //首次登录游戏免费送一次
            long l = luckyDrawHelper.countLuckyChanceFrom(buyerNick, LuckyChanceFromEnum.FREE);
            if (l == 0) {
                int getNum = 1;
                luckyDrawHelper.sendCard(buyerNick, LuckyChanceFromEnum.FREE, getNum,
                        "恭喜你！免费登陆成功");
            }


            //获得未使用的所有卡牌
            sysLuckyChances = luckyDrawHelper.unUseLuckyChance(buyerNick);
            List<SysLuckyChance> jigsaw = luckyDrawHelper.jigsawCheck(sysLuckyChances);
            if (!CollectionUtils.isEmpty(jigsaw)) {

                List<SysSettingAward> thisTimeAwardPool = sysSettingAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.JIGSAW);

                long l1 = sysLuckyDrawRecordRepository.countByPlayerBuyerNickAndAwardId(buyerNick, thisTimeAwardPool.get(0).getId());
                if (l1 == 0) {
                    SysSettingAward winAward = luckyDrawHelper.luckyDraw(thisTimeAwardPool, jigsaw,
                            syscustom, sysParm.getRequestStartTime());

                    /*只反馈有效数据*/
                    resultMap.put("jigsaw_win", !Objects.isNull(winAward));
                    resultMap.put("jigsaw_award", winAward);
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
                }
            }
        }


        //1.活动规则
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        actBaseSettingDto.setDrawCouponNum(null);
        actBaseSettingDto.setTaskBrowseShouldSee(null);
        resultMap.put("game_rule", actBaseSettingDto);

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
            award.setMaxCanGet(null);
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
        resultMap.put("award_show", otherAward);

        Map<AwardUseWayEnum, List<SysLuckyChance>> allCards = sysLuckyChances.stream().collect(Collectors.groupingBy(SysLuckyChance::getCardType));
        for (SysSettingAward award : exchangeAward) {
            award.setHaveGetNum(0);
            List<SysLuckyChance> cards = allCards.get(award.getUseWay());
            if (!CollectionUtils.isEmpty(cards)) {
                award.setHaveGetNum(cards.size());
            }
        }
        LinkedHashMap<AwardUseWayEnum, SysSettingAward> unUseCard = new LinkedHashMap<>();
        exchangeAward.stream().sorted(Comparator.comparing(SysSettingAward::getUseWay)).forEach(x -> {
            unUseCard.put(x.getUseWay(), x);
        });
        resultMap.put("card_show", unUseCard);

        return YunReturnValue.ok(resultMap, "游戏首页");
    }
}




