package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.FollowWayFromEnum;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Resource
    private ITaobaoAPIService taobaoAPIService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        String openUId = sysParm.getApiParameter().getYunTokenParameter().getOpenUId();
        Date requestStartTime = sysParm.getRequestStartTime();

        /*1.查询玩家信息,未查询到初始化玩家信息*/
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(buyerNick);
        if (sysCustom == null) {
            //保存粉丝信息
            boolean historyMember = taobaoAPIService.isMember(buyerNick);
            sysCustom = sysCustomRepository.save(new SysCustom()
                    .setBuyerNick(buyerNick)
                    .setCreateTime(requestStartTime)
                    .setOpenId(openUId)
                    .setHaveAuthorization(BooleanConstant.BOOLEAN_NO)
                    .setMemberWayFrom(historyMember ? MemberWayFromEnum.HISTROY_MEMBER : MemberWayFromEnum.NON_MEMBER)
                    .setFollowWayFrom(FollowWayFromEnum.UNDIFIND)
                    .setHaveActionGuide(BooleanConstant.BOOLEAN_NO));
        }


        /*2.组织返回值*/
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        boolean should_sure_history_follow_state = FollowWayFromEnum.UNDIFIND.equals(sysCustom.getFollowWayFrom());
        boolean have_authorization = BooleanConstant.BOOLEAN_YES.equals(sysCustom.getHaveAuthorization());
        resultMap.put("buyer_nick", sysCustom.getBuyerNick());
        resultMap.put("znick", sysCustom.getZnick());
        resultMap.put("history_follow_undefined", should_sure_history_follow_state);
        resultMap.put("have_authorization", have_authorization);
        return YunReturnValue.ok(resultMap, "获取玩家信息," +
                (should_sure_history_follow_state ? "【提示：】history_follow_undefined = true ---> 首次进入活动，需要调用接口(wx.dz.common.playerInfo.fill.historyFollow)。!!!!!!!" : "") +
                (!have_authorization ? "【提示：】have_authorization = false ---> 表示当前玩家当前尚未授权。" : "")
        );
    }
}