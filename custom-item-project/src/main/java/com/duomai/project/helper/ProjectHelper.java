package com.duomai.project.helper;

import com.duomai.project.configuration.annotation.JoinMemcache;
import com.duomai.project.helper.constants.ActSettingConstant;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysSettingKeyValue;
import com.duomai.project.product.general.repository.SysSettingKeyValueRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 活动 常规操作
 * @description 【帮助类目录】
 *      1.任务配置--信息获取
 *      2.活动配置--信息获取
 *      3.检验时候为活动期间
 * @create by 王星齐
 * @date 2020-08-26 16:52
 */
@Slf4j
@Component
public class ProjectHelper {
    @Autowired
    private SysSettingKeyValueRepository sysSettingKeyValueRepository;


    /** 2.活动配置--信息获取
     * @description
     * @create by 王星齐
     * @time 2020-08-26 20:03:20
     */
    @JoinMemcache()
    public ActBaseSettingDto actBaseSettingFind() {
        List<SysSettingKeyValue> byType = sysSettingKeyValueRepository.findByType(ActSettingConstant.TYPE_ACT_SETTING);
        Map<String, String> collect = byType.stream().collect(Collectors.toMap(SysSettingKeyValue::getK, SysSettingKeyValue::getV));
        return new ActBaseSettingDto().setActRule(collect.get(ActSettingConstant.ACT_RULE))
                .setActStartTime(CommonDateParseUtil.string2date(collect.get(ActSettingConstant.ACT_START_TIME), CommonDateParseUtil.YYYY_MM_DD))
                .setActEndTime(CommonDateParseUtil.getEndTimeOfDay(CommonDateParseUtil.string2date(collect.get(ActSettingConstant.ACT_END_TIME), CommonDateParseUtil.YYYY_MM_DD)))
                .setDrawCouponNum(Integer.valueOf(collect.get(ActSettingConstant.DRAW_COUPON_NUM)))
                .setTaskSignContinuous(Integer.valueOf(collect.get(ActSettingConstant.TASK_SIGN_CONTINUOUS)))
                .setTaskSignContinuousPayment(Integer.valueOf(collect.get(ActSettingConstant.TASK_SIGN_CONTINUOUS_PAYMENT)))
                .setTaskBrowseShouldSee(Integer.valueOf(collect.get(ActSettingConstant.TASK_BROWSE_SHOULD_SEE)))
                .setTaskOrderShouldSpend(Double.valueOf(collect.get(ActSettingConstant.TASK_ORDER_SHOULD_SPEND)))
                ;
    }

    /* 3.检验时候为活动期间
     * @description
     *   使用场景-------->post请求
     * @create by 王星齐
     * @time 2020-08-26 20:11:04
     **/
    public void actTimeValidate() throws Exception {
        ActBaseSettingDto actBaseSettingDto = this.actBaseSettingFind();
        Date now = new Date();
        if (now.before(actBaseSettingDto.getActStartTime()))
            throw new Exception("活动尚未开始，尽情期待！");
        if (now.after(actBaseSettingDto.getActEndTime()))
            throw new Exception("活动已结束！");
    }

    /** 3.检验时候为活动期间
     * @description
     *      使用场景-------->load请求
     * @create by 王星齐
     * @time 2020-08-26 20:11:04
     */
    public boolean actTimeValidateFlag() {
        ActBaseSettingDto actBaseSettingDto = this.actBaseSettingFind();
        Date now = new Date();
        boolean liveFlag = true;
        if (now.before(actBaseSettingDto.getActStartTime()) || now.after(actBaseSettingDto.getActEndTime()))
            liveFlag = false;
        return liveFlag;
    }
}
