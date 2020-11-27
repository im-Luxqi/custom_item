package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysTaskMemberOrFollowLog;
import com.duomai.project.product.general.enums.FollowWayFromEnum;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.enums.TaskTypeEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysTaskMemberOrFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @内容：任务页面 关注
 * @创建人：lyj
 * @创建时间：2020.9.30
 */
@Component
public class TaskFollowExecute implements IApiExecute {
    @Autowired
    private SysTaskMemberOrFollowRepository sysTaskMemberOrFollowRepository;
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

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
        //校验是否已完成关注任务

        long memberTime = sysTaskMemberOrFollowRepository.countByBuyerNickAndTaskType(buyerNick
                , TaskTypeEnum.FOLLOW);
        Assert.isTrue(memberTime == 0, "关注任务已完成");
        /*2.关注动作*/
        sysTaskMemberOrFollowRepository.save(new SysTaskMemberOrFollowLog()
                .setBuyerNick(buyerNick)
                .setCreateTime(sysParm.getRequestStartTime())
                .setTaskType(TaskTypeEnum.FOLLOW));
        if (FollowWayFromEnum.NON_FOLLOW.equals(syscustom.getFollowWayFrom())) {
            syscustom.setFollowWayFrom(FollowWayFromEnum.NATURE_JOIN_FOLLOW);
            sysCustomRepository.save(syscustom);
        }

        /*3完成任务，获取奖励*/
        Integer thisGet = 1;
        luckyDrawHelper.sendLuckyChance(buyerNick, LuckyChanceFromEnum.FOLLOW, thisGet,
                "关注", "关注店铺，获得" + thisGet + "次游戏机会");
        return YunReturnValue.ok("完成关注任务！");
    }
}
