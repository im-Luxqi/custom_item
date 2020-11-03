package com.duomai.project.product.general.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* 玩家首次进入游戏初始化,补全字段history_follow（首次进入活动时的关注状态）
 * @description  由于后台没有相关权限接口（查询玩家是否关注店铺）
 * @create by 王星齐
 **/
@Component
public class PlayerInfoInitForHistroyFollowExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验参数*/
        JSONObject jsonObject = sysParm.getApiParameter().findJsonObjectAdmjson();
        Boolean has_follow = jsonObject.getBoolean("has_follow");
        Assert.notNull(has_follow, "是否关注店铺，不能为空(demo:->{'has_follow':true})");

        /*2.查找到指定玩家*/
        SysCustom sysCustom = sysCustomRepository.findByBuyerNickAndHistoryFollow(
                sysParm.getApiParameter().getYunTokenParameter().getBuyerNick(), BooleanConstant.BOOLEAN_UNDEFINED);
        Assert.notNull(sysCustom, "不存在该玩家，或者该玩家已经初始化关注信息");

        /*3.初始化玩家关注状态*/
        sysCustomRepository.save(sysCustom.setHistoryFollow(has_follow ? BooleanConstant.BOOLEAN_YES : BooleanConstant.BOOLEAN_NO));
        return YunReturnValue.ok("初始化玩家关注信息成功");
    }
}
