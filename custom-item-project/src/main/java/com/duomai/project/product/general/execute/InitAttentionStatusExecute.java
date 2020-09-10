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

/* 活动主页--load后，初始化玩家关注状态
 * @description  无关注状态的玩家（oldFans=-1）
 * @create by 王星齐
 * @time 2020-07-29 10:24:46
 **/
@Component
public class InitAttentionStatusExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;


    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验参数*/
        JSONObject jsonObject = sysParm.getApiParameter().findJsonObjectAdmjson();
        Boolean has_attention = jsonObject.getBoolean("has_attention");
        Assert.notNull(has_attention, "当前是否关注店铺，不能为空");

        /*2.查找到指定玩家*/
        SysCustom sysCustom = sysCustomRepository.findByBuyerNickAndOldFans(
                sysParm.getApiParameter().getYunTokenParameter().getBuyerNick(), BooleanConstant.BOOLEAN_UNDEFINED);
        Assert.notNull(sysCustom, "不存在该玩家，或者该玩家已经初始化关注信息");

        /*3.初始化玩家关注状态*/
        sysCustomRepository.save(sysCustom.setOldFans(has_attention ? BooleanConstant.BOOLEAN_YES : BooleanConstant.BOOLEAN_NO));
        return YunReturnValue.ok("初始化玩家关注信息成功");
    }
}
