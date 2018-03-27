package com.gonwan.restful.springboot;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.gonwan.restful.springboot.util.SystemUtils;

@ConfigurationProperties(prefix = "application")
@Component
public class Config {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private int port;

    private String instName;

    private int executorCorePoolSize;

    private int executorMaxPoolSize;

    private int executorQueueCapacity;

    private int contentStreamThreshold;

    private String zookeeperMonitorPath;

    @PostConstruct
    private void init() {
        instName = String.format("%s_%s:%d", appName, SystemUtils.getLocalHost(), port);
    }

    public String getAppName() {
        return appName;
    }

    public int getPort() {
        return port;
    }

    public String getInstName() {
        return instName;
    }

    public int getExecutorCorePoolSize() {
        return executorCorePoolSize;
    }

    public void setExecutorCorePoolSize(int executorCorePoolSize) {
        this.executorCorePoolSize = executorCorePoolSize;
    }

    public int getExecutorMaxPoolSize() {
        return executorMaxPoolSize;
    }

    public void setExecutorMaxPoolSize(int executorMaxPoolSize) {
        this.executorMaxPoolSize = executorMaxPoolSize;
    }

    public int getExecutorQueueCapacity() {
        return executorQueueCapacity;
    }

    public void setExecutorQueueCapacity(int executorQueueCapacity) {
        this.executorQueueCapacity = executorQueueCapacity;
    }

    public int getContentStreamThreshold() {
        return contentStreamThreshold;
    }

    public void setContentStreamThreshold(int contentStreamThreshold) {
        this.contentStreamThreshold = contentStreamThreshold;
    }

    public String getZookeeperMonitorPath() {
        return zookeeperMonitorPath;
    }

    public void setZookeeperMonitorPath(String zookeeperMonitorPath) {
        this.zookeeperMonitorPath = zookeeperMonitorPath;
    }

    public String getRedisPublishPrefix() {
        return getAppName() + ".publish.";
    }

}
