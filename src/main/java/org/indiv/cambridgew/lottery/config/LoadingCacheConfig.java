package org.indiv.cambridgew.lottery.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.indiv.cambridgew.lottery.dao.ActivityMapper;
import org.indiv.cambridgew.lottery.entity.Activity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author cambridge.w
 * @since 2021/8/4
 */
@Configuration
public class LoadingCacheConfig {

    @Resource
    private ActivityMapper activityMapper;

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
                    public Activity load(Integer key) {
                        LambdaQueryWrapper<Activity> query = new LambdaQueryWrapper<>();
                        query.eq(Activity::getId, key);
                        return activityMapper.selectOne(query);
                    }
                });
    }

}
