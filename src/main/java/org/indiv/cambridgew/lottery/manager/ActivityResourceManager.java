package org.indiv.cambridgew.lottery.manager;

import org.indiv.cambridgew.lottery.dao.ActivityMapper;
import org.indiv.cambridgew.lottery.entity.Activity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

import static org.indiv.cambridgew.lottery.constant.ErrorMsgConstants.ACTIVITY_NOT_EXIST;

/**
 * 活动相关资源管理器
 *
 * @author cambridge.w
 * @since 2021/8/17
 */
@Service
public class ActivityResourceManager {

    @Resource
    private ActivityMapper activityMapper;

    /**
     * 查询活动
     * @param actId 活动Id
     * @return 活动实体
     */
    public Activity findActivity(Integer actId) {
        return Optional.ofNullable(activityMapper.selectById(actId))
                .orElseThrow(() -> new IllegalStateException(ACTIVITY_NOT_EXIST));
    }

}
