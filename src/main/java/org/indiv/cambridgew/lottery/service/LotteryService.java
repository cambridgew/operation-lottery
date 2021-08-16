package org.indiv.cambridgew.lottery.service;

import org.indiv.cambridgew.lottery.dto.PrizeDetailDTO;
import org.indiv.cambridgew.lottery.dto.req.DrawDTO;
import org.indiv.cambridgew.lottery.entity.Participant;
import org.indiv.cambridgew.lottery.manager.qualify.QualificationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

import static org.indiv.cambridgew.lottery.constant.ErrorMsgConstants.*;
import static org.springframework.util.Assert.isTrue;

/**
 * 抽奖服务
 *
 * @author cambridge.w
 * @since 2021/8/9
 */
@Service
public class LotteryService {

    @Resource
    private QualificationManager qualificationManager;

    /**
     * 抽奖
     *
     * @param dto 抽奖请求dto
     * @return 中奖详情
     */
    @Transactional(rollbackFor = Exception.class)
    public PrizeDetailDTO draw(DrawDTO dto) {
        // 资格消耗
        List<Participant> p = qualificationManager.queryParticipant(dto.getActId(), dto.getUserId(), null);
        isTrue(!CollectionUtils.isEmpty(p), QUALIFICATION_NOT_POSSESS);
        Participant qualificationToConsume = p.stream()
                .filter(item -> item.getCurrentChanceNumber() > 0)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(QUALIFICATION_EXHAUSTED));
        isTrue(qualificationManager.consumeQualification(qualificationToConsume, 1), QUALIFICATION_CONSUME_FAILURE);
        // TODO 资格消耗成功开始抽奖

        // TODO 抽奖信息落库
        return new PrizeDetailDTO();
    }



}
