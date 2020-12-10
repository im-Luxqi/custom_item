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
 * 排行榜 常规操作
 * @description 【帮助类目录】
 *      1.查询排行榜
 *      2.实时查询，当前玩家在本次排行榜活动中的排名
 * @create by 王星齐
 * @date 2020-08-26 16:52
 */
@Slf4j
@Component
public class RankingHelper {

}
