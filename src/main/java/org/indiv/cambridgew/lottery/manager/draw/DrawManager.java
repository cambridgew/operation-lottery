package org.indiv.cambridgew.lottery.manager.draw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.indiv.cambridgew.lottery.constant.ErrorMsgConstants.DRAW_OPERATION_NOT_EXIST;
import static org.springframework.util.Assert.isTrue;

/**
 * 抽奖管理器
 *
 * @author cambridge.w
 * @since 2021/8/14
 */
@Service
public class DrawManager {

    @Autowired
    private Map<String, DrawExecutor> strategy = new HashMap<>(16);

    public Integer draw(Integer actId, String drawOperationType, Long userId) {
        isTrue(strategy.containsKey(drawOperationType), DRAW_OPERATION_NOT_EXIST);
        return strategy.get(drawOperationType).draw(actId, userId);
    }

    @FunctionalInterface
    public interface DrawExecutor {

        /**
         * 抽奖
         *
         * @param actId             活动Id
         * @param userId            用户Id
         * @return 抽中的奖品Id
         */
        Integer draw(Integer actId, Long userId);
    }

}
