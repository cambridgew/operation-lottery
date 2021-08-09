package org.indiv.cambridgew.lottery.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础实体字段
 *
 * @author cambridge.w
 * @since 2021/8/4
 */
@Data
public class BaseEntity {

    @TableId
    @ApiModelProperty(value = "自增Id")
    protected Integer id;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间", hidden = true)
    protected LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间", hidden = true)
    protected LocalDateTime updateTime;

    @TableLogic
    @ApiModelProperty(value = "删除标记", hidden = true)
    protected Integer deleted;

}
