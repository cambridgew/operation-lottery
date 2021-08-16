package org.indiv.cambridgew.lottery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.indiv.cambridgew.lottery.common.BaseEntity;

import java.time.LocalDateTime;

/**
 * 活动信息
 *
 * @author cambridge.w
 * @since 2021/8/4
 */
@Data
@TableName(value = "tb_activity")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Activity extends BaseEntity {

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动说明, 描述信息
     */
    private String description;

    /**
     * 活动状态
     * invalid - 无效
     * active - 有效
     */
    private String status;

    /**
     * 抽奖执行方式(按照资格/按照次数)
     */
    private String drawOperationType;

    /**
     * 活动开始时间
     */
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime endTime;

    /**
     * 抽奖开始时间
     */
    private LocalDateTime drawStartTime;

    /**
     * 抽奖结束时间(多数时候同活动结束时间)
     */
    private LocalDateTime drawEndTime;

}
