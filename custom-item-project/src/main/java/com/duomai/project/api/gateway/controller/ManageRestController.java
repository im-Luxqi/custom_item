package com.duomai.project.api.gateway.controller;

import com.duomai.common.base.controller.BaseRestController;
import com.duomai.project.product.general.entity.SysSettingAward;
import com.duomai.project.product.general.entity.SysSettingKeyValue;
import com.duomai.project.product.general.repository.SysSettingAwardRepository;
import com.duomai.project.product.general.repository.SysSettingKeyValueRepository;
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
    private SysSettingAwardRepository sysSettingAwardRepository;
    @Autowired
    private SysSettingKeyValueRepository sysSettingKeyValueRepository;


    /*gateWay
     * @description
     **/
    static String CHECK_STRING = "0068f4c44d7a453bbd95cfb58150925d";

    @PostMapping(value = "/change/award")
    public String changeAward(SysSettingAward frontAward, String check) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isBlank(frontAward.getId()) || StringUtils.isBlank(check) || !check.equals(CHECK_STRING))
            return "check.error";
        Optional<SysSettingAward> byId = sysSettingAwardRepository.findById(frontAward.getId());
        if (byId.isPresent()) {
            SysSettingAward behindAward = byId.get();
            if (StringUtils.isNotBlank(frontAward.getLuckyValue()))
                behindAward.setLuckyValue(frontAward.getLuckyValue());
            if (StringUtils.isNotBlank(frontAward.getEname()))
                behindAward.setEname(frontAward.getEname());
            sysSettingAwardRepository.save(behindAward);
        }
        return "success";
    }

    @PostMapping(value = "/change/kv")
    public String changeKv(String k, String v, String check) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isBlank(k) || StringUtils.isBlank(check) || !check.equals(CHECK_STRING))
            return "check.error";
        Optional<SysSettingKeyValue> byId = sysSettingKeyValueRepository.findById(k);
        if (byId.isPresent()) {
            SysSettingKeyValue behind = byId.get();
            behind.setV(v);
            sysSettingKeyValueRepository.save(behind);
        }
        return "success";
    }


}
