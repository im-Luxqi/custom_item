//package com.duomai.project.product.adidasmusic.execute;
//
//import com.duomai.common.base.execute.IApiExecute;
//import com.duomai.common.dto.ApiSysParameter;
//import com.duomai.common.dto.YunReturnValue;
//import com.duomai.project.helper.FinishTheTaskHelper;
//import com.duomai.project.helper.ProjectHelper;
//import com.duomai.project.product.general.dto.ActBaseSettingDto;
//import com.duomai.project.product.general.dto.PageListDto;
//import com.duomai.project.product.general.entity.SysTaskBrowseLog;
//import com.duomai.project.product.general.entity.SysSettingCommodity;
//import com.duomai.project.product.general.enums.AwardTypeEnum;
//import com.duomai.project.product.general.enums.CommonExceptionEnum;
//import com.duomai.project.product.general.repository.SysTaskBrowseLogRepository;
//import com.duomai.project.product.general.repository.SysSettingCommodityRepository;
//import com.duomai.project.tool.CommonDateParseUtil;
//import org.springframework.stereotype.Service;
//import org.springframework.util.Assert;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicReference;
//
///**
// * @author cjw
// * @description 阿迪双十一小程序二楼 浏览宝贝
// * @time 2020-10-03
// */
//@Service
//public class DmBrowseBabyListExecute implements IApiExecute {
//
//    @Resource
//    private SysSettingCommodityRepository sysSettingCommodityRepository;
//    @Resource
//    private SysTaskBrowseLogRepository browseLogRepository;
//    @Resource
//    private FinishTheTaskHelper finishTheTaskHelper;
//    @Resource
//    private ProjectHelper projectHelper;
//    private static String commoditySorT = "total";//源宝贝
//    private static String commoditySorE = "everyDay";//每日随机宝贝
//
//    @Override
//    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
//                                     HttpServletResponse response) throws Exception {
//
//        //是否在活动期间
//        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
//         projectHelper.actTimeValidate();
//
//        //取参
//        PageListDto pageListDto = sysParm.getApiParameter().findBeautyAdmjson(PageListDto.class);
//        pageListDto.startMybatisPage();
//        Date date = sysParm.getRequestStartTime();
//        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
//
//        //获取源商品
//        List<SysSettingCommodity> commodities = sysSettingCommodityRepository.queryByTypeAndCommoditySort(AwardTypeEnum.GOODS, commoditySorT);
//        Assert.notEmpty(commodities, "未获取到浏览宝贝信息!");
//
//        //获取该粉丝浏览日志 此处保留根据当天时间查询
//        List<SysTaskBrowseLog> browseLogs = browseLogRepository.findByBuyerNickAndCreateTimeBetween(buyerNick,
//                CommonDateParseUtil.getStartTimeOfDay(date), CommonDateParseUtil.getEndTimeOfDay(date));
//
//        commodities.stream().forEach(o -> {
//            AtomicReference<Boolean> fal = new AtomicReference<>(false);
//            browseLogs.stream().forEach(v -> {
//                if (o.getNumId().longValue() == v.getNumId().longValue()) {
//                    fal.set(true);
//                }
//            });
//            if (fal.get()) {
//                o.setIsBrowse(1);
//            }
//        });
//
//        //随机取12个宝贝
//        List<SysSettingCommodity> sysCommodities = new ArrayList<>();
//        sysCommodities = finishTheTaskHelper.randowList(commodities, sysCommodities, 12);
//        sysCommodities.stream().forEach(s -> s.setNames(s.getName().split(",")));
//
//        pageListDto.setResultList(sysCommodities);
//        return YunReturnValue.ok(pageListDto, CommonExceptionEnum.OPERATION_SUCCESS.getMsg());
//    }
//}
