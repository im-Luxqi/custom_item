package com.duomai.project.product.general.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.FollowWayFromEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 补全字段history_follow
 *
 * @author im-luxqi
 * @description 由于后台没有相关权限接口（查询玩家是否关注店铺）
 **/
@Component
public class PlayerInfoFillForHistroyFollowExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验参数*/
        JSONObject jsonObject = sysParm.getApiParameter().findJsonObjectAdmjson();
        Boolean has_follow = jsonObject.getBoolean("has_follow");
        Assert.notNull(has_follow, "是否关注店铺，不能为空(demo:->{'has_follow':true/false})");

        /*2.查找到指定玩家*/
        SysCustom sysCustom = sysCustomRepository.findFirstByBuyerNickAndFollowWayFrom(
                sysParm.getApiParameter().getYunTokenParameter().getBuyerNick(), FollowWayFromEnum.UNDIFIND);

        boolean queryFlag = !Objects.isNull(sysCustom);
        if (queryFlag){
            /*3.初始化玩家关注状态*/
            sysCustomRepository.save(sysCustom.setFollowWayFrom(has_follow ? FollowWayFromEnum.HISTROY_FOLLOW : FollowWayFromEnum.NON_FOLLOW));
        }
        return YunReturnValue.ok(queryFlag ? "初始化玩家关注信息成功" : "未查询到符合条件的玩家");
    }
}
