package com.ssn.delay.api;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDelayTaskHolder implements DelayTask{

    protected final TaskProcessor taskProcessor;

    protected List<Thread> consumerThreadList;

    //是否已停止
    protected volatile boolean stop = false;

    protected final DelayTaskProperties properties;


    protected AbstractDelayTaskHolder(TaskProcessor taskProcessor, DelayTaskProperties properties) {
        if (properties.getConcurrentConsumers() <=0) {
            properties.setConcurrentConsumers(5);
        }
        if (taskProcessor == null) {
            throw new IllegalArgumentException("TaskProcessor cannot be null");
        }
        this.taskProcessor = taskProcessor;
        this.properties = properties;
        this.consumerThreadList = new ArrayList<>(properties.getConcurrentConsumers());
    }


    @Override
    public void stop() {
        this.stop= true;
        for (Thread thread : consumerThreadList) {
            thread.interrupt();
        }
    }


    public String getThreadName(int i) {
        return String.format("DelayTaskProcessor-ConsumerThread-%s-%d", this.properties.getName(), i);
    }

}
