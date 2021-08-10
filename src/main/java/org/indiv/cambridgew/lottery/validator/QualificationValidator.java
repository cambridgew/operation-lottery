package org.indiv.cambridgew.lottery.validator;

import org.indiv.cambridgew.lottery.entity.Qualification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.indiv.cambridgew.lottery.constant.ErrorMsgConstants.EVENT_KEY_NOT_EXIST;
import static org.springframework.util.Assert.isTrue;

/**
 * 资格校验-根据不同的资格类型执行不同的资格校验策略
 *
 * @author cambridge.w
 * @since 2021/8/4
 */
@Service
public class QualificationValidator {

    @Autowired
    private Map<String, Validator> strategy = new HashMap<>(16);

    public Qualification validate(Integer actId, String eventKey, Long userId) {
        isTrue(strategy.containsKey(eventKey), EVENT_KEY_NOT_EXIST);
        return strategy.get(eventKey).validate(actId, eventKey, userId);
    }

    @FunctionalInterface
    public interface Validator {
        /**
         * 校验过滤用户抽奖资格
         *
         * @param actId    活动Id
         * @param eventKey 资格事件
         * @param userId   用户Id
         * @return 返回用户可用资格, 也即本次抽奖所消耗的资格, 若用户无可用资格, 则返回null
         */
        Qualification validate(Integer actId, String eventKey, Long userId);
    }

}
