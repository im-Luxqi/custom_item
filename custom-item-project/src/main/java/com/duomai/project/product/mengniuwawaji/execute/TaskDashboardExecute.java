package com.duomai.project.product.mengniuwawaji.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.FinishTheTaskHelper;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysTaskDailyBoard;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 任务页load
 *
 * @author 王星齐
 * @description
 * @create 2020/11/21 20:31
 */
@Component
public class TaskDashboardExecute implements IApiExecute {
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private FinishTheTaskHelper finishTheTaskHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Date today = sysParm.getRequestStartTime();
        ActBaseSettingDto config = projectHelper.actBaseSettingFind();
        boolean taskFinish = today.after(config.getActEndTime());
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();

        SysTaskDailyBoard dailyBoard = finishTheTaskHelper.todayTaskBoard(buyerNick);

        List<Map<String, Object>> resultList = new ArrayList<>();
        //1 签到
        Map<String, Object> signMap = new LinkedHashMap<>();
        signMap.put("task", LuckyChanceFromEnum.SIGN);
        signMap.put("title", "每日打卡");
        signMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon0.png");
        signMap.put("explain", "用户累计签到一定天数，有机会获得一定拼图碎片");
        signMap.put("finish", BooleanConstant.BOOLEAN_YES.equals(dailyBoard.getHaveFinishSignToday()));
        signMap.put("task_end", taskFinish);
        signMap.put("sign_condition", config.getTaskSign());
        signMap.put("sign_total_num", dailyBoard.getSignTotalNum());
        resultList.add(signMap);


        //2.关注
        Map<String, Object> attentionMap = new LinkedHashMap<>();
        attentionMap.put("task", LuckyChanceFromEnum.FOLLOW);
        attentionMap.put("title", "关注店铺");
        attentionMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon1.png");
        attentionMap.put("explain", "成功关注店铺，可以获得1块拼图碎片。");
        attentionMap.put("finish", BooleanConstant.BOOLEAN_YES.equals(dailyBoard.getHaveFinishFollow()));
        attentionMap.put("task_end", taskFinish);
        resultList.add(attentionMap);

        //3.成为会员
        Map<String, Object> membershipMap = new LinkedHashMap<>();
        membershipMap.put("task", LuckyChanceFromEnum.MEMBER);
        membershipMap.put("title", "加入会员");
        membershipMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon0.png");
        membershipMap.put("explain", "加入会员可获得1块碎片");
        membershipMap.put("finish", BooleanConstant.BOOLEAN_YES.equals(dailyBoard.getHaveFinishMember()));
        membershipMap.put("task_end", taskFinish);
        resultList.add(membershipMap);


        //4.分享任务
        Map<String, Object> shareMap = new LinkedHashMap<>();
        shareMap.put("task", LuckyChanceFromEnum.SHARE);
        shareMap.put("title", "分享活动");
        shareMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon2.png");
        shareMap.put("explain", "用户每成功邀请5位好友参加活动，有机会获得1块拼图碎片");
        shareMap.put("finish", false);
        shareMap.put("progress", dailyBoard.getShareProgress());
        shareMap.put("task_end", taskFinish);
        resultList.add(shareMap);


        //5.邀请好友关注店铺
        Map<String, Object> shareFollowMap = new LinkedHashMap<>();
        shareFollowMap.put("task", LuckyChanceFromEnum.SHARE_FOLLOW);
        shareFollowMap.put("title", "邀请好友关注店铺");
        shareFollowMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon2.png");
        shareFollowMap.put("explain", "用户每日成功邀请3位好友关注店铺，有机会获得1块拼图碎片");
        shareFollowMap.put("finish", false);
        shareFollowMap.put("progress", dailyBoard.getInviteFollowProgress());
        shareFollowMap.put("task_end", taskFinish);
        resultList.add(shareFollowMap);


        //6.邀请好友加入会员
        Map<String, Object> shareMemberMap = new LinkedHashMap<>();
        shareMemberMap.put("task", LuckyChanceFromEnum.SHARE_MEMBER);
        shareMemberMap.put("title", "邀请好友加入会员");
        shareMemberMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon2.png");
        shareMemberMap.put("explain", "用户每成功邀请1位好友注册成为会员，有机会获得1块拼图碎片");
        shareMemberMap.put("finish", false);
        shareMemberMap.put("progress", dailyBoard.getInviteFollowProgress());
        shareMemberMap.put("task_end", taskFinish);
        resultList.add(shareMemberMap);


        //7.每日浏览宝贝
        Map<String, Object> browseMap = new LinkedHashMap<>();
        browseMap.put("task", LuckyChanceFromEnum.BROWSE);
        browseMap.put("title", "每日浏览宝贝");
        browseMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon3.png");
        browseMap.put("explain", "用户每日浏览至少3款商品，浏览时长至少10s，有机会获得1块拼图碎片");
        browseMap.put("finish", BooleanConstant.BOOLEAN_YES.equals(dailyBoard.getHaveFinishBrowseToday()));
        browseMap.put("task_end", taskFinish);
        browseMap.put("progress", dailyBoard.getBrowseProgress());
        resultList.add(browseMap);


        //8. 看直播
        long tv = luckyDrawHelper.countTodayLuckyChanceFrom(buyerNick, LuckyChanceFromEnum.TV);
        Map<String, Object> tvMap = new LinkedHashMap<>();
        tvMap.put("task", LuckyChanceFromEnum.TV);
        tvMap.put("title", "观看直播");
        tvMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon4.png");
        tvMap.put("explain", "用户每日观看直播至少15s，有机会获得1块拼图碎片");
        tvMap.put("finish", BooleanConstant.BOOLEAN_YES.equals(dailyBoard.getHaveFinishTvToday()));
        tvMap.put("task_end", taskFinish);
        resultList.add(tvMap);

        //9.消费赠送
        Map<String, Object> orderMap = new LinkedHashMap<>();
        orderMap.put("task", LuckyChanceFromEnum.ORDER);
        orderMap.put("title", "每日消费" + dailyBoard.getSpendProgress());
        orderMap.put("icon", "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/otherindex/duteIcon6.png");
        orderMap.put("explain", "用户购买指定商品可获得5块拼图碎片，每天最多通过该任务获得15块拼图碎片");
        orderMap.put("finish", BooleanConstant.BOOLEAN_YES.equals(dailyBoard.getHaveFinishSpendToday()));
        orderMap.put("task_end", taskFinish);
        resultList.add(orderMap);
        return YunReturnValue.ok(resultList, "任务页面");
    }
}
