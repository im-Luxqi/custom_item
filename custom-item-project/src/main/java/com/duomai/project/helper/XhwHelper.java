package com.duomai.project.helper;

import com.duomai.common.constants.BooleanConstant;
import com.duomai.project.configuration.annotation.JoinMemcache;
import com.duomai.project.product.general.dto.XhwSettingDto;
import com.duomai.project.product.general.entity.*;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.CommonDateParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 排行榜 常规操作
 *
 * @description 【帮助类目录】
 * 1.查询排行榜
 * 2.实时查询，当前玩家在本次排行榜活动中的排名
 * @create by 王星齐
 * @date 2020-08-26 16:52
 */
@Slf4j
@Component
public class XhwHelper {
    @Autowired
    private XhwCustomRepository xhwCustomRepository;
    @Autowired
    private XhwGroupRepository xhwGroupRepository;
    @Autowired
    private XhwSettingRepository xhwSettingRepository;

    @Autowired
    private XhwAwardRepository xhwAwardRepository;
    @Autowired
    private XhwAwardRecordRepository xhwAwardRecordRepository;


    public XhwCustom findCustom(String buyerNick) {
        XhwCustom byBuyerNick = xhwCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(byBuyerNick, "无效的参与者");
        byBuyerNick.setNewGuy(false);
        return byBuyerNick;
    }

    @Transactional
    public XhwCustom findCustom(String buyerNick, String ip) {

        if (StringUtils.isNotBlank(buyerNick)) {
            XhwCustom byBuyerNick = xhwCustomRepository.findByBuyerNick(buyerNick);
            Assert.notNull(byBuyerNick, "无效的参与者");
            byBuyerNick.setNewGuy(false);
            return byBuyerNick;
        }

        XhwGroup group = xhwGroupRepository.findFirstByFinish(0);
        if (group == null) {
            List<XhwGroup> all = xhwGroupRepository.findAll();
            group = all.get(new Random().nextInt(all.size()));
        } else {
            int remain = group.getMaxNum() - 1;
            group.setMaxNum(remain);
            if (remain == 0) {
                group.setFinish(1);
            }
            xhwGroupRepository.save(group);
        }

        Date date = new Date();
        String dateString = CommonDateParseUtil.date2string(date, CommonDateParseUtil.YYYY_MM_DD_HH_MM_SS);
        XhwCustom custom = new XhwCustom()
                .setBuyerNick(UUID.randomUUID() + dateString)
                .setCreateTime(date)
                .setUpdateTime(date)
                .setNewGuy(true)
                .setIp(ip)
                .setGroupChat(group.getQrCode());

        XhwSetting joinNum = xhwSettingRepository.findFirstByK("join_num");
        int i = Integer.parseInt(joinNum.getV()) + 1;
        joinNum.setV(i + "");
        xhwSettingRepository.save(joinNum);
        return xhwCustomRepository.save(custom);
    }

    /**
     * 2.活动配置--信息获取
     *
     * @description
     * @create by 王星齐
     * @time 2020-08-26 20:03:20
     */
    @JoinMemcache()
    public XhwSettingDto findSetting() {
        List<XhwSetting> byType = xhwSettingRepository.findByType("xhw_setting");
        Map<String, String> collect = byType.stream().collect(Collectors.toMap(XhwSetting::getK, XhwSetting::getV));
        XhwSettingDto xhwSettingDto = new XhwSettingDto();
        xhwSettingDto.setActRule(collect.get("act_rule"));
        xhwSettingDto.setVirtualNum(collect.get("virtual_num"));
        return xhwSettingDto;
    }

    public Integer findJoinNum() {
        XhwSetting joinNum = xhwSettingRepository.findFirstByK("join_num");
        return Integer.valueOf(joinNum.getV());
    }


    @Transactional
    public XhwAward draw(String awardId, XhwCustom custom, Date drawTime, String ip) throws Exception {

        Optional<XhwAward> byId = xhwAwardRepository.findById(awardId);
        Assert.isTrue(byId.isPresent(), "不存在的奖品");
        XhwAward award = byId.get();
        if (xhwAwardRepository.tryReduceOne(award.getId()) != 1) {
            return award;
        }
        String dateString = CommonDateParseUtil.date2string(drawTime, CommonDateParseUtil.YYYY_MM_DD);
        XhwAwardRecord awardRecord = new XhwAwardRecord()
                .setDrawTime(drawTime)
                .setDrawTimeString(dateString)
                .setBuyerNick(custom.getBuyerNick())
                .setAwardId(award.getId())
                .setAwardName(award.getName())
                .setAwardImg(award.getImg())
                .setIsFill(BooleanConstant.BOOLEAN_NO)
                .setIp(ip);
        XhwAwardRecord save = xhwAwardRecordRepository.save(awardRecord);
        award.setLogId(save.getId());
        return award;
    }

    public Object drawLog() {
        return xhwAwardRecordRepository.queryLuckyDrawLog();
    }
}