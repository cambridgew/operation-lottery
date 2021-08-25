package org.indiv.cambridgew.operation.lottery.service;

import org.indiv.cambridgew.operation.lottery.dto.LotteryDetailDTO;
import org.indiv.cambridgew.operation.lottery.dto.req.DrawDTO;
import org.indiv.cambridgew.operation.lottery.entity.Activity;
import org.indiv.cambridgew.operation.lottery.entity.Participant;
import org.indiv.cambridgew.operation.lottery.entity.Prize;
import org.indiv.cambridgew.operation.lottery.manager.ResourceManager;
import org.indiv.cambridgew.operation.lottery.manager.draw.DrawManager;
import org.indiv.cambridgew.operation.lottery.manager.qualify.QualificationManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static org.indiv.cambridgew.operation.lottery.constant.ErrorMsgConstants.QUALIFICATION_CONSUME_FAILURE;
import static org.springframework.util.Assert.isTrue;

/**
 * 抽奖服务
 *
 * @author cambridge.w
 * @since 2021/8/9
 */
@Service
public class LotteryService {

    private static final ReentrantLock lock = new ReentrantLock();

    @Resource
    private QualificationManager qualificationManager;
    @Resource
    private DrawManager drawManager;
    @Resource
    private ResourceManager resourceManager;
    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 抽奖
     *
     * @param dto 抽奖请求dto
     * @return 中奖详情
     */
    public LotteryDetailDTO draw(DrawDTO dto) {
        lock.lock();
        // 尝试资格消耗
        Participant qualificationToConsume = drawManager.determineParticipant(dto.getActId(), dto.getUserId());
        // 存在抽奖资格, 开始抽奖
        Activity activity = resourceManager.findActivity(dto.getActId());
        Prize prize = drawManager.draw(dto.getActId(), activity.getDrawOperationType(), dto.getUserId(), qualificationToConsume);
        // 抽奖完毕消耗抽奖资格
        isTrue(qualificationManager.consumeParticipant(qualificationToConsume, 1), QUALIFICATION_CONSUME_FAILURE);
        lock.unlock();

        // 中奖结果异步落库
        taskExecutor.getThreadPoolExecutor()
                .execute(() -> drawManager.recordLottery(qualificationToConsume.getQualificationId(), dto.getUserId(), prize));
        return new LotteryDetailDTO(dto.getUserId(), prize);
    }

    /**
     * 查询中奖详情信息
     *
     * @param dto 请求dto
     * @return 中奖详情
     */
    public LotteryDetailDTO getLotteryDetail(DrawDTO dto) {
        List<Prize> prizeList = drawManager.getRecordLottery(dto.getActId(), dto.getUserId());
        return new LotteryDetailDTO(dto.getUserId(), prizeList);
    }

}
