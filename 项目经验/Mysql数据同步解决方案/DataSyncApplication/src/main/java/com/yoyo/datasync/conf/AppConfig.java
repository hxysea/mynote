package com.yoyo.datasync.conf;

import com.yoyo.datasync.producer.IMessageHandler;
import com.yoyo.datasync.task.CanalTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by huxianyang on 2017/12/8.
 */
@Configuration
public class AppConfig {
    @Bean
    public CanalTask canalTask(CanalConfig canalConfig, IMessageHandler messageHandler) {
        return new CanalTask(canalConfig, messageHandler);
    }
}
