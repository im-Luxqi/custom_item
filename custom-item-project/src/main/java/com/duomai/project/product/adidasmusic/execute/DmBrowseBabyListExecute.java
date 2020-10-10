package com.duomai.project.product.adidasmusic.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.FinishTheTaskHelper;
import com.duomai.project.product.general.dto.PageListDto;
import com.duomai.project.product.general.entity.SysBrowseLog;
import com.duomai.project.product.general.entity.SysCommodity;
import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.enums.CommonExceptionEnum;
import com.duomai.project.product.general.repository.SysBrowseLogRepository;
import com.duomai.project.product.general.repository.SysCommodityRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author cjw
 * @description 阿迪双十一小程序二楼 浏览宝贝
 * @time 2020-10-03
 */
@Service
public class DmBrowseBabyListExecute implements IApiExecute {

    @Resource
    private SysCommodityRepository sysCommodityRepository;
    @Resource
    private SysBrowseLogRepository browseLogRepository;
    @Resource
    private FinishTheTaskHelper finishTheTaskHelper;
    private static String commoditySorE = "everyDay";
    private static String commoditySorT = "total";

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) {

        //取参
        PageListDto pageListDto = sysParm.getApiParameter().findBeautyAdmjson(PageListDto.class);
        pageListDto.startPage();
        Date date = sysParm.getRequestStartTime();
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Date finalDate = CommonDateParseUtil.date2date(date, CommonDateParseUtil.YYYY_MM_DD);
        //获取浏览宝贝实物
        SysCommodity commodity = new SysCommodity();
        List<SysCommodity> sysCommodities = sysCommodityRepository.findAll(Example.of(
                commodity.setType(AwardTypeEnum.GOODS).setCommoditySort(commoditySorE).setCreateTime(date)));

        //为空随机12个商品
        if (sysCommodities.isEmpty()) {
            //获取源商品
            List<SysCommodity> commodities = sysCommodityRepository.queryAllByTypeAndAndCommoditySort(AwardTypeEnum.GOODS, commoditySorT);
            Assert.notEmpty(commodities, "未获取到浏览宝贝信息!");
            //随机取12个宝贝
            sysCommodities = finishTheTaskHelper.randowList(commodities, sysCommodities, 12);
            //保存当天宝贝信息
            sysCommodities.stream().forEach(o -> o.setCommoditySort(commoditySorE).setCreateTime(finalDate).setId(null));
            sysCommodityRepository.saveAll(sysCommodities);
        }

        //获取该粉丝浏览日志 此处保留根据当天时间查询
        SysBrowseLog browseLog = new SysBrowseLog();
        List<SysBrowseLog> browseLogs = browseLogRepository.findAll(
                Example.of(browseLog.setBuyerNick(buyerNick)
                        .setCreateTime(finalDate)));

        sysCommodities.stream().forEach(o -> {
            AtomicReference<Boolean> fal = new AtomicReference<>(false);
            browseLogs.stream().forEach(v -> {
                if (o.getNumId().longValue() == v.getNumId().longValue()) {
                    fal.set(true);
                }
            });
            if (fal.get()) {
                o.setIsBrowse(1);
            }
        });
        pageListDto.setResultList(sysCommodities);
        return YunReturnValue.ok(pageListDto, CommonExceptionEnum.OPERATION_SUCCESS.getMsg());
    }
}
