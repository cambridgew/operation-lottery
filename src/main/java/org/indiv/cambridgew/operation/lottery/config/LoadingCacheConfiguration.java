package org.indiv.cambridgew.operation.lottery.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.indiv.cambridgew.operation.lottery.dao.ActivityMapper;
import org.indiv.cambridgew.operation.lottery.dao.PrizeMapper;
import org.indiv.cambridgew.operation.lottery.entity.Activity;
import org.indiv.cambridgew.operation.lottery.entity.Prize;
import org.indiv.cambridgew.operation.lottery.manager.common.AliasSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.indiv.cambridgew.operation.lottery.constant.ErrorMsgConstants.JACKPOT_NOT_EXIST;

/**
 * @author cambridge.w
 * @since 2021/8/4
 */
@Configuration
public class LoadingCacheConfiguration {

    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private PrizeMapper prizeMapper;

    /**
     * 活动信息本地缓存
     */
    @Bean(name = "activityCache")
    public LoadingCache<Integer, Activity> createActivityLocalCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .initialCapacity(16)
                .build(new CacheLoader<Integer, Activity>() {
                    @Override
                    public Activity load(@NotNull Integer key) {
                        return activityMapper.selectById(key);
                    }
                });
    }

    /**
     * 奖品在奖池内顺序映射关系缓存
     * <p>供抽样器使用</p>
     * <p>奖池不发生改变的情况下缓存在本地即可</p>
     */
    @Bean(name = "prizeMapCache")
    public LoadingCache<Integer, List<Prize>> createPrizeMapLocalCache() {
        return CacheBuilder.newBuilder()
                .initialCapacity(32)
                .build(new CacheLoader<Integer, List<Prize>>() {
                    @Override
                    public List<Prize> load(@NonNull Integer key) {
                        return Optional.ofNullable(prizeMapper.selectList(Wrappers.<Prize>lambdaQuery()
                                .eq(Prize::getJackpotId, key)
                                .orderByAsc(Prize::getId)))
                                .filter(item -> item.size() > 0)
                                .orElseThrow(() -> new IllegalStateException(JACKPOT_NOT_EXIST));
                    }
                });
    }

    /**
     * 奖池缓存
     * <p>由于抽样器的生成规则统一, 故奖池不发生改变的情况下缓存在本地即可</p>
     */
    @Bean(name = "jackpotCache")
    public LoadingCache<Integer, AliasSampler> createJackpotLocalCache() {
        return CacheBuilder.newBuilder()
                .initialCapacity(16)
                .build(new CacheLoader<Integer, AliasSampler>() {
                    @Override
                    public AliasSampler load(@NonNull Integer key) {
                        List<Double> probabilities = Optional.ofNullable(prizeMapper.selectList(Wrappers.<Prize>lambdaQuery()
                                .eq(Prize::getJackpotId, key)
                                .orderByAsc(Prize::getId)))
                                .filter(item -> item.size() > 0)
                                .orElseThrow(() -> new IllegalStateException(JACKPOT_NOT_EXIST))
                                .stream()
                                .map(Prize::getProbability)
                                .collect(Collectors.toList());
                        return new AliasSampler(probabilities);
                    }
                });
    }

}
