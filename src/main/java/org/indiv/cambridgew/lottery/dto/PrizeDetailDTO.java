package org.indiv.cambridgew.lottery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 中奖信息详情
 *
 * @author cambridge.w
 * @since 2021/8/14
 */
@Data
@ApiModel(value = "中奖信息详情")
public class PrizeDetailDTO {

    @ApiModelProperty(value = "活动Id")
    private Integer actId;

    @ApiModelProperty(value = "用户Id")
    private Integer userId;

    @ApiModelProperty(value = "奖品Id")
    private Integer prizeId;

    @ApiModelProperty(value = "奖品名称")
    private String prizeName;

    @ApiModelProperty(value = "奖品图片地址")
    private String prizeImg;

    @ApiModelProperty(value = "是否是头奖")
    private Boolean firstPrize;

}
