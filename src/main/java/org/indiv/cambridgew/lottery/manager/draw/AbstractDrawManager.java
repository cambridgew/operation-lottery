package org.indiv.cambridgew.lottery.manager.draw;

import org.indiv.cambridgew.lottery.entity.Participant;
import org.springframework.lang.NonNull;

/**
 * @author cambridge.w
 * @since 2021/8/16
 */
public abstract class AbstractDrawManager implements DrawManager.DrawExecutor {

    @Override
    public Integer draw(Integer actId, Long userId, @NonNull Participant participant) {
        Integer jackpotId = determineJackpot(actId, userId, participant);

        return 0;
    }

    /**
     * 确定用户抽奖所在的奖池(根据用户抽奖次数或资格类型)
     *
     * @param actId  活动Id
     * @param userId 用户Id
     * @return 用户抽奖所在奖池
     */
    public abstract Integer determineJackpot(Integer actId, Long userId, Participant participant);

}
