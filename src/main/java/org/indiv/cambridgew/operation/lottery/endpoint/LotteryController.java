package org.indiv.cambridgew.operation.lottery.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.indiv.cambridgew.operation.lottery.dto.req.DrawDTO;
import org.indiv.cambridgew.operation.lottery.service.LotteryService;
import org.indiv.cambridgew.poseidon.core.entity.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author cambridge.w
 * @since 2021/8/20
 */
@Slf4j
@Api(tags = "抽奖相关接口")
@RestController
@RequestMapping(value = "/lottery")
public class LotteryController {

    @Resource
    private LotteryService lotteryService;

    @ApiOperation("抽奖")
    @PostMapping(value = "/draw", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result qualify(@RequestBody DrawDTO dto) {
        lotteryService.draw(dto);
        return Result.success();
    }

}
