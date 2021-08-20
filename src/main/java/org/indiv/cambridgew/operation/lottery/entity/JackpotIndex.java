package org.indiv.cambridgew.operation.lottery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.indiv.cambridgew.poseidon.core.entity.BaseEntity;

/**
 * 奖池索引信息, 标识触发某资格事件时, 在某抽奖次数区间内所抽奖的奖池
 *
 * @author cambridge.w
 * @since 2021/8/4
 */
@Data
@TableName(value = "tb_jackpot_index")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JackpotIndex extends BaseEntity {

    /**
     * 资格Id
     */
    private Integer qualificationId;

    /**
     * 奖池Id
     */
    private Integer jackpotId;

    /**
     * 抽奖起始次数
     */
    private Integer stageFrom;

    /**
     * 抽奖结束次数
     */
    private Integer stageTo;

    /**
     * 活动Id-冗余
     */
    private Integer actId;

}
