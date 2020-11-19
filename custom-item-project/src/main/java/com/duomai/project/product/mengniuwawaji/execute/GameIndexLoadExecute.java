package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 游戏首页 加载
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
public class GameIndexLoadExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();

        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");


        /*2.被邀请助力或者被邀请入会*/
        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        String sharer = jsonObjectAdmjson.getString("sharer");
        String inviter = jsonObjectAdmjson.getString("inviter");
        //邀请助力
        if (StringUtils.isNotBlank(sharer) && !sharer.equals(buyerNick)) {
            //同一个人每天只能为他人助力一次


        }

        //邀请入会
        if (StringUtils.isNotBlank(inviter) && !inviter.equals(buyerNick)) {
            //您已是店铺会员，无法为好友助力
            if(!MemberWayFromEnum.NON_MEMBER.equals(syscustom.getMemberWayFrom())){
                resultMap.put("alter_for_invitee_flag",false);
                resultMap.put("alter_for_invitee_msg","您已是店铺会员，无法为好友助力");
            }else{

            }
        }


        //1.活动规则
        resultMap.put("game_rule","");
        //2.抓娃娃机会次数
        resultMap.put("lucky_chance_num","");
        //3.我的战利品
        resultMap.put("lucky_win_bottle","");
        //4.兑换弹幕
        resultMap.put("lucky_exchange_barrage","");
        return null;
    }
}




