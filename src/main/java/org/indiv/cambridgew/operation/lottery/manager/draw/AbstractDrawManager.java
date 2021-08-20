package org.indiv.cambridgew.operation.lottery.manager.draw;

import org.indiv.cambridgew.operation.lottery.entity.Participant;
import org.indiv.cambridgew.operation.lottery.entity.Prize;
import org.indiv.cambridgew.operation.lottery.manager.ResourceManager;
import org.springframework.lang.NonNull;

import javax.annotation.Resource;

/**
 * @author cambridge.w
 * @since 2021/8/16
 */
public abstract class AbstractDrawManager implements DrawManager.DrawExecutor {

    @Resource
    private ResourceManager resourceManager;

    @Override
    public Prize draw(Integer actId, Long userId, @NonNull Participant participant) {
        Integer jackpotId = determineJackpot(actId, userId, participant);
        return doDrawOperation(jackpotId);
    }

    /**
     * 确定用户抽奖所在的奖池(根据用户抽奖次数或资格类型)
     *
     * @param actId  活动Id
     * @param userId 用户Id
     * @return 用户抽奖所在奖池
     */
    public abstract Integer determineJackpot(Integer actId, Long userId, Participant participant);

    /**
     * 执行抽奖动作
     * @param jackpotId 奖池Id
     * @return 中奖奖品实体
     */
    private Prize doDrawOperation(Integer jackpotId) {
        return resourceManager.findPrizeIds(jackpotId).get(resourceManager.findAliasSampler(jackpotId).sample());
    }

}
