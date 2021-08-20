package org.indiv.cambridgew.operation.lottery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.indiv.cambridgew.poseidon.core.entity.BaseEntity;

/**
 * 活动参与者资格(设计上实际为qualification和用户的关联关系表, 由于用户信息无需做冗余记录, 故合并为同一张表)
 * 同一用户获取相同的资格(同一资格Id, 不区分来源source)合并在同一条记录中
 *
 * @author cambridge.w
 * @since 2021/8/9
 */
@Data
@TableName(value = "tb_participant")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant extends BaseEntity {

    // 资格Id
    private Integer qualificationId;

    // 用户Id
    private Long userId;

    // 当前资格剩余抽奖次数
    private Integer currentChanceNumber;

    // 当前资格总抽奖次数
    private Integer totalChanceNumber;

    // 状态, 用于标识是否正在抽奖中
    private String status;

    // 活动Id-冗余
    private Integer actId;

    // 资格消耗优先级-冗余
    private Integer priority;

    // 资格Id对应奖池Id-冗余
    private Integer jackpotId;

}
