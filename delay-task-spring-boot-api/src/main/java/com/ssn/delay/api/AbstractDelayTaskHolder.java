package com.ssn.delay.api;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDelayTaskHolder implements DelayTask{

    protected final TaskProcessor taskProcessor;

    protected List<Thread> consumerThreadList;

    //是否已停止
    protected volatile boolean stop = false;

    protected final String threadName;


    protected AbstractDelayTaskHolder(TaskProcessor taskProcessor, String threadName) {
        this.taskProcessor = taskProcessor;
        consumerThreadList = new ArrayList<>();
        this.threadName = threadName;
    }


    @Override
    public void stop() {
        this.stop= true;
        for (Thread thread : consumerThreadList) {
            thread.interrupt();
        }
    }


}
