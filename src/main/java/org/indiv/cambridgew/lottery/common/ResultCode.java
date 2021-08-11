package org.indiv.cambridgew.lottery.common;

import lombok.Getter;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static org.slf4j.event.Level.*;

/**
 * 结果码
 *
 * @author cambridge.w
 * @since 2021/8/11
 */
@Getter
public enum ResultCode {

    SUCCESS0000("一切OK"),

    WARN0003("请求状态不合法"),

    ERROR0500("系统内部异常");

    private final String defaultMessage;

    ResultCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getCode() {
        return name().substring(name().length() - 4);
    }

    /**
     * 解析日志级别
     *
     * @param code 错误码字符串
     * @return 日志级别 默认{@link Level#ERROR}
     */
    @NonNull
    public static Level parseLevel(@Nullable ResultCode code) {
        if (null != code) {
            if (code.name().contains("SUCCESS")) return INFO;
            if (code.name().contains("WARN")) return WARN;
            if (code.name().contains("ERROR")) return ERROR;
        }
        return ERROR;
    }

}
