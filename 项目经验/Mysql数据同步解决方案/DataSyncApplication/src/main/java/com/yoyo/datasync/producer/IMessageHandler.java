package com.yoyo.datasync.producer;

import com.alibaba.otter.canal.protocol.CanalEntry.Entry;

import java.util.List;

/**
 * Created by huxianyang on 2017/12/8.
 */
public interface IMessageHandler {
    boolean handle(List<Entry> entryList);
}
