package com.duomai.project.api.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.duomai.common.base.controller.BaseRestController;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.ReturnBaseData;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.common.dto.YunTokenParameter;
import com.duomai.common.enums.SysErrorEnum;
import com.duomai.project.api.gateway.QLApiExecuteHandler;
import com.duomai.project.api.gateway.entity.CgApiLog;
import com.duomai.project.api.gateway.repository.CgApiLogRepository;
import com.duomai.project.api.gateway.tool.ApiTool;
import com.duomai.project.configuration.SysCustomProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Random;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping(value = "/g2")
public class PostExchanageRestController extends BaseRestController {

    @Autowired
    private CgApiLogRepository cgApiLogRepository;

    @Autowired
    private SysCustomProperties sysCustomProperties;


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


    /**
     * gateWay
     *
     * @param apiSysParameter   内部参数
     * @param yunTokenParameter 云应用参数
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/router/api")
    public YunReturnValue gateWay(
            @RequestBody @Validated ApiSysParameter apiSysParameter,
            @Validated YunTokenParameter yunTokenParameter,
            HttpServletRequest request, HttpServletResponse response) {

        //压力测试
//        pressTest(yunTokenParameter);

        apiSysParameter.setRequestStartTime(new Date());
        CgApiLog cgApiLog = new CgApiLog()
                .setParType(0)
                .setCreateTime(apiSysParameter.getRequestStartTime())
                .setRequestIp(ApiTool.getIpAddress(request))
                .setApiName(apiSysParameter.getMethod())
                .setApiSign(apiSysParameter.getApiParameter().getCommomParameter().getSign())
                .setAppkey(apiSysParameter.getApiParameter().getCommomParameter().getAppkey())
                .setRequestData(JSON.toJSONString(apiSysParameter));

        /*1.补充需要的请求参数*/
        apiSysParameter.getApiParameter().getCommomParameter().setIp(cgApiLog.getRequestIp());
        apiSysParameter.getApiParameter().setYunTokenParameter(yunTokenParameter);
        /*2.sign校验*/
        if (!sysCustomProperties.getSysConfig().getAppkey().equals(apiSysParameter.getApiParameter().getCommomParameter().getAppkey())) {
            cgApiLog.setParType(1);
            cgApiLog.setErrorMsg(SysErrorEnum.VALID_APPKEY.getValue());
            return YunReturnValue.fail(SysErrorEnum.VALID_APPKEY);
        }
        if (!ApiTool.signCheck(apiSysParameter)) {
            cgApiLog.setParType(1);
            cgApiLog.setErrorMsg(SysErrorEnum.VALID_SIGN.getValue());
            return YunReturnValue.fail(SysErrorEnum.VALID_SIGN);
        }

        /*3.执行业务逻辑*/
        try {
            //执行业务逻辑，并获得返回值rValue
            YunReturnValue yunReturnValue = QLApiExecuteHandler.ApiExecute(apiSysParameter, request, response);
            if (yunReturnValue.getData().getStatus().equals(ReturnBaseData.error)) {
                cgApiLog.setParType(1);
                cgApiLog.setErrorMsg(yunReturnValue.getData().getMsg());
            }
            cgApiLog.setResponseData(JSON.toJSONString(yunReturnValue));
            return yunReturnValue;
        } catch (Exception e) {
            e.printStackTrace();
            cgApiLog.setParType(1);
            cgApiLog.setErrorMsg(e.getMessage());
            return YunReturnValue.fail(SysErrorEnum.SERVE_INNER, e.getMessage());
        } finally {
            //失败请求记录日志
            if (cgApiLog.getParType() == 1)
                cgApiLogRepository.save(cgApiLog);
        }
    }


    static Random random = new Random();
    static void pressTest(YunTokenParameter yunTokenParameter) {
        yunTokenParameter.setBuyerNick("夏01MN0fiX4NSkxY6YSa4UBv9j9xhcZT2bM0mnYiHo5bPlE="+ random.nextInt(1000000));
    }
}
