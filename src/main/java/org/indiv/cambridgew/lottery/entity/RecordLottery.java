package org.indiv.cambridgew.lottery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.indiv.cambridgew.lottery.common.BaseEntity;

/**
 * 抽奖流水记录
 *
 * @author cambridge.w
 * @since 2021/8/10
 */
@Data
@TableName(value = "tb_record_lottery")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecordLottery extends BaseEntity {

    // 本次抽奖消耗的资格Id
    private Integer qualificationId;

    // 用户Id
    private Long userId;

    // 奖池Id
    private Integer jackpotId;

    // 奖品Id
    private Integer prizeId;

    // 活动Id-冗余
    private Integer actId;

    // 资格名称-冗余
    private String eventKey;

}
