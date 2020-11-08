//package com.duomai.project.helper;
//
//import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
//import com.duomai.project.api.taobao.MemCacheData;
//import com.duomai.project.api.taobao.MemcacheTools;
//import com.duomai.project.product.general.dto.PageListDto;
//import com.duomai.project.product.setbox.domain.SysCustomRanking;
//import com.duomai.project.product.setbox.repository.SysCustomRankingRepository;
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
//    final static String ranking_key = "_RANKING_";
//    final static String lock_key = "_lock_key_";
//    final static int lock_time = 100;
//    final static int ranking_exp = 5;
//
//    @Autowired
//    private SysCustomRankingRepository sysCustomRankingRepository;
//
//
//    /* 排行榜 (前端需要传pageSize 作为 所查询的前多少名玩家的排行)
//     * @description   如pageSize=50，则表示前50的排行榜，100则表示前100
//     * @create by 王星齐
//     * @time 2020-10-04 16:04:31
//     * @param sysParm
//     **/
//    public PageListDto<SysCustomRanking> rankingData(int rankSize) throws InterruptedException {
//        if (!ProjectTools.hasMemCacheEnvironment()) {
//            return this.rankingDbData(rankSize);
//        }
//        MemCacheData<PageListDto<SysCustomRanking>> memcache_ranking = MemcacheTools.loadData(ranking_key);
//        if (Objects.isNull(memcache_ranking)) {
//            if (MemcacheTools.add(lock_key, lock_time)) {
//                PageListDto<SysCustomRanking> dbData = this.rankingDbData(rankSize);
//                MemcacheTools.cacheData(ranking_key, new MemCacheData<PageListDto<SysCustomRanking>>(ranking_exp - 1).setData(dbData), ranking_exp);
//                MemcacheTools.cleanData(lock_key);
//            } else {
//                Thread.sleep(50);
//                this.rankingData(rankSize);
//            }
//        } else if (memcache_ranking.getTimeout() <= new Date().getTime()) {
//            if (MemcacheTools.add(lock_key, lock_time)) {
//                MemcacheTools.cacheData(ranking_key, new MemCacheData<PageListDto<SysCustomRanking>>(ranking_exp - 1).setData(memcache_ranking.getData()), ranking_exp);
//                PageListDto<SysCustomRanking> dbData = this.rankingDbData(rankSize);
//                MemcacheTools.cacheData(ranking_key, new MemCacheData<PageListDto<SysCustomRanking>>(ranking_exp - 1).setData(dbData), ranking_exp);
//                MemcacheTools.cleanData(lock_key);
//            } else {
//                Thread.sleep(50);
//                this.rankingData(rankSize);
//            }
//        }
//        memcache_ranking = MemcacheTools.loadData(ranking_key);
//        return memcache_ranking.getData();
//    }
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
