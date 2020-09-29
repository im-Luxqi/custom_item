package com.duomai.project.product.general.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysPagePvLog;
import com.duomai.project.product.general.enums.PvPageEnum;
import com.duomai.project.product.general.repository.SysPagePvLogRepository;
import com.duomai.project.tool.ProjectTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* 记录pv
 * @description (哪个渠道，页面)
 * @create by 王星齐
 * @time 2020-08-26 19:11:50
 **/
@Component
public class PagePvExecute implements IApiExecute {
    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验必传参数*/
        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        PvPageEnum page = ProjectTools.enumValueOf(PvPageEnum.class, jsonObjectAdmjson.getString("page"));
        String channel = jsonObjectAdmjson.getString("channel");
        Assert.notNull(page, "来源页不能为空");
        if (PvPageEnum.PAGE_DAKA.equals(page))
            Assert.notNull(channel, "来源渠道不能为空");

        /*2.保存pv*/
        sysPagePvLogRepository.save(new SysPagePvLog()
                .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                .setCreateTime(sysParm.getRequestStartTime())
                .setId(sysParm.getApiParameter().getCommomParameter().getIp())
                .setChannel(channel)
                .setPage(page));
        return YunReturnValue.ok("pv记录成功");
    }
}
