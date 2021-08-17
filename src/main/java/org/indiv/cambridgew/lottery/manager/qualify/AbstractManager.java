package org.indiv.cambridgew.lottery.manager.qualify;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.indiv.cambridgew.lottery.common.RecordQualificationOperationEnum;
import org.indiv.cambridgew.lottery.dao.ParticipantMapper;
import org.indiv.cambridgew.lottery.dao.QualificationMapper;
import org.indiv.cambridgew.lottery.dao.RecordQualificationMapper;
import org.indiv.cambridgew.lottery.entity.Participant;
import org.indiv.cambridgew.lottery.entity.Qualification;
import org.indiv.cambridgew.lottery.entity.RecordQualification;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.indiv.cambridgew.lottery.constant.ErrorMsgConstants.*;
import static org.springframework.util.Assert.isTrue;

/**
 * @author cambridge.w
 * @since 2021/8/5
 */
public abstract class AbstractManager implements QualificationManager.Validator {

    @Resource
    private QualificationMapper qualificationMapper;
    @Resource
    private ParticipantMapper participantMapper;
    @Resource
    private RecordQualificationMapper recordQualificationMapper;

    @Override
    public Qualification validate(Integer actId, String eventKey, Long userId) {
        LambdaQueryWrapper<Qualification> qualificationQuery = new LambdaQueryWrapper<>();
        qualificationQuery.eq(Qualification::getActId, actId)
                .eq(Qualification::getEventKey, eventKey);
        Qualification qualification = qualificationMapper.selectOne(qualificationQuery);
        isTrue(null != qualification, EVENT_KEY_NOT_SUPPORTED_IN_ACTIVITY);

        qualification = doValidate(doTimeValidate(qualification), userId);
        checkLimit(qualification, userId);
        checkDependents(qualification, userId);
        return qualification;
    }

    public abstract Qualification doValidate(Qualification qualification, Long userId) throws IllegalStateException;

    /**
     * 校验时间
     *
     * @param qualification 参与者将获得的资格
     * @return 满足时间校验的资格
     */
    private Qualification doTimeValidate(Qualification qualification) {
        LocalDateTime now = LocalDateTime.now();
        return Optional.ofNullable(qualification)
                .filter(item -> !now.isBefore(item.getStartTime()) && !now.isAfter(item.getEndTime()))
                .orElseThrow(() -> new IllegalStateException(EVENT_KEY_INVALID));
    }

    /**
     * 校验资格限制
     *
     * @param qualification 资格
     * @param userId        用户Id
     */
    private void checkLimit(Qualification qualification, Long userId) {
        LocalDateTime now = LocalDateTime.now();

        // 校验单人每日资格获取上限
        Optional.ofNullable(qualification.getSingleDailyLimit())
                .ifPresent(singleDailyLimit -> {
                    LambdaQueryWrapper<RecordQualification> singleDailyLimitQuery = new LambdaQueryWrapper<>();
                    singleDailyLimitQuery.eq(RecordQualification::getActId, qualification.getActId())
                            .eq(RecordQualification::getQualificationId, qualification.getId())
                            .eq(RecordQualification::getUserId, userId)
                            .eq(RecordQualification::getOperation, RecordQualificationOperationEnum.QUALIFY)
                            .ge(RecordQualification::getCreateTime, LocalDateTime.of(now.toLocalDate(), LocalTime.MIN))
                            .le(RecordQualification::getCreateTime, LocalDateTime.of(now.toLocalDate(), LocalTime.MAX));
                    Integer singleDailyCount = recordQualificationMapper.selectCount(singleDailyLimitQuery);
                    isTrue(singleDailyCount < singleDailyLimit, OUT_OF_SINGLE_DAILY_QUALIFICATION_LIMIT);
                });

        // 校验单人累计资格获取上限
        Optional.ofNullable(qualification.getSingleLimit())
                .ifPresent(singleLimit -> {
                    LambdaQueryWrapper<RecordQualification> singleLimitQuery = new LambdaQueryWrapper<>();
                    singleLimitQuery.eq(RecordQualification::getActId, qualification.getActId())
                            .eq(RecordQualification::getQualificationId, qualification.getId())
                            .eq(RecordQualification::getUserId, userId)
                            .eq(RecordQualification::getOperation, RecordQualificationOperationEnum.QUALIFY);
                    Integer singleCount = recordQualificationMapper.selectCount(singleLimitQuery);
                    isTrue(singleCount < singleLimit, OUT_OF_SINGLE_QUALIFICATION_LIMIT);

                });

        // 校验活动每日资格获取上限
        Optional.ofNullable(qualification.getTotalDailyLimit())
                .ifPresent(totalDailyLimit -> {
                    LambdaQueryWrapper<RecordQualification> totalDailyLimitQuery = new LambdaQueryWrapper<>();
                    totalDailyLimitQuery.eq(RecordQualification::getActId, qualification.getActId())
                            .eq(RecordQualification::getQualificationId, qualification.getId())
                            .eq(RecordQualification::getOperation, RecordQualificationOperationEnum.QUALIFY)
                            .ge(RecordQualification::getCreateTime, LocalDateTime.of(now.toLocalDate(), LocalTime.MIN))
                            .le(RecordQualification::getCreateTime, LocalDateTime.of(now.toLocalDate(), LocalTime.MAX));
                    Integer totalDailyCount = recordQualificationMapper.selectCount(totalDailyLimitQuery);
                    isTrue(totalDailyCount < totalDailyLimit, OUT_OF_DAILY_QUALIFICATION_LIMIT);

                });

        // 校验活动累计资格获取上限
        Optional.ofNullable(qualification.getTotalLimit())
                .ifPresent(totalLimit -> {
                    LambdaQueryWrapper<RecordQualification> totalLimitQuery = new LambdaQueryWrapper<>();
                    totalLimitQuery.eq(RecordQualification::getActId, qualification.getActId())
                            .eq(RecordQualification::getQualificationId, qualification.getId())
                            .eq(RecordQualification::getOperation, RecordQualificationOperationEnum.QUALIFY);
                    Integer totalCount = recordQualificationMapper.selectCount(totalLimitQuery);
                    isTrue(totalCount < totalLimit, OUT_OF_QUALIFICATION_LIMIT);
                });
    }

    /**
     * 校验资格依赖限制
     *
     * @param qualification 资格
     * @param userId        用户Id
     */
    private void checkDependents(Qualification qualification, Long userId) {
        List<Integer> dependentQualificationIds = Optional.ofNullable(qualification.getDependents())
                .map(item -> Arrays.stream(item.split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        Integer dependentQualificationCount = Optional.of(dependentQualificationIds)
                .map(item -> {
                    LambdaQueryWrapper<Participant> participantQuery = new LambdaQueryWrapper<>();
                    participantQuery.eq(Participant::getActId, qualification.getActId())
                            .in(Participant::getQualificationId, dependentQualificationIds)
                            .eq(Participant::getUserId, userId);
                    return dependentQualificationIds.size() > 0 ? participantMapper.selectCount(participantQuery) : 0;
                }).orElse(0);
        isTrue(dependentQualificationIds.size() == dependentQualificationCount, DEPENDENT_QUALIFICATION_NOT_EXIST);
    }

}
