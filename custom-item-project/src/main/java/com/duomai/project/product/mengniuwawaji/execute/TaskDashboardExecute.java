package com.duomai.project.product.mengniuwawaji.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysTaskMemberOrFollowLog;
import com.duomai.project.product.general.entity.SysTaskSignLog;
import com.duomai.project.product.general.enums.TaskTypeEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.product.general.repository.SysTaskMemberOrFollowRepository;
import com.duomai.project.product.general.repository.SysTaskSignLogRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/** 任务页load
 * @author 王星齐
 * @description
 * @create 2020/11/21 20:31
 */
@Component
public class TaskDashboardExecute implements IApiExecute {
    @Autowired
    private SysTaskSignLogRepository sysTaskSignLogRepository;
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private SysTaskMemberOrFollowRepository sysTaskMemberOrFollowRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Date today = sysParm.getRequestStartTime();
        ActBaseSettingDto config = projectHelper.actBaseSettingFind();
        boolean taskFinish = today.after(config.getActEndTime());


        List<Map<String, Object>> resultList = new ArrayList<>();
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();


        //1 签到
        String todayString = CommonDateParseUtil.date2string(today, "yyyy-MM-dd");
        List<SysTaskSignLog> taskSignLogList = sysTaskSignLogRepository.findByBuyerNickOrderBySignTimeAsc(buyerNick);
        SysTaskSignLog lastLog = null;
        boolean todaySign = false;
        if (taskSignLogList.size() > 0) {
            SysTaskSignLog sysTaskSignLog = taskSignLogList.get(taskSignLogList.size() - 1);
            if (todayString.equals(sysTaskSignLog.getSignTime())) {
                lastLog = sysTaskSignLog;
                todaySign = true;
            } else {
                Date yesterday = CommonDateParseUtil.addDay(today, -1);
                String yesterdayString = CommonDateParseUtil.date2string(yesterday, "yyyy-MM-dd");
                if (yesterdayString.equals(sysTaskSignLog.getSignTime())) {
                    lastLog = sysTaskSignLog;
                }
            }
            taskSignLogList.forEach(x->{
                x.setId(null);
                x.setCreateTime(null);
                x.setBuyerNick(null);
            });
        }
        Map<String, Object> signMap = new LinkedHashMap<>();
        signMap.put("task", "sign");
        signMap.put("title", "每日签到");
        signMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon0.png");
        signMap.put("explain", "游戏次数+2，活动期间1次");
        signMap.put("finish", todaySign);
        signMap.put("task_end", taskFinish);
        signMap.put("sign_list", taskSignLogList);
        signMap.put("sign_continuous", lastLog == null ? 0 : lastLog.getContinuousNum());
        resultList.add(signMap);


        //2.成为会员
        List<SysTaskMemberOrFollowLog> memberAndFollow = sysTaskMemberOrFollowRepository.findByBuyerNick(buyerNick);
        boolean finishMember = false;
        boolean finishFollow = false;
        if (!CollectionUtils.isEmpty(memberAndFollow)) {
            for (SysTaskMemberOrFollowLog sysTaskMemberOrFollowLog : memberAndFollow) {
                if (TaskTypeEnum.FOLLOW.equals(sysTaskMemberOrFollowLog.getTaskType())) {
                    finishFollow = true;
                }
                if (TaskTypeEnum.MEMBER.equals(sysTaskMemberOrFollowLog.getTaskType())) {
                    finishMember = true;
                }
            }
        }
        Map<String, Object> membershipMap = new LinkedHashMap<>();
        membershipMap.put("task", "membership");
        membershipMap.put("title", "加入会员");
        membershipMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon0.png");
        membershipMap.put("explain", "游戏次数+2，活动期间1次");
        membershipMap.put("finish", finishMember);
        membershipMap.put("task_end", taskFinish);
        resultList.add(membershipMap);


        //3.关注
        Map<String, Object> attentionMap = new LinkedHashMap<>();
        attentionMap.put("task", "follow");
        attentionMap.put("title", "关注店铺");
        attentionMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon1.png");
        attentionMap.put("explain", "游戏次数+1，活动期间1次");
        attentionMap.put("finish", finishFollow);
        attentionMap.put("task_end", taskFinish);
        resultList.add(attentionMap);


        //4.分享任务
        Map<String, Object> shareMap = new LinkedHashMap<>();
        shareMap.put("task", "share");
        shareMap.put("title", "分享活动");
        shareMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon2.png");
        shareMap.put("explain", "游戏次数+1");
        shareMap.put("finish", false);
        shareMap.put("task_end", taskFinish);
        resultList.add(shareMap);

        //5.浏览商品
        Map<String, Object> browseMap = new LinkedHashMap<>();
        browseMap.put("task", "browse");
        browseMap.put("title", "浏览商品");
        browseMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon3.png");
        browseMap.put("explain", "浏览纯甄商品，游戏次数+1");
        browseMap.put("finish", false);
        browseMap.put("task_end", taskFinish);
//        browseMap.put("progress", browseCount + "/3");
        resultList.add(browseMap);

        //6. 看直播
        //        luckyDrawHelper.countTodayLuckyChanceFrom(buyerNick, LuckyChanceFromEnum.BROWSE);
        Map<String, Object> tvMap = new LinkedHashMap<>();
        tvMap.put("task", "tv");
        tvMap.put("title", "观看直播");
        tvMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon4.png");
        tvMap.put("explain", "游戏次数+1，每日1次");
        tvMap.put("finish", false);
        tvMap.put("task_end", taskFinish);
        resultList.add(tvMap);


        //7. 邀请加入会员
        Map<String, Object> inviteMap = new LinkedHashMap<>();
        inviteMap.put("task", "invite");
        inviteMap.put("title", "邀请加入会员");
        inviteMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon5.png");
        inviteMap.put("explain", "游戏次数+1，受邀者须为从未注册加入蒙牛的全新会员");
        inviteMap.put("finish", false);
        inviteMap.put("task_end", taskFinish);
//        inviteMap.put("invite_progress", inviteCount == 0 ? 0 : ((inviteCount % 3) == 0 ? 3 : (inviteCount % 3)) + "/3");
        resultList.add(inviteMap);


        //8.消费赠送
        Map<String, Object> orderMap = new LinkedHashMap<>();
        orderMap.put("task", "order");
        orderMap.put("title", "消费赠送");
        orderMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon6.png");
        orderMap.put("explain", "购买纯甄系列产品，单笔订单实付金额达到99元及以上，游戏次数+3");
        orderMap.put("finish", false);
        orderMap.put("task_end", taskFinish);
//        orderMap.put("gold_deposit_progress", orderCount + "/10");
        resultList.add(orderMap);
        return YunReturnValue.ok(resultList, "任务页面");
    }
}
