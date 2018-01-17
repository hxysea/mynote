package com.yoyo.datasync.producer;

import com.alibaba.otter.canal.protocol.CanalEntry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by huxianyang on 2017/11/27.
 */
@Component
public class Canal2KafkaHandler implements IMessageHandler {
    @Resource
    private KafkaTemplate kafkaTemplate;

    @Override
    public boolean handle(List<CanalEntry.Entry> entryList) {
        return false;
    }
}
