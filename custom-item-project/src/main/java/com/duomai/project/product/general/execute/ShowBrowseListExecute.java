package com.duomai.project.product.general.execute;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.dto.PageListDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysSettingCommodity;
import com.duomai.project.product.general.entity.SysTaskBrowseLog;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysSettingCommodityRepository;
import com.duomai.project.product.general.repository.SysTaskBrowseLogRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 浏览商品列表
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @time 2020-05-09 09:13:09
 */
@Component
public class ShowBrowseListExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private SysSettingCommodityRepository sysSettingCommodityRepository;
    @Autowired
    private SysTaskBrowseLogRepository sysTaskBrowseLogRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {


        PageListDto<SysSettingCommodity> pageListDto = sysParm.getApiParameter().findBeautyAdmjson(PageListDto.class);

        /*1.校验*/
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        /*2.查询商品*/
        Page<SysSettingCommodity> list = sysSettingCommodityRepository.findAll(pageListDto.startJPAPage());
        pageListDto.setJpaResultList(list);

        /*3.今日浏览过的打标*/
        Date today = sysParm.getRequestStartTime();
        List<SysTaskBrowseLog> todayHasBrowseLogs = sysTaskBrowseLogRepository.findByBuyerNickAndBrowseTime(buyerNick
                , CommonDateParseUtil.date2string(today, "yyyy-MM-dd"));
        if (!CollectionUtils.isEmpty(todayHasBrowseLogs)) {
            Set<Long> collect = todayHasBrowseLogs.stream().map(SysTaskBrowseLog::getNumId).collect(Collectors.toSet());
            pageListDto.getResultList().forEach(x -> {
                x.setId(null);
                if (collect.contains(x.getNumId())) {
                    x.setTodayHasBrowse(true);
                }
            });
        }
        pageListDto.setTodayBrowseNum(todayHasBrowseLogs.size());
        return YunReturnValue.ok(pageListDto, "浏览商品列表");
    }
}
