package org.indiv.cambridgew.operation.lottery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indiv.cambridgew.operation.lottery.entity.Prize;
import org.springframework.beans.BeanUtils;

/**
 * 中奖信息详情
 *
 * @author cambridge.w
 * @since 2021/8/14
 */
@Data
@ApiModel(value = "中奖信息详情")
@NoArgsConstructor
public class LotteryDetailDTO {

    @ApiModelProperty(value = "活动Id")
    private Integer actId;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "奖品Id")
    private Integer prizeId;

    @ApiModelProperty(value = "奖品名称")
    private String prizeName;

    @ApiModelProperty(value = "奖品图片地址")
    private String prizeImg;

    @ApiModelProperty(value = "是否是头奖")
    private Boolean firstPrize;

    /**
     * 构造中奖详细信息
     *
     * @param userId 用户Id
     * @param prize  奖品实体
     */
    public LotteryDetailDTO(Long userId, Prize prize) {
        BeanUtils.copyProperties(prize, this);
        this.actId = prize.getActId();
        this.userId = userId;
        this.prizeId = prize.getId();
    }

}
