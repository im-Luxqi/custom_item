package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/* 玩家信息扫描，首次完成初始化操作
 * @description
 * @create by 王星齐
 **/
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
            sysCustom = sysCustomRepository.save(
                    new SysCustom().setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                            .setCreateTime(sysParm.getRequestStartTime())
                            .setOpenId(sysParm.getApiParameter().getYunTokenParameter().getOpenUId())
                            .setHistoryFollow(BooleanConstant.BOOLEAN_UNDEFINED)//由于关注权限在前端查询，此处默认未知状态(-1),等待首次更新
                            .setFollow(BooleanConstant.BOOLEAN_UNDEFINED)
                            .setHistoryMember(taobaoAPIService.isMember(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick()) ? BooleanConstant.BOOLEAN_YES : BooleanConstant.BOOLEAN_NO)
                            .setMember(sysCustom.getHistoryMember()))
                    .setHaveAuthorization(BooleanConstant.BOOLEAN_NO);
        }

        /*2.组织返回值*/
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        boolean should_sure_history_follow_state = BooleanConstant.BOOLEAN_UNDEFINED.equals(sysCustom.getHistoryFollow());
        boolean have_authorization = BooleanConstant.BOOLEAN_NO.equals(sysCustom.getHaveAuthorization());
        resultMap.put("history_follow_undefined", should_sure_history_follow_state);
        resultMap.put("have_authorization", have_authorization);
        return YunReturnValue.ok(resultMap, "获取玩家信息," +
                (should_sure_history_follow_state ? "【友情提示：】history_follow_undefined = true ---> 首次进入活动，需要立马确认该玩家的关注状态，并将本次关注状态作为history_follow。!!!!!!!" : "") +
                (!have_authorization ? "【友情提示：】have_authorization = false ---> 表示当前玩家当前尚未授权。" : "")
        );
    }
}