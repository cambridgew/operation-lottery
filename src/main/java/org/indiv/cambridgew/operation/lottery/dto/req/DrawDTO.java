package org.indiv.cambridgew.operation.lottery.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 抽奖请求DTO
 *
 * @author cambridge.w
 * @since 2021/8/14
 */
@Data
@ApiModel(value = "抽奖请求DTO")
public class DrawDTO {

    @ApiModelProperty(value = "活动Id")
    private Integer actId;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

}
