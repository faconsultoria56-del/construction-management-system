package com.management.authorization.config;

import com.management.authorization.interceptor.FinancialAccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private FinancialAccessInterceptor financialAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(financialAccessInterceptor)
                .addPathPatterns("/api/v1/projects/{projectId}/financial-summary");
    }
}
