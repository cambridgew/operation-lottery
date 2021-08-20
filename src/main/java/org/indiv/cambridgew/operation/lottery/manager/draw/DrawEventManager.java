package org.indiv.cambridgew.operation.lottery.manager.draw;

import org.indiv.cambridgew.operation.lottery.entity.Participant;
import org.springframework.stereotype.Component;

/**
 * @author cambridge.w
 * @since 2021/8/16
 */
@Component("drawEvent")
public class DrawEventManager extends AbstractDrawManager{

    @Override
    public Integer determineJackpot(Integer actId, Long userId, Participant participant) {
        return participant.getJackpotId();
    }

}
