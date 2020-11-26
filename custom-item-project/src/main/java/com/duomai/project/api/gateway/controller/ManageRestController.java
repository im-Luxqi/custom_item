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
import java.util.Objects;
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
        if (StringUtils.isBlank(frontAward.getId()) || StringUtils.isBlank(check) || !check.equals(CHECK_STRING)){
            return "check.error";
        }
        Optional<SysSettingAward> byId = sysSettingAwardRepository.findById(frontAward.getId());
        if (byId.isPresent()) {
            SysSettingAward behindAward = byId.get();
            //1.ename
            if (StringUtils.isNotBlank(frontAward.getEname())) {
                behindAward.setEname(frontAward.getEname());
            }

            //2.img
            if (StringUtils.isNotBlank(frontAward.getImg())) {
                behindAward.setImg(frontAward.getImg());
            }
            //3.lucky_value
            if (StringUtils.isNotBlank(frontAward.getLuckyValue())) {
                behindAward.setLuckyValue(frontAward.getLuckyValue());
            }
            //4.name
            if (StringUtils.isNotBlank(frontAward.getName())) {
                behindAward.setName(frontAward.getName());
            }
            //5.price
            if (StringUtils.isNotBlank(frontAward.getPrice())) {
                behindAward.setPrice(frontAward.getPrice());
            }
            //6.description
            if (StringUtils.isNotBlank(frontAward.getDescription())) {
                behindAward.setDescription(frontAward.getDescription());
            }
            //6.pool_level
            if (!Objects.isNull(frontAward.getPoolLevel())) {
                behindAward.setPoolLevel(frontAward.getPoolLevel());
            }
            //7.remain_num
            if (!Objects.isNull(frontAward.getRemainNum())) {
                behindAward.setRemainNum(frontAward.getRemainNum());
            }
            //8.send_num
            if (!Objects.isNull(frontAward.getSendNum())) {
                behindAward.setSendNum(frontAward.getSendNum());
            }
            //9.total_num
            if (!Objects.isNull(frontAward.getTotalNum())) {
                behindAward.setTotalNum(frontAward.getTotalNum());
            }
            //10.type
            if (!Objects.isNull(frontAward.getType())) {
                behindAward.setType(frontAward.getType());
            }


            sysSettingAwardRepository.save(behindAward);
        }
        return "success";
    }

    @PostMapping(value = "/change/kv")
    public String changeKv(String k, String v, String check) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isBlank(k) || StringUtils.isBlank(check) || !check.equals(CHECK_STRING)){
            return "check.error";
        }
        Optional<SysSettingKeyValue> byId = sysSettingKeyValueRepository.findById(k);
        if (byId.isPresent()) {
            SysSettingKeyValue behind = byId.get();
            behind.setV(v);
            sysSettingKeyValueRepository.save(behind);
        }
        return "success";
    }


}
