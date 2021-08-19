package org.indiv.cambridgew.lottery.manager;

import com.google.common.cache.LoadingCache;
import org.indiv.cambridgew.lottery.entity.Activity;
import org.indiv.cambridgew.lottery.entity.Prize;
import org.indiv.cambridgew.lottery.manager.common.AliasSampler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static org.indiv.cambridgew.lottery.constant.ErrorMsgConstants.*;

/**
 * 资源管理器
 * <p>缓存在这里定义</p>
 *
 * @author cambridge.w
 * @since 2021/8/17
 */
@Service
public class ResourceManager {

    @Resource
    private LoadingCache<Integer, Activity> activityCache;
    @Resource
    private LoadingCache<Integer, AliasSampler> jackpotCache;
    @Resource
    private LoadingCache<Integer, List<Prize>> prizeMapCache;

    /**
     * 查询活动
     *
     * @param actId 活动Id
     * @return 活动实体
     */
    public Activity findActivity(Integer actId) {
        return Optional.ofNullable(activityCache.getUnchecked(actId))
                .orElseThrow(() -> new IllegalStateException(ACTIVITY_NOT_EXIST));
    }

    /**
     * 查询抽样器
     *
     * @param jackpotId 奖池Id
     * @return 抽样器
     */
    public AliasSampler findAliasSampler(Integer jackpotId) {
        return Optional.ofNullable(jackpotCache.getUnchecked(jackpotId))
                .orElseThrow(() -> new IllegalStateException(ALIAS_SAMPLER_NOT_EXIST));
    }

    /**
     * 查询奖品Id组
     *
     * @param jackpotId 奖池Id
     * @return 奖品组
     */
    public List<Prize> findPrizeIds(Integer jackpotId) {
        return Optional.ofNullable(prizeMapCache.getUnchecked(jackpotId))
                .orElseThrow(() -> new IllegalStateException(JACKPOT_NOT_EXIST));
    }
}
