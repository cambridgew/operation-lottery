package org.indiv.cambridgew.lottery.manager.draw;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.indiv.cambridgew.lottery.dao.ParticipantMapper;
import org.indiv.cambridgew.lottery.dao.RecordLotteryMapper;
import org.indiv.cambridgew.lottery.entity.Participant;
import org.indiv.cambridgew.lottery.entity.Prize;
import org.indiv.cambridgew.lottery.entity.RecordLottery;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.indiv.cambridgew.lottery.constant.ErrorMsgConstants.*;
import static org.springframework.util.Assert.isTrue;

/**
 * 抽奖管理器
 * <p>提供供上层使用的方法</p>
 * <p>设计具有策略的抽奖接口</p>
 *
 * @author cambridge.w
 * @since 2021/8/14
 */
@Service
public class DrawManager {

    @Resource
    private ParticipantMapper participantMapper;
    @Resource
    private RecordLotteryMapper recordLotteryMapper;
    @Resource
    private Map<String, DrawExecutor> drawStrategy = new HashMap<>(16);

    /**
     * 确定用户抽奖所消耗的资格
     *
     * @param actId  活动Id
     * @param userId 用户Id
     * @return 用户抽奖所消耗的资格实体
     */
    public Participant determineParticipant(Integer actId, Long userId) {
        List<Participant> p = participantMapper.selectList(Wrappers.<Participant>lambdaQuery()
                .eq(null != actId, Participant::getActId, actId)
                .eq(null != userId, Participant::getUserId, userId)
                .orderByAsc(Participant::getPriority));
        isTrue(!CollectionUtils.isEmpty(p), QUALIFICATION_NOT_POSSESS);
        return p.stream()
                .filter(item -> item.getCurrentChanceNumber() > 0)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(QUALIFICATION_EXHAUSTED));
    }

    /**
     * 执行抽奖动作
     *
     * @param actId             活动Id
     * @param drawOperationType 抽奖动作类型(按照次数/按照资格事件类型)
     * @param userId            用户Id
     * @param participant       抽奖所消耗的资格实体
     * @return 中奖奖品实体
     */
    public Prize draw(Integer actId, String drawOperationType, Long userId, @NonNull Participant participant) {
        isTrue(drawStrategy.containsKey(drawOperationType), DRAW_OPERATION_NOT_EXIST);
        return drawStrategy.get(drawOperationType).draw(actId, userId, participant);
    }

    /**
     * 记录抽奖结果
     *
     * @param qualificationId 本次抽奖消耗的资格Id
     * @param userId          用户Id
     * @param prize           中奖奖品实体
     */
    public void recordLottery(Integer qualificationId, Long userId, Prize prize) {
        recordLotteryMapper.insert(RecordLottery.builder()
                .qualificationId(qualificationId)
                .userId(userId)
                .jackpotId(prize.getJackpotId())
                .prizeId(prize.getId())
                .actId(prize.getActId()).build());
    }


    // =======================待实现的接口====================

    @FunctionalInterface
    public interface DrawExecutor {

        /**
         * 抽奖
         *
         * @param actId       活动Id
         * @param userId      用户Id
         * @param participant 抽奖所消耗的资格实体
         * @return 抽中的奖品实体
         */
        Prize draw(Integer actId, Long userId, @NonNull Participant participant);
    }


}
