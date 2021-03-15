package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.FinishTheTaskHelper;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysTaskDailyBoard;
import com.duomai.project.product.general.entity.SysTaskMemberOrFollowLog;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import com.duomai.project.product.general.enums.TaskTypeEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysTaskMemberOrFollowRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @内容：任务页面 入会
 * @创建人：lyj
 * @创建时间：2020.9.30
 */
@Component
public class TaskMemberExecute implements IApiExecute {
    @Autowired
    private SysTaskMemberOrFollowRepository sysTaskMemberOrFollowRepository;
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private FinishTheTaskHelper finishTheTaskHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {


        /*1.校验*/
        //活动只能再活动期间
        projectHelper.actTimeValidate();
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        Assert.isTrue(BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveAuthorization()), "请先授权");
        //校验是否已完成入会任务

        long memberTime = sysTaskMemberOrFollowRepository.countByBuyerNickAndTaskType(buyerNick
                , TaskTypeEnum.MEMBER);
        Assert.isTrue(memberTime == 0, "入会任务已完成");
        /*2.入会动作*/
        sysTaskMemberOrFollowRepository.save(new SysTaskMemberOrFollowLog()
                .setBuyerNick(buyerNick)
                .setCreateTime(sysParm.getRequestStartTime())
                .setMemberOrFollowTime(CommonDateParseUtil.date2string(sysParm.getRequestStartTime(),"yyyy-MM-dd"))
                .setTaskType(TaskTypeEnum.MEMBER));
        if (MemberWayFromEnum.NON_MEMBER.equals(syscustom.getMemberWayFrom())) {
            syscustom.setMemberWayFrom(MemberWayFromEnum.NATURE_JOIN_MEMBER);
            sysCustomRepository.save(syscustom);
        }

        /*3完成任务，获取奖励*/
        Integer thisGet = 1;
        luckyDrawHelper.sendCard(syscustom, LuckyChanceFromEnum.MEMBER, thisGet,
                "入会成功，获得【有料品鉴官】一博送你的食力拼图*" + thisGet);

        SysTaskDailyBoard taskDailyBoard = finishTheTaskHelper.todayTaskBoard(buyerNick);
        taskDailyBoard.setHaveFinishMember(BooleanConstant.BOOLEAN_YES);
        finishTheTaskHelper.updateTaskBoard(taskDailyBoard);

        return YunReturnValue.ok("完成入会任务！");
    }
}
