package org.indiv.cambridgew.lottery.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.indiv.cambridgew.dto.QualifyDTO;
import org.indiv.cambridgew.lottery.entity.Participant;
import org.indiv.cambridgew.lottery.entity.Qualification;
import org.indiv.cambridgew.lottery.entity.RecordQualification;
import org.indiv.cambridgew.lottery.mapper.ParticipantMapper;
import org.indiv.cambridgew.lottery.mapper.RecordQualificationMapper;
import org.indiv.cambridgew.lottery.validator.QualificationValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 资格服务
 *
 * @author cambridge.w
 * @since 2021/8/4
 */
@Service
public class QualifyService {

    @Resource
    private QualificationValidator validator;
    @Resource
    private ParticipantMapper participantMapper;
    @Resource
    private RecordQualificationMapper recordQualificationMapper;

    /**
     * 资格下发
     *
     * @param dto 资格下发DTO
     */
    public void qualify(QualifyDTO dto) {
        // 校验是否可以下发资格
        Qualification qualification = validator.validate(dto.getActId(), dto.getEventKey(), dto.getUserId());
        // 资格落库

    }

    @Transactional(rollbackFor = Exception.class)
    public void save(Qualification qualification, Long userId, String source) {
        recordQualificationMapper.insert(RecordQualification.builder()
                .qualificationId(qualification.getId())
                .userId(userId)
                .chanceNumber(qualification.getChanceNumber())
                .actId(qualification.getActId())
                .eventKey(qualification.getEventKey()).build());
        LambdaQueryWrapper<Participant> participantQuery = new LambdaQueryWrapper<>();
        participantQuery.eq(Participant::getActId, qualification.getActId())
                .eq(Participant::getQualificationId, qualification.getId())
                .eq(Participant::getUserId, userId);
        if(Optional.ofNullable(participantMapper.selectOne(participantQuery)).isPresent()) {
            // TODO 已存在相同资格, 更新数据
        } else {
            // 不存在资格, 新增数据
            participantMapper.insert(Participant.builder()
                    .qualificationId(qualification.getId())
                    .userId(userId)
                    .currentChanceNumber(qualification.getChanceNumber())
                    .totalChanceNumber(qualification.getChanceNumber())
                    .source(source)
                    .actId(qualification.getActId())
                    .priority(qualification.getPriority()).build());
        }
    }

}
