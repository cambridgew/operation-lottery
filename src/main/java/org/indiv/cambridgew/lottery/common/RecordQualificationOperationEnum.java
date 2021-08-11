package org.indiv.cambridgew.lottery.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author cambridge.w
 * @since 2021/8/11
 */
@Getter
public enum RecordQualificationOperationEnum {

    // 资格下发
    QUALIFY("qualify"),

    // 资格回撤
    ROLLBACK("rollback"),

    ;

    @JsonValue
    @EnumValue
    private final String key;

    RecordQualificationOperationEnum(String key) {
        this.key = key;
    }

}
