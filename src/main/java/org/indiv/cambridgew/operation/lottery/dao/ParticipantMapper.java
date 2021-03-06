package org.indiv.cambridgew.operation.lottery.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.indiv.cambridgew.operation.lottery.entity.Participant;

/**
 * @author cambridge.w
 * @since 2021/8/9
 */
@Mapper
public interface ParticipantMapper extends BaseMapper<Participant> {
}
