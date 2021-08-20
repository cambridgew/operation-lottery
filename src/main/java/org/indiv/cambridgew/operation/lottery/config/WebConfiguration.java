package org.indiv.cambridgew.operation.lottery.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.Optional;

/**
 * @author cambridge.w
 * @since 2021/8/11
 */
@Configuration
public class WebConfiguration implements InitializingBean {

    @Resource
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * 跨域过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Override
    public void afterPropertiesSet() {
        Optional.ofNullable(requestMappingHandlerAdapter.getArgumentResolvers()).ifPresent(resolvers -> {
            LinkedList<HandlerMethodArgumentResolver> argumentResolvers = new LinkedList<>(resolvers);
            requestMappingHandlerAdapter.setArgumentResolvers(argumentResolvers);
        });
    }

}
