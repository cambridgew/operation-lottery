package org.indiv.cambridgew.operation.lottery.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 核心服务自动配置
 *
 * @author cambridge.w
 * @since 2021/8/11
 */
@Configuration
public class CoreAutoConfiguration {

    @Configuration
    @ConditionalOnClass(BaseMapper.class)
    @PropertySource("classpath:/core-dao.properties")
    @MapperScan("org.indiv.cambridgew.lottery.dao")
    public static class CoreDaoConfiguration {

    }

}
