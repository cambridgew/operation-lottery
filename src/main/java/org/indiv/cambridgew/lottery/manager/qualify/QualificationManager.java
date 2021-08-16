package org.indiv.cambridgew.lottery.manager.qualify;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.indiv.cambridgew.lottery.dao.ParticipantMapper;
import org.indiv.cambridgew.lottery.entity.Participant;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.List;

/**
 * 资格管理器
 *
 * @author cambridge.w
 * @since 2021/8/14
 */
@Service
public class QualificationManager {

    @Resource
    private ParticipantMapper participantMapper;

    /**
     * 查询用户当前资格情况
     *
     * @param actId           活动Id
     * @param userId          用户Id
     * @param qualificationId 资格Id
     * @return 用户资格列表(按资格消耗优先级排序)
     */
    public List<Participant> queryParticipant(Integer actId, @Nullable Long userId, @Nullable Integer qualificationId) {
        LambdaQueryWrapper<Participant> query = Wrappers.<Participant>lambdaQuery()
                .eq(null != actId, Participant::getActId, actId)
                .eq(null != userId, Participant::getUserId, userId)
                .eq(null != qualificationId, Participant::getQualificationId, qualificationId)
                .orderByAsc(Participant::getPriority);
        return participantMapper.selectList(query);
    }

    /**
     * 消耗抽奖资格次数
     *
     * @param participant  待消耗资格详情
     * @param chanceNumber 资格消耗次数
     * @return 是否消耗成功
     */
    public Boolean consumeQualification(Participant participant, Integer chanceNumber) {
        LambdaUpdateWrapper<Participant> update = Wrappers.<Participant>lambdaUpdate()
                .set(Participant::getCurrentChanceNumber, participant.getCurrentChanceNumber() - chanceNumber)
                .eq(Participant::getId, participant.getId());
        return participantMapper.update(null, update) > 0;
    }

}
