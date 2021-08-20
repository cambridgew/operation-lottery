package org.indiv.cambridgew.operation.lottery.manager.qualify;

import org.indiv.cambridgew.operation.lottery.entity.Qualification;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.indiv.cambridgew.operation.lottery.constant.ErrorMsgConstants.QUALIFICATION_NOT_EXIST;

/**
 * @author cambridge.w
 * @since 2021/8/9
 */
@Component("common")
public class CommonManager extends AbstractManager {

    @Override
    public Qualification doValidate(Qualification qualification, Long userId) {
        return Optional.ofNullable(qualification)
                .orElseThrow(() -> new IllegalStateException(QUALIFICATION_NOT_EXIST));
    }

}
