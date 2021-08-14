package org.indiv.cambridgew.lottery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.indiv.cambridgew.lottery.common.BaseEntity;

/**
 * 奖池信息
 *
 * @author cambridge.w
 * @since 2021/8/4
 */
@Data
@TableName(value = "tb_jackpot")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Jackpot extends BaseEntity {

}
