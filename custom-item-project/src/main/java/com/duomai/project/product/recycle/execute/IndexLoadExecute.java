package com.duomai.project.product.recycle.execute;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.dto.ActBaseSetting;
import com.duomai.project.product.general.dto.XyReturn;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.tool.ProjectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/* 活动主页--load
 * @description
 * @create by 王星齐
 * @time 2020-07-29 10:24:46
 **/
@Component
public class IndexLoadExecute implements IApiExecute {
    @Autowired
    private ProjectHelper projectHelper;

    @Autowired
    private SysCustomRepository sysCustomRepository;


    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.活动配置查询，活动期间才可访问接口*/
        ActBaseSetting actBaseSetting = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate(actBaseSetting);

        /*2.确认当前玩家身份*/
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(
                sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
        //未注册的玩家，注册身份
        if (Objects.isNull(sysCustom)) {
            sysCustom = projectHelper.customInit(sysParm);
            sysCustomRepository.save(sysCustom);
        }

        XyReturn ordersByOpenId = projectHelper.findOrdersByOpenId(System.currentTimeMillis(), sysParm.getApiParameter().getYunTokenParameter().getOpenUId(),
                actBaseSetting.getActStartTime().getTime(), actBaseSetting.getActEndTime().getTime(), sysParm.getApiParameter().getYunTokenParameter().getBuyerNick(), sysParm.getRequestStartTime());
        /*3.数据展示*/
        Map result = new HashMap<>();
        result.put("act_base_setting", actBaseSetting);
        result.put("custom_has_order", ordersByOpenId.getCode().equals(0) && CollectionUtils.isNotEmpty(ordersByOpenId.getData()));
        result.put("custom", sysCustom.setId(null)
                .setCreateTime(null).setUpdateTime(null).setOpenId(null));
        return YunReturnValue.ok(result, "玩家成功登陆活动首页");
    }
}
