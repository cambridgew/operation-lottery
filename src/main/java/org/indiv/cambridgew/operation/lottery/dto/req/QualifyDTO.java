package org.indiv.cambridgew.operation.lottery.dto.req;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资格下发DTO
 *
 * @author cambridge.w
 * @since 2021/8/10
 */
@Data
@ApiModel(value = "资格下发DTO")
public class QualifyDTO {

    @ApiModelProperty(value = "活动Id")
    private Integer actId;

    @ApiModelProperty(value = "资格事件")
    private String eventKey;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "补充信息")
    private JSONObject ext;

}
