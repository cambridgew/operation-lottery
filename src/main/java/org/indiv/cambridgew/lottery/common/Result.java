package org.indiv.cambridgew.lottery.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * @author cambridge.w
 * @since 2021/8/11
 */
@Data
@ApiModel(description = "返回结果")
public class Result<T> {

    @ApiModelProperty(value = "结果码")
    private String code;

    @ApiModelProperty(value = "结果信息")
    private String message;

    @ApiModelProperty(value = "用户提示信息")
    private String noticeMsg;

    @ApiModelProperty(value = "结果数据")
    private T data;

    private Result(ResultCode resultCode, @Nullable String message, @Nullable String noticeMsg, @Nullable T data) {
        this.code = resultCode.getCode();
        this.message = Optional.ofNullable(message).orElse(resultCode.getDefaultMessage());
        this.noticeMsg = noticeMsg;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return Result.success(ResultCode.SUCCESS0000, data);
    }

    public static <T> Result<T> success(ResultCode resultCode, T data) {
        return new Result<>(resultCode, resultCode.getDefaultMessage(), null, data);
    }

    public static <T> Result<T> failure(ResultCode resultCode, @Nullable String message, @Nullable String reason) {
        return new Result<>(resultCode, message, reason, null);
    }

}
