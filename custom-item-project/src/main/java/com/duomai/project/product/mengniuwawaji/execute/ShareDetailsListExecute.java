package com.duomai.project.product.mengniuwawaji.execute;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.dto.PageListDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysTaskInviteLog;
import com.duomai.project.product.general.enums.InviteTypeEnum;
import com.duomai.project.product.general.enums.PvPageEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysTaskInviteLogRepository;
import com.duomai.project.tool.ProjectTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ShareDetailsListExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysTaskInviteLogRepository sysTaskInviteLogRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PageListDto<SysTaskInviteLog> pageListDto = sysParm.getApiParameter().findBeautyAdmjson(PageListDto.class);

        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        /*1.校验*/
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        InviteTypeEnum type = ProjectTools.enumValueOf(InviteTypeEnum.class, jsonObjectAdmjson.getString("inviteType"));
        Assert.notNull(type,"inviteType不能为空");
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        Page<SysTaskInviteLog> list = sysTaskInviteLogRepository.findByInviteTypeAndMixInviterAndHaveSuccessOrderByCreateTimeDesc(type, buyerNick, BooleanConstant.BOOLEAN_YES, pageListDto.startJPAPage());
        pageListDto.setJpaResultList(list);
        if (CollectionUtils.isNotEmpty(pageListDto.getResultList())) {
            pageListDto.getResultList().forEach(x -> {
                x.setId(null);
                x.setHaveSuccess(null);
                x.setInviteTime(null);
                x.setInviter(null);
                x.setInviterImg(null);
                x.setMixInviter(null);
            });
        }
        return YunReturnValue.ok(pageListDto, "邀请明细");
    }
}
