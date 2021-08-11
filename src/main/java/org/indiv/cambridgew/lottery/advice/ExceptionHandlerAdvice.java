package org.indiv.cambridgew.lottery.advice;

import lombok.extern.slf4j.Slf4j;
import org.indiv.cambridgew.lottery.common.Result;
import org.indiv.cambridgew.lottery.common.ResultCode;
import org.slf4j.event.Level;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.indiv.cambridgew.lottery.common.ResultCode.WARN0003;
import static org.indiv.cambridgew.lottery.common.ResultCode.parseLevel;

/**
 * 统一异常处理
 * Warn:不影响业务流程
 * Error:业务流程无法继续
 *
 * @author cambridge.w
 * @since 2021/8/10
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(IllegalStateException.class)
    public Result<Object> stateExceptionHandler(IllegalStateException ex) {
        return handle(WARN0003, ex, true);
    }

    /**
     * 统一处理
     *
     * @param code          响应状态码
     * @param ex            异常
     * @param showException 是否用异常信息做展示
     * @return 统一响应
     */
    private Result<Object> handle(ResultCode code, Exception ex, boolean showException) {
        Result<Object> result = showException
                ? Result.failure(code, ex.getMessage(), code.getDefaultMessage())
                : Result.failure(code, code.getDefaultMessage(), ex.getMessage());
        Level level = parseLevel(code);
        printExceptionLog(result, ex, level);
        return result;
    }

    private void printExceptionLog(Result<Object> result, Exception ex, Level level) {
        String msg = result.toString();
        switch (level) {
            case INFO:
                log.info(msg, ex);
                break;
            case WARN:
                log.warn(msg, ex);
                break;
            default:
                log.error(msg, ex);
        }
    }

}
