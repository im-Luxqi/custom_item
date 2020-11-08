package com.duomai.project.helper;

import com.duomai.project.configuration.annotation.JoinMemcache;
import com.duomai.project.helper.constants.ActSettingConstant;
import com.duomai.project.helper.constants.MemcachePublicKeyConstant;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.dto.TaskBaseSettingDto;
import com.duomai.project.product.general.entity.SysKeyValue;
import com.duomai.project.product.general.repository.SysKeyValueRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 活动 配置相关  常规操作 service
 *
 * @description
 * @create by 王星齐
 * @date 2020-08-26 16:52
 */
@Slf4j
@Component
public class ProjectHelper {
    @Autowired
    private SysKeyValueRepository sysKeyValueRepository;


    /* 任务配置--信息获取
     * @description
     * @create by lyj
     * @time 2020-10-10 16:52:00
     **/
    public TaskBaseSettingDto taskBaseSettingFind() {
        List<SysKeyValue> taskSetting = sysKeyValueRepository.findByType(ActSettingConstant.TYPE_TASK_DAKA_SETTING);
        Map<String, String> map = taskSetting.stream().collect(Collectors.toMap(SysKeyValue::getK, SysKeyValue::getV));
        return new TaskBaseSettingDto().setTaskStartTime(CommonDateParseUtil.string2date(map.get(ActSettingConstant.TASK_START_TIME), CommonDateParseUtil.YYYY_MM_DD))
                .setTaskEndTime(CommonDateParseUtil.string2date(map.get(ActSettingConstant.TASK_END_TIME), CommonDateParseUtil.YYYY_MM_DD));
    }


    /* 活动配置--信息获取
     * @description
     * @create by 王星齐
     * @time 2020-08-26 20:03:20
     **/
    @JoinMemcache(key = MemcachePublicKeyConstant.memcache_act_setting,refreshTime = 10)
    public ActBaseSettingDto actBaseSettingFind() {
        List<SysKeyValue> byType = sysKeyValueRepository.findByType(ActSettingConstant.TYPE_ACT_SETTING);
        Map<String, String> collect = byType.stream().collect(Collectors.toMap(SysKeyValue::getK, SysKeyValue::getV));
        return new ActBaseSettingDto().setActRule(collect.get(ActSettingConstant.ACT_RULE))
                .setActStartTime(CommonDateParseUtil.string2date(collect.get(ActSettingConstant.ACT_START_TIME), CommonDateParseUtil.YYYY_MM_DD))
                .setActEndTime(CommonDateParseUtil.getEndTimeOfDay(CommonDateParseUtil.string2date(collect.get(ActSettingConstant.ACT_END_TIME), CommonDateParseUtil.YYYY_MM_DD)));
    }

    /* 活动配置--检验时候为活动期间
     * @description ，不在活动范围，抛出异常不往下执行
     * @create by 王星齐
     * @time 2020-08-26 20:11:04
     * @param actBaseSetting
     **/
    public void actTimeValidate(ActBaseSettingDto actBaseSettingDto) throws Exception {
        Date now = new Date();
        if (now.before(actBaseSettingDto.getActStartTime()))
            throw new Exception("活动尚未开始，尽情期待！");
        if (now.after(actBaseSettingDto.getActEndTime()))
            throw new Exception("活动已结束！");
    }

    /* 活动配置--检验时候为活动期间
     * @description ，处于活动期间返回true
     * @create by 王星齐
     * @time 2020-08-26 20:11:04
     * @param actBaseSetting
     **/
    public boolean actTimeValidateFlag(ActBaseSettingDto actBaseSettingDto) {
        Date now = new Date();
        boolean liveFlag = true;
        if (now.before(actBaseSettingDto.getActStartTime()) || now.after(actBaseSettingDto.getActEndTime()))
            liveFlag = false;
        return liveFlag;
    }
}
