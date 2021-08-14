package org.indiv.cambridgew.lottery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.indiv.cambridgew.lottery.common.BaseEntity;

/**
 * 奖品信息-对应到某个活动中的奖品信息
 *
 * @author cambridge.w
 * @since 2021/8/14
 */
@Data
@TableName(value = "tb_prize")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Prize extends BaseEntity {

    // 奖池Id
    private Integer jackpotId;

    // 商品Id(可能是虚拟物或实体物, 可充当奖品)
    private Integer itemId;

    // 中奖概率
    private Double probability;

    // 奖品名称
    private String prizeName;

    // 奖品图片地址
    private String prizeImg;

    // 头奖/大奖
    private Boolean firstPrize;

    // 奖品数量上限
    private Integer limit;

    // 活动Id-冗余字段
    private Integer actId;

}
