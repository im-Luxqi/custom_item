package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.CoachConstant;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysTaskMemberOrFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

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
    private ITaobaoAPIService taobaoAPIService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {


        /*1.校验*/
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        //校验是否已完成入会任务

        boolean member = taobaoAPIService.isMember(buyerNick);
        Assert.isTrue(member, "不是会员");

        if (MemberWayFromEnum.NON_MEMBER.equals(syscustom.getMemberWayFrom())) {
            syscustom.setMemberWayFrom(MemberWayFromEnum.NATURE_JOIN_MEMBER);
            syscustom.setStarValue(syscustom.getStarValue()+ CoachConstant.joinmember_xingyuan);
            sysCustomRepository.save(syscustom);
        }
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        return YunReturnValue.ok(resultMap,"完成入会任务！");
    }
}
