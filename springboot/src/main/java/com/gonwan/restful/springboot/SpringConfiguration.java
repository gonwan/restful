package com.gonwan.restful.springboot;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
@EnableJpaRepositories
public class SpringConfiguration {

    /*
     * Spring uses forwarding for exception/status code handling.
     * see: org.apache.catalina.core.StandardHostValve#invoke().
     * ErrorAttributes is used for view rendering. Simply disable it by returning empty attributes.
     */
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
                return Collections.emptyMap();
            }
        };
    }

    @Bean(name = "error")
    public View defaultErrorView() {
        return new View() {
            @Override
            public String getContentType() {
                return "text/html";
            }
            @Override
            public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                /* send nothing */
            }
        };
    }

    /*
     * See: https://spring.io/blog/2012/05/10/spring-mvc-3-2-preview-making-a-controller-method-asynchronous/
     */
    @Bean
    public AsyncTaskExecutor asyncTaskExecutor(Config config) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(config.getExecutorCorePoolSize());
        executor.setMaxPoolSize(config.getExecutorMaxPoolSize());
        executor.setQueueCapacity(config.getExecutorQueueCapacity());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(AsyncTaskExecutor asyncTaskExecutor) {
        return new WebMvcConfigurer() {
            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
                configurer.setTaskExecutor(asyncTaskExecutor);
            }
        };
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

}
