package com.yoyo.datasync.producer;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.yoyo.datasync.conf.CanalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.List;
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
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private CanalConnector connector;
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1, new
            CustomizableThreadFactory());

    @Override
    public void afterPropertiesSet() throws Exception {

        //初始化canal连接
        initConnector();

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                logger.info("==========================");
                startWork();

//                kafkaTemplate.send("testbinlog", "hello kafka");

            }
        }, 1, 2, TimeUnit.SECONDS);

    }

    private void startWork() {
        int emptyCount = 0;
        int totalEmptyCount = 10;
        while (emptyCount < totalEmptyCount) {
            Message message = connector.getWithoutAck(canalConfig.getBatchSize()); // 获取指定数量的数据
            long batchId = message.getId();
            int size = message.getEntries().size();
            if (batchId == -1 || size == 0) {
                emptyCount++;
                System.out.println("empty count : " + emptyCount);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                emptyCount = 0;
                // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                printEntry(message.getEntries());
            }

            connector.ack(batchId); // 提交确认
            // connector.rollback(batchId); // 处理失败, 回滚数据
        }

    }

    private void initConnector() {

        connector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalConfig.getIp(),
                canalConfig.getPort()), canalConfig.getDestination(), canalConfig.getUserName(), canalConfig
                .getPassword());

        connector.connect();
        connector.subscribe(canalConfig.getTopics());//指定topic后缀
        connector.rollback();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        }));
    }

    private void shutdown() {

        connector.disconnect();
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    private static void printEntry(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry
                    .EntryType
                    .TRANSACTIONEND) {
                continue;
            }

            CanalEntry.RowChange rowChage = null;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            CanalEntry.EventType eventType = rowChage.getEventType();
            System.out.println(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == CanalEntry.EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == CanalEntry.EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    System.out.println("-------> before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("-------> after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
