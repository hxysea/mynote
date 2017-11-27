package com.yoyo.datasync.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by huxianyang on 2017/11/27.
 */
@Component
public class Kafka2MessageHandler {
    @KafkaListener(topics = "testbinlog")
    public void processMessage(String content){

        System.out.println("==================" + content);

    }
}
