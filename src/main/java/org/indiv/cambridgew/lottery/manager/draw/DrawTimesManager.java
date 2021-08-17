package org.indiv.cambridgew.lottery.manager.draw;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.indiv.cambridgew.lottery.dao.JackpotIndexMapper;
import org.indiv.cambridgew.lottery.dao.RecordLotteryMapper;
import org.indiv.cambridgew.lottery.entity.JackpotIndex;
import org.indiv.cambridgew.lottery.entity.Participant;
import org.indiv.cambridgew.lottery.entity.RecordLottery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static org.indiv.cambridgew.lottery.constant.ErrorMsgConstants.QUALIFICATION_NOT_EXIST;
import static org.springframework.util.Assert.isTrue;

/**
 * @author cambridge.w
 * @since 2021/8/16
 */
@Component("drawTimes")
public class DrawTimesManager extends AbstractDrawManager {

    @Resource
    private RecordLotteryMapper recordLotteryMapper;
    @Resource
    private JackpotIndexMapper jackpotIndexMapper;

    @Override
    public Integer determineJackpot(Integer actId, Long userId, Participant participant) {
        // 当前活动该用户已抽奖次数
        int recordNumber = recordLotteryMapper.selectCount(Wrappers.<RecordLottery>lambdaQuery()
                .eq(RecordLottery::getActId, actId)
                .eq(RecordLottery::getUserId, userId));
        List<JackpotIndex> jackpotIndexList = jackpotIndexMapper.selectList(Wrappers.<JackpotIndex>lambdaQuery()
                .eq(JackpotIndex::getActId, actId));
        isTrue(!CollectionUtils.isEmpty(jackpotIndexList), QUALIFICATION_NOT_EXIST);
        JackpotIndex jackpotIndex = jackpotIndexList.stream()
                .filter(item -> item.getStageFrom() <= recordNumber && item.getStageTo() >= recordNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(QUALIFICATION_NOT_EXIST));
        return jackpotIndex.getJackpotId();
    }

}
