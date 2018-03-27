package com.gonwan.restful.springboot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZookeeperClient {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

    public ZookeeperClient(@Autowired(required=false) CuratorFramework curatorFramework, @Autowired Config config) {
        if (curatorFramework != null) {
            String path = config.getZookeeperMonitorPath() + "/" + config.getInstName();
            String data = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
            try {
                logger.info("Creating zookeeper node: {}", path);
                curatorFramework.create().
                    creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
            } catch (Exception e) {
                logger.warn("Error creating zookeeper node", e);
            }
        }
    }

}
