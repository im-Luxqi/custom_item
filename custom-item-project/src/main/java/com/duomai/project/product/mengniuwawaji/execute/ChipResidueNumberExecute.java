package com.duomai.project.product.mengniuwawaji.execute;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.dto.PageListDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ChipResidueNumberExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysLuckyChanceRepository sysLuckyChanceRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PageListDto<SysLuckyChance> pageListDto = sysParm.getApiParameter().findBeautyAdmjson(PageListDto.class);

        /*1.校验*/
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        // 查询使用过的碎片
        Page<SysLuckyChance> list = sysLuckyChanceRepository.findByBuyerNickAndIsUseOrderByUseTimeDesc(buyerNick, BooleanConstant.BOOLEAN_YES, pageListDto.startJPAPage());
        pageListDto.setJpaResultList(list);
        if (CollectionUtils.isNotEmpty(pageListDto.getResultList())) {
            pageListDto.getResultList().forEach(x -> {
                x.setId(null);
                x.setIsUse(null);
                x.setUseTime(null);
            });
        }
        return YunReturnValue.ok(pageListDto, "碎片明细");
    }
}
