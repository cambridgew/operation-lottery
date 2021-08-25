package org.indiv.cambridgew.operation.lottery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indiv.cambridgew.operation.lottery.entity.Prize;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @ApiModelProperty(value = "奖品详情列表")
    private List<PrizeDetailDTO> prizeDetailDTOList;

    @Data
    private static class PrizeDetailDTO {

        @ApiModelProperty(value = "奖品Id")
        private Integer prizeId;

        @ApiModelProperty(value = "奖品名称")
        private String prizeName;

        @ApiModelProperty(value = "奖品图片地址")
        private String prizeImg;

        @ApiModelProperty(value = "是否是头奖")
        private Boolean firstPrize;

    }

    /**
     * 构造中奖详细信息(单个奖品)
     *
     * @param userId 用户Id
     * @param prize  奖品实体
     */
    public LotteryDetailDTO(Long userId, @NotNull Prize prize) {
        PrizeDetailDTO prizeDetailDTO = new PrizeDetailDTO();
        BeanUtils.copyProperties(prize, prizeDetailDTO);
        prizeDetailDTO.setPrizeId(prize.getId());
        this.actId = prize.getActId();
        this.userId = userId;
        this.prizeDetailDTOList = new ArrayList<>(Collections.singletonList(prizeDetailDTO));
    }

    /**
     * 构造中奖详细信息(多个奖品)
     *
     * @param userId    用户Id
     * @param prizeList 奖品实体列表
     */
    public LotteryDetailDTO(Long userId, @NotNull List<Prize> prizeList) {
        List<PrizeDetailDTO> prizeDetailDTOList = new ArrayList<>();
        prizeList.forEach(p -> {
            PrizeDetailDTO prizeDetailDTO = new PrizeDetailDTO();
            BeanUtils.copyProperties(p, prizeDetailDTO);
            prizeDetailDTO.setPrizeId(p.getId());
            prizeDetailDTOList.add(prizeDetailDTO);
        });
        this.actId = Optional.ofNullable(prizeList.get(0))
                .map(Prize::getActId)
                .orElseThrow(IllegalArgumentException::new);
        this.userId = userId;
        this.prizeDetailDTOList = prizeDetailDTOList;
    }

}
