package org.indiv.cambridgew.operation.lottery.service;

import org.indiv.cambridgew.operation.lottery.dto.QualificationDetailDTO;
import org.indiv.cambridgew.operation.lottery.dto.req.QualificationQueryDTO;
import org.indiv.cambridgew.operation.lottery.dto.req.QualifyDTO;
import org.indiv.cambridgew.operation.lottery.entity.Qualification;
import org.indiv.cambridgew.operation.lottery.manager.qualify.QualificationManager;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 资格服务
 *
 * @author cambridge.w
 * @since 2021/8/4
 */
@Service
public class QualifyService {

    @Resource
    private QualificationManager qualificationManager;

    /**
     * 资格下发
     *
     * @param dto 资格下发DTO
     */
    public void qualify(QualifyDTO dto) {
        // 校验是否可以下发资格
        Qualification qualification = qualificationManager.validate(dto.getActId(), dto.getEventKey(), dto.getUserId());
        // 资格落库
        save(qualification, dto.getUserId(), dto.getSource());
    }

    /**
     * 资格详情查询
     *
     * @param dto 查询DTO
     * @return 资格详情
     */
    public QualificationDetailDTO getQualification(QualificationQueryDTO dto) {
        QualificationDetailDTO result = new QualificationDetailDTO();
        BeanUtils.copyProperties(dto, result);
        List<QualificationDetailDTO.QualificationDetail> qualificationDetailList = new ArrayList<>();
        Optional.ofNullable(qualificationManager.queryParticipants(dto.getActId(), dto.getUserId(), dto.getQualificationId()))
                .ifPresent(list -> list.forEach(item -> {
                    QualificationDetailDTO.QualificationDetail detail = new QualificationDetailDTO.QualificationDetail();
                    BeanUtils.copyProperties(item, detail);
                    qualificationDetailList.add(detail);
                }));
        result.setQualificationDetails(qualificationDetailList);
        return result;
    }

    /**
     * 资格落库
     *
     * @param qualification 资格
     * @param userId        用户Id
     * @param source        来源信息
     */
    @Transactional(rollbackFor = Exception.class)
    protected void save(Qualification qualification, Long userId, String source) {
        // 记录资格流水
        qualificationManager.recordQualify(qualification, userId, source);
        // 更新用户资格数据
        qualificationManager.updateParticipant(qualification, userId);
    }

}
