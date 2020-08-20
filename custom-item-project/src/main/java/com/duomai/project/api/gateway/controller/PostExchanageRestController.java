package com.duomai.project.api.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.duomai.common.base.controller.BaseRestController;
import com.duomai.common.constants.CommomCode;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.common.dto.YunTokenParameter;
import com.duomai.project.api.gateway.QLApiExecuteHandler;
import com.duomai.project.api.gateway.entity.CgApiLog;
import com.duomai.project.api.gateway.repository.CgApiLogRepository;
import com.duomai.project.api.gateway.tool.ApiTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping(value = "/g2")
public class PostExchanageRestController extends BaseRestController {

    @Autowired
    private CgApiLogRepository cgApiLogRepository;

    /**
     * 云应用存活检测
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/router/test")
    public YunReturnValue test2() {
        return YunReturnValue.ok("云应用存活检测");
    }


    /*gateWay
     * @description
     * @create by 王星齐
     * @time 2020-08-20 17:29:56
     **/
    @PostMapping(value = "/router/api")
    public YunReturnValue gateWay(
            @RequestBody @Validated ApiSysParameter apiSysParameter,
            @Validated YunTokenParameter yunTokenParameter,
            HttpServletRequest request, HttpServletResponse response) {
        /*1.补充需要的请求参数*/
        apiSysParameter.getApiParameter().setYunTokenParameter(yunTokenParameter);
        CgApiLog cgApiLog = new CgApiLog()
                .setParType(0)
                .setCreateTime(new Timestamp(Long.parseLong(apiSysParameter.getApiParameter().getCommomParameter().getTimestamp())))
                .setRequestIp(ApiTool.getIpAddress(request))
                .setApiName(apiSysParameter.getMethod())
                .setApiSign(apiSysParameter.getApiParameter().getCommomParameter().getSign())
                .setAppkey(apiSysParameter.getApiParameter().getCommomParameter().getAppkey())
                .setRequestData(JSON.toJSONString(apiSysParameter));

        /*2.sign校验*/
        if (!ApiTool.signCheck(apiSysParameter)) {
            cgApiLog.setParType(1);
            cgApiLog.setErrorMsg(CommomCode.ApiErrorMap.get(CommomCode.SIGN_ERROR));
            return YunReturnValue.fail(null, CommomCode.SIGN_ERROR, CommomCode.ApiErrorMap.get(CommomCode.SIGN_ERROR));
        }

        /*3.执行业务逻辑*/
        try {
            YunReturnValue yunReturnValue = QLApiExecuteHandler.ApiExecute(apiSysParameter, request, response);//执行业务逻辑，并获得返回值rValue
            cgApiLog.setResponseData(JSON.toJSONString(yunReturnValue));
            return yunReturnValue;
        } catch (Exception e) {
            e.printStackTrace();
            cgApiLog.setParType(1);
            cgApiLog.setErrorMsg(e.getMessage());
            return YunReturnValue.fail(null, CommomCode.REQUEST_ERROR, CommomCode.ApiErrorMap.get(CommomCode.REQUEST_ERROR));
        } finally {
            cgApiLogRepository.save(cgApiLog);
        }
    }
}
