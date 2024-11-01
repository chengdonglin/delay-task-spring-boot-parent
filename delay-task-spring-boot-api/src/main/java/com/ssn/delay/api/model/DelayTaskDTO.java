package com.ssn.delay.api.model;


import com.ssn.delay.api.TaskProcessor;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayTaskDTO<T> implements Delayed {

    private String messageType;

    private Long taskExecuteTimeMs;

    private T data;

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(taskExecuteTimeMs - System.currentTimeMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o instanceof DelayTaskDTO) {
            DelayTaskDTO o1 = (DelayTaskDTO) o;
            return Long.compare(this.getTaskExecuteTimeMs(), o1.getTaskExecuteTimeMs());
        }
        return 0;
    }


    public Long getTaskExecuteTimeMs() {
        return taskExecuteTimeMs;
    }

    public void setTaskExecuteTimeMs(Long taskExecuteTimeMs) {
        this.taskExecuteTimeMs = taskExecuteTimeMs;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
