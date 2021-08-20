package org.indiv.cambridgew.lottery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.indiv.cambridgew.lottery.common.RecordQualificationOperationEnum;
import org.indiv.cambridgew.poseidon.core.entity.BaseEntity;

/**
 * 资格流水记录
 *
 * @author cambridge.w
 * @since 2021/8/10
 */
@Data
@TableName(value = "tb_record_qualification")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordQualification extends BaseEntity {

    // 资格Id
    private Integer qualificationId;

    // 用户Id
    private Long userId;

    // 本次资格下发数量
    private Integer chanceNumber;

    // 来源记录
    private String source;

    // 操作类型(资格下发/资格回撤)
    private RecordQualificationOperationEnum operation;

    // 活动Id-冗余
    private Integer actId;

    // 资格名称-冗余
    private String eventKey;

}
