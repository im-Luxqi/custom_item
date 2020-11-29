package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.FollowWayFromEnum;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 玩家信息扫描，首次完成初始化操作
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 */
@Component
public class PlayerInfoScanOrInitExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Resource
    private ITaobaoAPIService taobaoAPIService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.查询玩家信息,未查询到初始化玩家信息*/
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
        if (sysCustom == null) {
            //保存粉丝信息
            boolean historyMember = taobaoAPIService.isMember(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
            SysCustom temp = new SysCustom().setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                    .setCreateTime(sysParm.getRequestStartTime())
                    .setOpenId(sysParm.getApiParameter().getYunTokenParameter().getOpenUId())
                    .setHaveAuthorization(BooleanConstant.BOOLEAN_NO)
                    .setMemberWayFrom(historyMember ? MemberWayFromEnum.HISTROY_MEMBER : MemberWayFromEnum.NON_MEMBER)
                    .setFollowWayFrom(FollowWayFromEnum.UNDIFIND);
            sysCustom = sysCustomRepository.save(temp);
        }


//        else if (MemberWayFromEnum.NON_MEMBER.equals(sysCustom.getMemberWayFrom())) {
//            boolean historyMember = taobaoAPIService.isMember(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
//            if (historyMember) {
//                sysCustom.setMemberWayFrom(MemberWayFromEnum.HISTROY_MEMBER);
//                sysCustom = sysCustomRepository.save(sysCustom);
//            }
//        }

        /*2.组织返回值*/
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        boolean should_sure_history_follow_state = FollowWayFromEnum.UNDIFIND.equals(sysCustom.getFollowWayFrom());
        boolean have_authorization = BooleanConstant.BOOLEAN_YES.equals(sysCustom.getHaveAuthorization());
        resultMap.put("buyer_nick", sysCustom.getBuyerNick());
        resultMap.put("znick", sysCustom.getZnick());
        resultMap.put("history_follow_undefined", should_sure_history_follow_state);
        resultMap.put("have_authorization", have_authorization);
//        resultMap.put("hava_join_member", !MemberWayFromEnum.NON_MEMBER.equals(sysCustom.getMemberWayFrom()));
        return YunReturnValue.ok(resultMap, "获取玩家信息," +
                (should_sure_history_follow_state ? "【提示：】history_follow_undefined = true ---> 首次进入活动，需要调用接口(wx.dz.common.playerInfo.fill.historyFollow)。!!!!!!!" : "") +
                (!have_authorization ? "【提示：】have_authorization = false ---> 表示当前玩家当前尚未授权。" : "")
        );
    }
}