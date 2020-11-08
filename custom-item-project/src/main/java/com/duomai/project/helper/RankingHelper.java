package com.duomai.project.helper;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.project.configuration.annotation.JoinMemcache;
import com.duomai.project.product.general.dto.PageListDto;
import com.duomai.project.product.general.entity.SysCustomRanking;
import com.duomai.project.product.general.repository.SysCustomRankingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * 排行榜Helper 常规操作
 * @description 【帮助类目录】
 *      1.查询排行榜
 *      2.实时查询，当前玩家在本次排行榜活动中的排名
 * @create by 王星齐
 * @date 2020-08-26 16:52
 */
@Slf4j
@Component
public class RankingHelper {

    @Autowired
    private SysCustomRankingRepository sysCustomRankingRepository;


    /**
     * 1.查询排行榜
     *
     * @param rankSize 排行榜前多少名
     * @description
     * @create by 王星齐
     * @time 2020-11-08 18:28:06
     */
    @JoinMemcache
    public PageListDto<SysCustomRanking> rankingList(int rankSize) {
        PageListDto<SysCustomRanking> pageListDto = new PageListDto<>();
        pageListDto.setPageSize(rankSize);
        Page<SysCustomRanking> ranking = sysCustomRankingRepository.ranking(pageListDto.startJPAPage());
        pageListDto.setJpaResultList(ranking);
        if (CollectionUtils.isNotEmpty(pageListDto.getResultList())) {
            pageListDto.getResultList().forEach(x -> {
                x.setId(null);
                x.setRankingReverse(null);
            });
        }
        return pageListDto;
    }

    /* 2.实时查询，当前玩家在本次排行榜活动中的排名
     * @description
     *     当参与人数过多时(>100w)，不建议使用
     * @create by 王星齐
     * @time 2020-10-04 16:12:35
     * @param buyerNick
     **/
    public long whoRankingWhere(SysCustomRanking customRanking) {
        return sysCustomRankingRepository.whoRankingWhere(customRanking.getRankingReverse(), customRanking.getRankingUpdateTime(), customRanking.getId());
    }
}
