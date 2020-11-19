package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysInviteLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/** 授权成功后，完善用户信息
 * @author im-luxqi
 * @description  (真实昵称，头像)
 * @create by 王星齐
 */
@Component
public class PlayerInfoFillForAfterAuthorizationExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Resource
    private SysInviteLogRepository inviteLogRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验参数*/
        SysCustom sysCustomParam = sysParm.getApiParameter().findBeautyAdmjson(SysCustom.class);
        Assert.hasLength(sysCustomParam.getZnick(), "真实昵称不能为空(demo:->{'znick':'王小明'})");
        Assert.hasLength(sysCustomParam.getHeadImg(), "头像不能为空(demo:->{'headImg':'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1419628337,1603242413&fm=26&gp=0.jpg'})");


        /*2.校验玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(buyerNick);

        /*3.更新用户信息*/
        if (!Objects.isNull(sysCustom)) {
            sysCustomRepository.save(sysCustom.setZnick(sysCustomParam.getZnick())
                    .setHeadImg(sysCustomParam.getHeadImg())
                    .setHaveAuthorization(BooleanConstant.BOOLEAN_YES)
                    .setUpdateTime(sysParm.getRequestStartTime()));
        }

        return YunReturnValue.ok("完善用户信息成功");
    }
}
