package com.duomai.project.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.project.api.taobao.MemcacheTools;
import com.duomai.project.configuration.annotation.JoinMemcache;
import com.duomai.project.product.general.dto.XhwSettingDto;
import com.duomai.project.product.general.entity.*;
import com.duomai.project.product.general.enums.AwardRunningEnum;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.CommonDateParseUtil;
import com.duomai.project.tool.ProjectTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

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

    static String joinNumKey = "xinghuangwang_join";

    public XhwCustom findCustom(String buyerNick) {
        XhwCustom byBuyerNick = xhwCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(byBuyerNick, "无效的参与者");
        byBuyerNick.setNewGuy(false);
        return byBuyerNick;
    }

    @Transactional
    public XhwCustom findCustom(String buyerNick, String ip) throws InterruptedException {

        if (StringUtils.isNotBlank(buyerNick)) {
            XhwCustom byBuyerNick = xhwCustomRepository.findByBuyerNick(buyerNick);
            Assert.notNull(byBuyerNick, "无效的参与者");
            byBuyerNick.setNewGuy(false);
            return byBuyerNick;
        }
//        XhwGroup group = xhwGroupRepository.findFirstByFinish(0);
//        if (group == null) {
//            List<XhwGroup> all = xhwGroupRepository.findAll();
//            group = all.get(new Random().nextInt(all.size()));
//        } else {
//            int remain = group.getRemainNum() - 1;
//            group.setRemainNum(remain);
//            if (remain == 0) {
//                group.setFinish(1);
//            }
//            xhwGroupRepository.save(group);
//        }
        List<XhwGroup> all = findAllGroup();
        XhwGroup group = all.get(new Random().nextInt(all.size()));


        Date date = new Date();
        String dateString = CommonDateParseUtil.date2string(date, "yyyyMMddHHmmss");
        XhwCustom custom = new XhwCustom()
                .setBuyerNick(UUID.randomUUID() + dateString)
                .setCreateTime(date)
                .setUpdateTime(date)
                .setNewGuy(true)
                .setIp(ip)
                .setGroupChat(group.getQrCode());

        findJoinNum();
        MemcacheTools.addIncr(joinNumKey);
//        XhwSetting joinNum = xhwSettingRepository.findFirstByK("join_num");
//        int i = Integer.parseInt(joinNum.getV()) + 1;
//        joinNum.setV(i + "");
//        xhwSettingRepository.save(joinNum);
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


    @JoinMemcache()
    public List<XhwGroup> findAllGroup() {
        List<XhwGroup> all = xhwGroupRepository.findAll();
        return all;
    }


    public Integer findJoinNum() throws InterruptedException {

        if (!ProjectTools.hasMemCacheEnvironment()) {
            long count = xhwCustomRepository.count();
            int c = (int) count;
            return c;
        }


        Integer xinghuangwang_join = MemcacheTools.<Integer>loadData(joinNumKey);
        if (xinghuangwang_join == null) {
            if (MemcacheTools.add(joinNumKey + "_lock_", 1000)) {
                long count = xhwCustomRepository.count();
                int c = (int) count;
                MemcacheTools.cacheData(joinNumKey, c, 100);
                xinghuangwang_join = c;
                MemcacheTools.cleanData(joinNumKey + "_lock_");
            } else {
                Thread.sleep(50);
                return findJoinNum();
            }
        }
        return xinghuangwang_join;
    }


    @Transactional
    public XhwAward draw(String awardId, XhwCustom custom, Date drawTime, String ip) throws Exception {

        Optional<XhwAward> byId = xhwAwardRepository.findById(awardId);
        Assert.isTrue(byId.isPresent(), "不存在的奖品");
        XhwAward award = byId.get();
        Assert.isTrue(AwardRunningEnum.RUNNING.equals(award.getAwardRunningType()), "商品还未开抢");
        if (xhwAwardRepository.tryReduceOne(award.getId()) != 1) {
            award.setAwardRunningType(AwardRunningEnum.FINISH);
            xhwAwardRepository.save(award);
            return award;
        }
        if (award.getRemainNum() == 1) {
            award.setAwardRunningType(AwardRunningEnum.FINISH);
            award.setRemainNum(0);
            xhwAwardRepository.save(award);
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

    public List<String> drawLog(XhwAward hotAward) {
        List<Map> maps = xhwAwardRecordRepository.queryLuckyDrawLog();

        Date currentTime = new Date();
        List<String> collect = new ArrayList<>();
        List<XhwAwardRecord> xhwAwardRecords = JSONArray.parseArray(JSON.toJSONString(maps), XhwAwardRecord.class);
        if (!CollectionUtils.isEmpty(xhwAwardRecords)) {
            collect = xhwAwardRecords.stream().map(xhwAwardRecord -> {
                String recevicePhone = xhwAwardRecord.getRecevicePhone();
                String start = recevicePhone.substring(0, 3);
                String end = recevicePhone.substring(7);

                String msg = start + "****" + end + " 的网友 ";
                long l = currentTime.getTime() - xhwAwardRecord.getReceviceTime().getTime();

                if (l >= (24 * 60 * 60 * 1000)) {
                    msg += l / (24 * 60 * 60 * 1000) + "天";
                } else if (l >= (60 * 60 * 1000)) {
                    msg += l / (60 * 60 * 1000) + "小时";
                } else if (l >= (60 * 1000)) {
                    msg += l / (60 * 1000) + "分钟";
                } else if (l >= (1000)) {
                    msg += l / (1000) + "秒";
                } else {
                    msg += "1秒";
                }
                msg += "前抢中 " + xhwAwardRecord.getAwardName();
                return msg;
            }).collect(Collectors.toList());
        }
        if (collect.size() < 20) {
            List<String> temp = new ArrayList<>();
            List<String> fakeLog = getFakeLog(hotAward);
            for (int i = collect.size(); i < 20; i++) {
                temp.add(fakeLog.get(i));
            }
            temp.addAll(collect);
            return temp;
        }
        return collect;
    }

    @Transactional
    public void finishAward(XhwAward award) {
        award.setAwardRunningType(AwardRunningEnum.FINISH);
        xhwAwardRepository.save(award);
    }

    static List<String> fakeLog;

    static List<String> getFakeLog(XhwAward hotAward) {
        if (!CollectionUtils.isEmpty(fakeLog)) {
            return fakeLog;
        }
        fakeLog = new ArrayList<String>();
        fakeLog.add("133****9659 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("133****3556 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("134****3264 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("134****7019 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("134****1808 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("134****4527 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("134****1836 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("134****0533 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("134****2725 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("134****8869 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("135****6344 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("135****6562 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("135****2042 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("135****7091 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("135****5598 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("135****6525 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("135****6624 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("135****5403 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("136****7990 的网友 1秒前抢中 " + hotAward.getName());
        fakeLog.add("136****3797 的网友 1秒前抢中 " + hotAward.getName());
        return fakeLog;
    }
}