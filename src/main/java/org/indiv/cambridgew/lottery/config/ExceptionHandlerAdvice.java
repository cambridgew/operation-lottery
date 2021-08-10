package org.indiv.cambridgew.lottery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
