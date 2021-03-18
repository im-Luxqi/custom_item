package com.duomai.project.product.general.execute;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.PageListDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysTaskInviteLog;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysTaskInviteLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 邀请明细
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @time 2020-05-09 09:13:09
 */
@Component
public class ShowInviteListExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private ProjectHelper projectHelper;

    @Autowired
    private SysTaskInviteLogRepository sysTaskInviteLogRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {


        PageListDto<SysTaskInviteLog> pageListDto = sysParm.getApiParameter().findBeautyAdmjson(PageListDto.class);

        /*1.校验*/
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        Page<SysTaskInviteLog> list = null;
//                sysTaskInviteLogRepository.findByMixInviterAndHaveSuccessOrderByCreateTimeDesc(
//                buyerNick, BooleanConstant.BOOLEAN_YES, pageListDto.startJPAPage());
//        pageListDto.setJpaResultList(list);
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
