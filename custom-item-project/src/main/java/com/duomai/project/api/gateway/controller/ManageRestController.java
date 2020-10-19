package com.duomai.project.api.gateway.controller;

import com.duomai.common.base.controller.BaseRestController;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.entity.SysKeyValue;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysKeyValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping(value = "/g3")
public class ManageRestController extends BaseRestController {
    @Autowired
    private SysAwardRepository sysAwardRepository;
    @Autowired
    private SysKeyValueRepository sysKeyValueRepository;


    /*gateWay
     * @description
     **/
    static String CHECK_STRING = "0068f4c44d7a453bbd95cfb58150925d";

    @PostMapping(value = "/change/award")
    public String changeAward(SysAward frontAward, String check) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isBlank(frontAward.getId()) || StringUtils.isBlank(check) || !check.equals(CHECK_STRING))
            return "check.error";
        Optional<SysAward> byId = sysAwardRepository.findById(frontAward.getId());
        if (byId.isPresent()) {
            SysAward behindAward = byId.get();
            if (StringUtils.isNotBlank(frontAward.getLuckyValue()))
                behindAward.setLuckyValue(frontAward.getLuckyValue());
            if (StringUtils.isNotBlank(frontAward.getEname()))
                behindAward.setEname(frontAward.getEname());
            sysAwardRepository.save(behindAward);
        }
        return "success";
    }

    @PostMapping(value = "/change/kv")
    public String changeKv(String k, String v, String check) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isBlank(k) || StringUtils.isBlank(check) || !check.equals(CHECK_STRING))
            return "check.error";
        Optional<SysKeyValue> byId = sysKeyValueRepository.findById(k);
        if (byId.isPresent()) {
            SysKeyValue behind = byId.get();
            behind.setV(v);
            sysKeyValueRepository.save(behind);
        }
        return "success";
    }


}
