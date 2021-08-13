package org.indiv.cambridgew.lottery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户资格详情
 *
 * @author cambridge.w
 * @since 2021/8/11
 */
@Data
@ApiModel(value = "用户资格详情")
public class QualificationDetailDTO {

    @ApiModelProperty(value = "活动Id")
    private Integer actId;

}
