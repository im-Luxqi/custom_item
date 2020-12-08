package com.duomai.project.product.mengniuwawaji.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.CoachConstant;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import com.duomai.project.product.general.enums.PlayActionEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * //2.8  使用 letter_party2邀请函
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameUseLetterParty2Execute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Resource
    private ITaobaoAPIService taobaoAPIService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        projectHelper.actTimeValidate();
        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        boolean member = taobaoAPIService.isMember(buyerNick);
        Assert.isTrue(member, "不是会员");
        if (syscustom.getMemberWayFrom().equals(MemberWayFromEnum.NON_MEMBER)) {
            syscustom.setMemberWayFrom(MemberWayFromEnum.NATURE_JOIN_MEMBER);
            syscustom.setStarValue(syscustom.getStarValue()+ CoachConstant.joinmember_xingyuan);
        }
        if ("party1".equals(syscustom.getPlayParty()) && syscustom.getCurrentAction().equals(PlayActionEnum.letter_party2)) {
            syscustom.setPlayParty("party1,party2");
            syscustom.setCurrentAction(PlayActionEnum.playwith_lamp);
        }
        sysCustomRepository.save(syscustom);
        return YunReturnValue.ok("使用letter_party2邀请函");
    }
}




