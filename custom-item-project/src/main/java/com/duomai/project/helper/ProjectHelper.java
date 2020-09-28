package com.duomai.project.helper;

import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.product.general.constants.ActSettingConstant;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysKeyValue;
import com.duomai.project.product.general.repository.SysKeyValueRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import com.taobao.api.ApiException;
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
@Component
public class ProjectHelper {
    @Autowired
    private ITaobaoAPIService taobaoAPIService;
    @Autowired
    private SysKeyValueRepository sysKeyValueRepository;


    /* 活动配置--信息获取
     * @description
     * @create by 王星齐
     * @time 2020-08-26 20:03:20
     **/
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


    /* 客户--信息初始化
     * @description
     * @create by 王星齐
     * @time 2020-08-26 17:15:05
     * @param sysParm  系统参数
     * @param hasAttention  是否已关注店铺（因为是否老粉丝由前端查询）
     **/
    public SysCustom customInit(ApiSysParameter sysParm) throws ApiException {
        SysCustom sysCustom = new SysCustom();

        return sysCustom.setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                .setCreateTime(sysParm.getRequestStartTime())
                .setOpenId(sysParm.getApiParameter().getYunTokenParameter().getOpenUId())
                .setOldFans(BooleanConstant.BOOLEAN_UNDEFINED)//由于关注权限在前端查询，此处默认未知状态(-1),等待首次更新
                .setOldMember(taobaoAPIService.isMember(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick()) ? BooleanConstant.BOOLEAN_YES : BooleanConstant.BOOLEAN_NO)
                .setFans(sysCustom.getOldFans())
                .setMember(sysCustom.getOldMember().equals(BooleanConstant.BOOLEAN_YES) ? BooleanConstant.BOOLEAN_YES : BooleanConstant.BOOLEAN_NO);
    }
}
