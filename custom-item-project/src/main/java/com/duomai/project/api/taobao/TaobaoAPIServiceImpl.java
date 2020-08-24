package com.duomai.project.api.taobao;

import com.duomai.project.api.taobao.enums.TaoBaoTradeStatus;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaBenefitSendRequest;
import com.taobao.api.request.CrmMemberIdentityGetRequest;
import com.taobao.api.request.OpenTradesSoldGetRequest;
import com.taobao.api.response.AlibabaBenefitSendResponse;
import com.taobao.api.response.CrmMemberIdentityGetResponse;
import com.taobao.api.response.OpenTradesSoldGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("taobaoAPIService")
@Slf4j
public class TaobaoAPIServiceImpl implements ITaobaoAPIService {

    @Autowired
    @Qualifier(value = "taoBaoClient")
    private TaobaoClient client;


    public CrmMemberIdentityGetResponse CrmMemberIdentityGet(String buyerNick, String sessionkey) throws ApiException {
        CrmMemberIdentityGetRequest req = new CrmMemberIdentityGetRequest();
        req.setMixNick(buyerNick);
        CrmMemberIdentityGetResponse rsp = client.execute(req, sessionkey);
        return rsp;
    }

    @Override
    public boolean isMember(String buyerNick, String sessionkey) throws ApiException {
        Boolean memberOrNot = Boolean.FALSE;
        CrmMemberIdentityGetResponse member = CrmMemberIdentityGet(buyerNick, sessionkey);
        if (member != null && member.getResult() != null && member.getResult().getMemberInfo() != null &&
                member.getResult().getMemberInfo().getGrade() > 0L)
            memberOrNot = Boolean.TRUE;
        return memberOrNot;
    }

    @Override
    public List<OpenTradesSoldGetResponse.Trade> taobaoOpenTradesSoldGet(String buyer_open_id, String buyer_nick, String sessionKey, TaoBaoTradeStatus status, Date start_created, Date end_created) throws ApiException {

        OpenTradesSoldGetRequest req = new OpenTradesSoldGetRequest();
        req.setFields("tid,payment,created,status,orders,buyer_rate,pay_time");
        req.setStartCreated(start_created);
        req.setEndCreated(end_created);
        req.setStatus(status.getCode());
        req.setType("guarantee_trade,auto_delivery,ec,cod,step,tmall_i18n");
        req.setPageNo(1L);
        req.setPageSize(40L);
        req.setUseHasNext(true);
        req.setBuyerOpenId(buyer_open_id);
//        req.setBuyerNick(buyer_nick);
        return this.getAllTaobaoOpenTradesSoldGet(new ArrayList<OpenTradesSoldGetResponse.Trade>(100), req, sessionKey);
    }

    public List<OpenTradesSoldGetResponse.Trade> getAllTaobaoOpenTradesSoldGet(ArrayList<OpenTradesSoldGetResponse.Trade> trades, OpenTradesSoldGetRequest req, String sessionKey) throws ApiException {
        if (trades == null)
            return null;
        OpenTradesSoldGetResponse rsp = client.execute(req, sessionKey);
        if (!rsp.isSuccess()) {
            log.info(rsp.getBody());
            trades = null;
            return null;
        }
        if (rsp.getTrades() != null)
            trades.addAll(rsp.getTrades());
        if (rsp.getHasNext()) {
            req.setPageNo(req.getPageNo() + 1);
            this.getAllTaobaoOpenTradesSoldGet(trades, req, sessionKey);
        }
        return trades;
    }


    @Override
    public AlibabaBenefitSendResponse sendTaobaoCoupon(String openId, String ename, String APP_NAME, String SESSION_KEY) throws ApiException {
        AlibabaBenefitSendRequest req = new AlibabaBenefitSendRequest();
        req.setRightEname(ename);//发放的权益(奖品)唯一标识
        req.setReceiverId(openId);//接收奖品的用户openId
        req.setUniqueId(openId + Calendar.getInstance().getTimeInMillis());//幂等校验id，业务重试需要，自定义唯一字段即可
        req.setAppName(APP_NAME);//商家来源身份标识
        req.setUserType("taobao");
        req.setIp("");
        return client.execute(req, SESSION_KEY);
    }
}
