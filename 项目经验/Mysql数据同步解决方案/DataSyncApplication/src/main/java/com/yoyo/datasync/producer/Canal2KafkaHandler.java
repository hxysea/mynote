package com.yoyo.datasync.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.CanalConnector;
import com.yoyo.datasync.conf.CanalConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by huxianyang on 2017/11/27.
 */
@Component
public class Canal2KafkaHandler implements InitializingBean {
    @Resource
    private KafkaTemplate kafkaTemplate;
    @Resource
    private CanalConfig canalConfig;
    CanalConnector connector;
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1, new
            CustomizableThreadFactory());

    @Override
    public void afterPropertiesSet() throws Exception {

        //初始化canal连接
        initConnector();

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                kafkaTemplate.send("testbinlog", "hello kafka");

            }
        }, 1, 2, TimeUnit.SECONDS);

    }

    private void initConnector() {

        System.out.println(JSON.toJSONString(canalConfig));
    }
}
