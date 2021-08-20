package org.indiv.cambridgew.operation.lottery.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资格查询DTO
 *
 * @author cambridge.w
 * @since 2021/8/14
 */
@Data
@ApiModel(value = "资格查询DTO")
public class QualificationQueryDTO {

    @ApiModelProperty(value = "活动Id")
    private Integer actId;

    @ApiModelProperty(value = "资格Id")
    private Integer qualificationId;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

}
