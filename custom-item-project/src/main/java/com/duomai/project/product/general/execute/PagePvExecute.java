package com.duomai.project.product.general.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.gateway.tool.ApiTool;
import com.duomai.project.product.general.entity.SysPagePvLog;
import com.duomai.project.product.general.repository.SysPagePvLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* 记录pv
 * @description
 * @create by 王星齐
 * @time 2020-08-21 09:19:23
 **/
@Component
public class PagePvExecute implements IApiExecute {
    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        JSONObject jsonObject = JSONObject.parseObject(sysParm.getApiParameter().getAdmjson().toString());
        String page = jsonObject.getString("page");
        String channel = jsonObject.getString("channel");
        Assert.hasLength(page, "page is not blank");
        Assert.hasLength(channel, "channel is not blank");

        sysPagePvLogRepository.save(new SysPagePvLog()
                .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                .setCreateTime(sysParm.getRequestStartTime())
                .setId(ApiTool.getIpAddress(request))
                .setChannel(channel)
                .setPage(page));

        return YunReturnValue.ok("pv记录成功");
    }
}
