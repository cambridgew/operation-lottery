package org.indiv.cambridgew.lottery.service;

import org.indiv.cambridgew.lottery.dto.PrizeDetailDTO;
import org.indiv.cambridgew.lottery.dto.req.DrawDTO;
import org.indiv.cambridgew.lottery.entity.Activity;
import org.indiv.cambridgew.lottery.entity.Participant;
import org.indiv.cambridgew.lottery.manager.ActivityResourceManager;
import org.indiv.cambridgew.lottery.manager.draw.DrawManager;
import org.indiv.cambridgew.lottery.manager.qualify.QualificationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.indiv.cambridgew.lottery.constant.ErrorMsgConstants.QUALIFICATION_CONSUME_FAILURE;
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
    @Resource
    private DrawManager drawManager;
    @Resource
    private ActivityResourceManager activityResourceManager;

    /**
     * 抽奖
     *
     * @param dto 抽奖请求dto
     * @return 中奖详情
     */
    @Transactional(rollbackFor = Exception.class)
    public PrizeDetailDTO draw(DrawDTO dto) {
        // 尝试资格消耗
        Participant qualificationToConsume = drawManager.determineParticipant(dto.getActId(), dto.getUserId());
        isTrue(qualificationManager.consumeParticipant(qualificationToConsume, 1), QUALIFICATION_CONSUME_FAILURE);
        // 资格消耗成功开始抽奖
        Activity activity = activityResourceManager.findActivity(dto.getActId());
        Integer prizeId = drawManager.draw(dto.getActId(), activity.getDrawOperationType(), dto.getUserId(), qualificationToConsume);
        // TODO 抽奖信息落库
        return new PrizeDetailDTO();
    }


}
