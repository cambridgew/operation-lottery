package org.indiv.cambridgew.lottery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.indiv.cambridgew.lottery.common.BaseEntity;

import java.time.LocalDateTime;

/**
 * 资格
 * 相同活动下同一资格类型的资格仅可唯一存在
 *
 * @author cambridge.w
 * @since 2021/8/4
 */
@Data
@TableName(value = "tb_qualification")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Qualification extends BaseEntity implements Comparable<Qualification> {

    // 活动Id
    private Integer actId;

    // 资格事件
    private String eventKey;

    // 奖池Id
    private Integer jackpotId;

    // 资格开始时间
    private LocalDateTime startTime;

    // 资格结束时间
    private LocalDateTime endTime;

    // 所依赖的资格, 资格Id","隔开
    private String dependents;

    // 满足资格单次下发抽奖次数
    private Integer chanceNumber;

    // 单人获取资格上限
    private Integer singleLimit;

    // 单人每日获取资格上限
    private Integer singleDailyLimit;

    // 累计获取资格上限
    private Integer totalLimit;

    // 累计每日获取资格上限
    private Integer totalDailyLimit;

    // 资格消耗优先级
    private Integer priority;

    // 补充条件-json格式
    private String ext;

    @Override
    public int compareTo(Qualification qualification) {
        return this.priority - qualification.getPriority();
    }

}
