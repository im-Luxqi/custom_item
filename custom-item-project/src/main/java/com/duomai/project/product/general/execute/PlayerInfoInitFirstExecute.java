package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/* 玩家进入游戏，确认身份
 * @description
 * @create by 王星齐
 **/
@Component
public class PlayerInfoInitFirstExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Resource
    private ITaobaoAPIService taobaoAPIService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

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
                            .setMember(sysCustom.getHistoryMember()));
        }

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("should_finish_history_follow_state",BooleanConstant.BOOLEAN_UNDEFINED.equals(sysCustom.getHistoryFollow()));
        return YunReturnValue.ok(resultMap,"获取玩家信息");
    }
}