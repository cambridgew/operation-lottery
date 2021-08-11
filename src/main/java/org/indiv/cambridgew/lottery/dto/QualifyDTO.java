package org.indiv.cambridgew.lottery.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 资格下发DTO
 *
 * @author cambridge.w
 * @since 2021/8/10
 */
@Data
public class QualifyDTO {

    // 活动Id
    private Integer actId;

    // 资格事件
    private String eventKey;

    // 用户Id
    private Long userId;

    // 来源
    private String source;

    // 补充信息
    private JSONObject ext;
}
