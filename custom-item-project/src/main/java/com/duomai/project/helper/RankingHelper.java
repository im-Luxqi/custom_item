//package com.duomai.project.helper;
//
//import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
//import com.duomai.project.api.taobao.MemCacheData;
//import com.duomai.project.api.taobao.MemcacheTools;
//import com.duomai.project.product.general.dto.PageListDto;
//import com.duomai.project.product.general.entity.SysCustomRanking;
//import com.duomai.project.product.general.repository.SysCustomRankingRepository;
//import com.duomai.project.tool.ProjectTools;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//
//import java.util.Date;
//import java.util.Objects;
//
///**
// * 排行榜 相关操作
// *
// * @description
// * @create by 王星齐
// * @date 2020-08-26 16:52
// */
//@Slf4j
//@Component
//public class RankingHelper {
//
//    @Autowired
//    private SysCustomRankingRepository sysCustomRankingRepository;
//
//
//    private PageListDto<SysCustomRanking> rankingDbData(int rankSize) {
//        PageListDto<SysCustomRanking> pageListDto = new PageListDto<>();
//        pageListDto.setPageSize(rankSize);
//        Page<SysCustomRanking> ranking = sysCustomRankingRepository.ranking(pageListDto.startJPAPage());
//        pageListDto.setJpaResultList(ranking);
//        if (CollectionUtils.isNotEmpty(pageListDto.getResultList())) {
//            pageListDto.getResultList().forEach(x -> {
//                x.setId(null);
//                x.setRankingReverse(null);
//            });
//        }
//        return pageListDto;
//    }
//
//    /* 当前玩家在本次排行榜活动中的排名
//     * @description
//     * @create by 王星齐
//     * @time 2020-10-04 16:12:35
//     * @param buyerNick
//     **/
//    public long whoRankingWhere(String buyerNick) {
//        SysCustomRanking customRanking = sysCustomRankingRepository.findFirstByBuyerNick(buyerNick);
//        Assert.notNull(customRanking, "不存在的玩家");
//        return sysCustomRankingRepository.whoRankingWhere(customRanking.getRankingReverse(), customRanking.getRankingUpdateTime(), customRanking.getId());
//    }
//}
