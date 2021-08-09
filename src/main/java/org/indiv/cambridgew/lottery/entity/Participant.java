package org.indiv.cambridgew.lottery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.indiv.cambridgew.lottery.common.BaseEntity;

/**
 * 活动参与者(设计上实际为qualification和用户的关联关系表, 由于用户信息无需做冗余记录, 故合并为同一张表)
 *
 * @author cambridge.w
 * @since 2021/8/9
 */
@Data
@TableName(value = "tb_participant")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Participant extends BaseEntity {

    // 资格Id
    private Integer qualificationId;

    // 用户Id
    private Long userId;

    // 当前资格剩余抽奖次数
    private Integer currentChanceNumber;

    // 当前资格总抽奖次数
    private Integer totalChanceNumber;

    // 活动Id-冗余
    private Integer actId;

    // 资格消耗优先级-冗余
    private Integer priority;

}
