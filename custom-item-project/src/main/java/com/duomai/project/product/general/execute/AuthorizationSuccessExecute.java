package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* 授权成功后，完善用户信息
 * @description  (真实昵称，头像)
 * @create by 王星齐
 **/
@Component
public class AuthorizationSuccessExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验参数*/
        SysCustom sysCustomParam = sysParm.getApiParameter().findBeautyAdmjson(SysCustom.class);
//        ProjectTools.validateParam(sysCustomParam);
        Assert.hasLength(sysCustomParam.getZnick(), "真实昵称不能为空");
        Assert.hasLength(sysCustomParam.getHeadImg(), "头像不能为空");


        /*2.校验玩家*/
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(
                sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
        Assert.notNull(sysCustom, "不存在该玩家");

        /*3.更新用户信息*/
        sysCustomRepository.save(sysCustom.setZnick(sysCustomParam.getZnick())
                .setHeadImg(sysCustomParam.getHeadImg())
                .setUpdateTime(sysParm.getRequestStartTime()));
        return YunReturnValue.ok("完善用户信息成功");
    }
}
