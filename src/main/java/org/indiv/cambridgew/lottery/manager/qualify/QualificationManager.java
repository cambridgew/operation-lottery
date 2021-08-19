package org.indiv.cambridgew.lottery.manager.qualify;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.indiv.cambridgew.lottery.common.RecordQualificationOperationEnum;
import org.indiv.cambridgew.lottery.dao.ParticipantMapper;
import org.indiv.cambridgew.lottery.dao.RecordQualificationMapper;
import org.indiv.cambridgew.lottery.entity.Participant;
import org.indiv.cambridgew.lottery.entity.Qualification;
import org.indiv.cambridgew.lottery.entity.RecordQualification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.indiv.cambridgew.lottery.constant.ErrorMsgConstants.EVENT_KEY_NOT_EXIST;
import static org.springframework.util.Assert.isTrue;

/**
 * 资格管理器
 * <p>提供供上层使用的方法</p>
 * <p>设计具有策略的资格接口</p>
 *
 * @author cambridge.w
 * @since 2021/8/4
 */
@Service
public class QualificationManager {

    @Resource
    private ParticipantMapper participantMapper;
    @Resource
    private RecordQualificationMapper recordQualificationMapper;
    @Resource
    private Map<String, Validator> qualificationStrategy = new HashMap<>(16);

    /**
     * 校验资格是否可以下发
     *
     * @param actId    活动Id
     * @param eventKey 资格事件Key
     * @param userId   用户Id
     * @return 可以下发的资格实体
     */
    public Qualification validate(Integer actId, String eventKey, Long userId) {
        isTrue(qualificationStrategy.containsKey(eventKey), EVENT_KEY_NOT_EXIST);
        return qualificationStrategy.get(eventKey).validate(actId, eventKey, userId);
    }

    /**
     * 消耗抽奖资格次数
     *
     * @param participant  待消耗资格详情
     * @param chanceNumber 资格消耗次数
     * @return 是否消耗成功
     */
    public Boolean consumeParticipant(Participant participant, Integer chanceNumber) {
        LambdaUpdateWrapper<Participant> update = Wrappers.<Participant>lambdaUpdate()
                .set(Participant::getCurrentChanceNumber, participant.getCurrentChanceNumber() - chanceNumber)
                .eq(Participant::getId, participant.getId());
        return participantMapper.update(null, update) > 0;
    }

    /**
     * 查询用户当前资格情况
     *
     * @param actId           活动Id
     * @param userId          用户Id
     * @param qualificationId 资格Id
     * @return 用户资格列表(按资格消耗优先级排序)
     */
    public List<Participant> queryParticipants(Integer actId, @Nullable Long userId, @Nullable Integer qualificationId) {
        LambdaQueryWrapper<Participant> query = Wrappers.<Participant>lambdaQuery()
                .eq(null != actId, Participant::getActId, actId)
                .eq(null != userId, Participant::getUserId, userId)
                .eq(null != qualificationId, Participant::getQualificationId, qualificationId)
                .orderByAsc(Participant::getPriority);
        return participantMapper.selectList(query);
    }

    // =======================流水记录相关方法====================

    /**
     * 记录资格下发流水
     *
     * @param qualification 资格详情实体
     * @param userId        用户Id
     * @param source        资格来源
     */
    public void recordQualify(Qualification qualification, Long userId, String source) {
        recordQualificationMapper.insert(RecordQualification.builder()
                .qualificationId(qualification.getId())
                .userId(userId)
                .chanceNumber(qualification.getChanceNumber())
                .source(source)
                .operation(RecordQualificationOperationEnum.QUALIFY)
                .actId(qualification.getActId())
                .eventKey(qualification.getEventKey()).build());
    }

    /**
     * 更新用户资格数据(participant资格)
     *
     * @param qualification 资格详情实体
     * @param userId        用户Id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateParticipant(Qualification qualification, Long userId) {
        LambdaQueryWrapper<Participant> participantQuery = new LambdaQueryWrapper<>();
        participantQuery.eq(Participant::getActId, qualification.getActId())
                .eq(Participant::getQualificationId, qualification.getId())
                .eq(Participant::getUserId, userId);
        Participant p = participantMapper.selectOne(participantQuery);
        if (Optional.ofNullable(p).isPresent()) {
            // 已存在相同资格, 更新数据
            LambdaUpdateWrapper<Participant> participantUpdate = new LambdaUpdateWrapper<>();
            participantUpdate.set(Participant::getTotalChanceNumber, p.getTotalChanceNumber() + qualification.getChanceNumber())
                    .set(Participant::getCurrentChanceNumber, p.getCurrentChanceNumber() + qualification.getChanceNumber());
            participantMapper.update(null, participantUpdate);
        } else {
            // 不存在资格, 新增数据
            participantMapper.insert(Participant.builder()
                    .qualificationId(qualification.getId())
                    .userId(userId)
                    .currentChanceNumber(qualification.getChanceNumber())
                    .totalChanceNumber(qualification.getChanceNumber())
                    .actId(qualification.getActId())
                    .priority(qualification.getPriority()).build());
        }
    }


    // =======================待实现的接口====================

    @FunctionalInterface
    public interface Validator {
        /**
         * 校验过滤用户抽奖资格-根据不同的资格类型执行不同的资格校验策略
         *
         * @param actId    活动Id
         * @param eventKey 资格事件
         * @param userId   用户Id
         * @return 返回用户可用资格, 也即本次抽奖所消耗的资格, 若用户无可用资格, 则返回null
         */
        Qualification validate(Integer actId, String eventKey, Long userId);
    }

}
