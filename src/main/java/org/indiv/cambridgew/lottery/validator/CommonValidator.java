package org.indiv.cambridgew.lottery.validator;

import org.indiv.cambridgew.lottery.entity.Qualification;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.indiv.cambridgew.lottery.constant.ErrorMsgConstants.QUALIFICATION_NOT_EXIST;

/**
 * @author cambridge.w
 * @since 2021/8/9
 */
@Component("COMMON")
public class CommonValidator extends AbstractValidator {

    @Override
    public Qualification doValidate(Integer actId, Long userId, List<Qualification> qualifications) {
        return Optional.ofNullable(qualifications)
                .map(item -> {
                    Collections.sort(item);
                    return item;
                })
                .filter(item -> item.size() > 0)
                .map(item -> item.get(0))
                .orElseThrow(() -> new IllegalStateException(QUALIFICATION_NOT_EXIST));
    }

}
