package com.duomai.new_custom_base.api.product.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.duomai.new_custom_base.api.product.core.QLApiExecuteHandler;
import com.duomai.new_custom_base.api.product.core.apiCommomModel.CommomCode;
import com.duomai.new_custom_base.api.product.core.apiReturnValue.YunReturnValue;
import com.duomai.new_custom_base.api.product.core.entity.CgApiLog;
import com.duomai.new_custom_base.api.product.core.pojo.ApiSysParameter;
import com.duomai.new_custom_base.api.product.core.repository.CgApiLogRepository;
import com.duomai.new_custom_base.api.product.core.temp.ApiTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping(value = "/g2")
public class PostExchanageController {

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


    /**
     * 正式入口
     */
    @PostMapping(value = "/router/api")
    public YunReturnValue openapiEntrance2(HttpServletRequest request, HttpServletResponse response) {
        CgApiLog cgApiLog = new CgApiLog();
        cgApiLog.setRequestIp(ApiTool.getIpAddress(request));
        cgApiLog.setParType(0);

        /*1.获取参数*/
        String dmwxsign;
        ApiSysParameter aps2;
        try {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
                dmwxsign = ApiTool.readertostring(reader);
            } catch (IOException e) {
                e.printStackTrace();
                return YunReturnValue.fail(null, CommomCode.INVALID_PARAMS, CommomCode.ApiErrorMap.get(CommomCode.INVALID_PARAMS));
            }

            /*2.转换补充需要的请求参数*/
            try {
                JSONObject jsonfile = JSONObject.parseObject(dmwxsign
                        , Feature.OrderedField);
                Object admjson = jsonfile.getJSONObject("params").get("admjson");
                aps2 = JSON.parseObject(jsonfile.toJSONString(), ApiSysParameter.class);
                aps2.getApiParameter().getCommomParameter()
                        .setAdmjsonStr(JSON.toJSONString(admjson, SerializerFeature.WriteMapNullValue));
                //补充云token参数
                aps2.findYunTokenParameter(request);

                cgApiLog.setCreateTime(new Timestamp(Long.parseLong(aps2.getApiParameter().getCommomParameter().getTimestamp())));
                cgApiLog.setApiName(aps2.getMethod());
                cgApiLog.setApiSign(aps2.getApiParameter().getCommomParameter().getSign());
                cgApiLog.setAppkey(aps2.getApiParameter().getCommomParameter().getAppkey());
                cgApiLog.setRequestData(JSON.toJSONString(aps2));
            } catch (Exception e) {
                e.printStackTrace();
                cgApiLog.setParType(1);
                cgApiLog.setErrorMsg(CommomCode.ApiErrorMap.get(CommomCode.TYPE_ERROR));
                return YunReturnValue.fail(null, CommomCode.TYPE_ERROR, CommomCode.ApiErrorMap.get(CommomCode.TYPE_ERROR));
            }

            /*3.sign校验*/
            if (!ApiTool.signCheck(aps2)) {
                cgApiLog.setParType(1);
                cgApiLog.setErrorMsg(CommomCode.ApiErrorMap.get(CommomCode.SIGN_ERROR));
                return YunReturnValue.fail(null, CommomCode.SIGN_ERROR, CommomCode.ApiErrorMap.get(CommomCode.SIGN_ERROR));
            }

            /*4.执行业务逻辑*/
            try {
                YunReturnValue yunReturnValue = QLApiExecuteHandler.ApiExecute(aps2, request, response);//执行业务逻辑，并获得返回值rValue
                cgApiLog.setResponseData(JSON.toJSONString(yunReturnValue));
                return yunReturnValue;
            } catch (Exception e) {
                e.printStackTrace();
                cgApiLog.setParType(1);
                cgApiLog.setErrorMsg(e.getMessage());
                return YunReturnValue.fail(null, CommomCode.REQUEST_ERROR, CommomCode.ApiErrorMap.get(CommomCode.REQUEST_ERROR));
            }
        } finally {
            cgApiLogRepository.save(cgApiLog);
        }
    }

}
