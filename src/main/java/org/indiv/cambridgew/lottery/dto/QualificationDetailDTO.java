package org.indiv.cambridgew.lottery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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

    private Long userId;

    private List<QualificationDetail> qualificationDetails;

    @Data
    public static class QualificationDetail {
        // 资格Id
        private Integer qualificationId;

        // 当前资格剩余抽奖次数
        private Integer currentChanceNumber;

        // 当前资格总抽奖次数
        private Integer totalChanceNumber;

        // 资格消耗优先级-冗余
        private Integer priority;
    }

}
