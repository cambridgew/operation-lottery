package org.indiv.cambridgew.lottery.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.indiv.cambridgew.lottery.common.RecordQualificationOperationEnum;
import org.indiv.cambridgew.lottery.dao.ParticipantMapper;
import org.indiv.cambridgew.lottery.dao.RecordQualificationMapper;
import org.indiv.cambridgew.lottery.dto.QualificationDetailDTO;
import org.indiv.cambridgew.lottery.dto.req.QualificationQueryDTO;
import org.indiv.cambridgew.lottery.dto.req.QualifyDTO;
import org.indiv.cambridgew.lottery.entity.Participant;
import org.indiv.cambridgew.lottery.entity.Qualification;
import org.indiv.cambridgew.lottery.entity.RecordQualification;
import org.indiv.cambridgew.lottery.manager.qualify.QualificationManager;
import org.indiv.cambridgew.lottery.validator.QualificationValidator;
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
    private QualificationValidator validator;
    @Resource
    private ParticipantMapper participantMapper;
    @Resource
    private QualificationManager qualificationManager;
    @Resource
    private RecordQualificationMapper recordQualificationMapper;

    /**
     * 资格下发
     *
     * @param dto 资格下发DTO
     */
    public void qualify(QualifyDTO dto) {
        // 校验是否可以下发资格
        Qualification qualification = validator.validate(dto.getActId(), dto.getEventKey(), dto.getUserId());
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
        Optional.ofNullable(qualificationManager.queryParticipant(dto.getActId(), dto.getUserId(), dto.getQualificationId()))
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
        recordQualificationMapper.insert(RecordQualification.builder()
                .qualificationId(qualification.getId())
                .userId(userId)
                .chanceNumber(qualification.getChanceNumber())
                .source(source)
                .operation(RecordQualificationOperationEnum.QUALIFY)
                .actId(qualification.getActId())
                .eventKey(qualification.getEventKey()).build());
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

}
