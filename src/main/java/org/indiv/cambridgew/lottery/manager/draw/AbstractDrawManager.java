package org.indiv.cambridgew.lottery.manager.draw;

/**
 * @author cambridge.w
 * @since 2021/8/16
 */
public abstract class AbstractDrawManager implements DrawManager.DrawExecutor {

    @Override
    public Integer draw(Integer actId, Long userId) {
        return 0;
    }

}
