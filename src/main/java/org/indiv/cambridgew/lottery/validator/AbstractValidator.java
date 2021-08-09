package org.indiv.cambridgew.lottery.validator;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.indiv.cambridgew.lottery.entity.Participant;
import org.indiv.cambridgew.lottery.entity.Qualification;
import org.indiv.cambridgew.lottery.mapper.ParticipantMapper;
import org.indiv.cambridgew.lottery.mapper.QualificationMapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 抽象通用校验
 *
 * @author cambridge.w
 * @since 2021/8/5
 */
public abstract class AbstractValidator implements QualificationValidator.Validator {

    @Resource
    private QualificationMapper qualificationMapper;
    @Resource
    private ParticipantMapper participantMapper;

    @Override
    public Qualification validate(Integer actId, Long userId) {
        LambdaQueryWrapper<Participant> participantQuery = new LambdaQueryWrapper<>();
        participantQuery.eq(Participant::getActId, actId)
                .eq(Participant::getUserId, userId);
        List<Participant> participants = participantMapper.selectList(participantQuery);
        List<Qualification> c = qualificationMapper.selectBatchIds(participants.stream().map(Participant::getQualificationId).collect(Collectors.toList()));
        c = doTimeValidate(c);
        return doValidate(actId, userId, c);
    }

    public abstract Qualification doValidate(Integer actId, Long userId, List<Qualification> qualifications);

    /**
     * 校验时间
     *
     * @param qualifications 参与者获得的所有资格
     * @return 满足时间校验的资格
     */
    private List<Qualification> doTimeValidate(List<Qualification> qualifications) {
        LocalDateTime now = LocalDateTime.now();
        return Optional.ofNullable(qualifications)
                .orElse(Collections.emptyList())
                .stream()
                .filter(item -> !now.isBefore(item.getStartTime()) && !now.isAfter(item.getEndTime()))
                .collect(Collectors.toList());
    }

}
