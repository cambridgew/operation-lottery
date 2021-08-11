package org.indiv.cambridgew.lottery.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.indiv.cambridgew.lottery.common.Result;
import org.indiv.cambridgew.lottery.dto.QualifyDTO;
import org.indiv.cambridgew.lottery.service.QualifyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author cambridge.w
 * @since 2021/8/11
 */
@Slf4j
@Api(tags = "资格相关接口")
@RestController
@RequestMapping(value = "/qualification")
public class QualificationController {

    @Resource
    private QualifyService qualifyService;

    @ApiOperation("下发抽奖资格")
    @PostMapping(value = "/qualify", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result qualify(@RequestBody QualifyDTO dto) {
        qualifyService.qualify(dto);
        return Result.success();
    }

}