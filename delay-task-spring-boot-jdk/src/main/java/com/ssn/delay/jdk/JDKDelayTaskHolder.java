package com.ssn.delay.jdk;

import org.slf4j.Logger;
import com.ssn.delay.api.DelayTask;
import com.ssn.delay.api.TaskProcessor;
import com.ssn.delay.api.model.DelayTaskDTO;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class JDKDelayTaskHolder implements DelayTask {

    private final Logger logger = LoggerFactory.getLogger(JDKDelayTaskHolder.class);

    private String threadName;

    private Integer capacity = 10000;

    //延迟队列
    private DelayQueue<DelayTaskDTO> delayQueue = new DelayQueue<>();

    //是否已停止
    private volatile boolean stop = false;

    //当前待处理的任务数量
    private AtomicInteger size = new AtomicInteger(0);

    private List<Thread> consumerThreadList;

    private TaskProcessor taskProcessor;


    public JDKDelayTaskHolder(String threadName, int capacity, TaskProcessor taskProcessor) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
        if (taskProcessor == null) {
            throw new IllegalArgumentException("TaskProcessor cannot be null");
        }
        this.threadName = threadName;
        this.capacity = capacity;
        this.consumerThreadList = new ArrayList<>();
        this.taskProcessor = taskProcessor;
        this.start();
    }


    @Override
    public boolean put(DelayTaskDTO dto) {
        if (capacity.equals(size.get())) {
            return false;
        }
        synchronized (this) {
            if (capacity.equals(size.get())) {
                return false;
            }
            this.delayQueue.put(dto);
            size.incrementAndGet();
        }
        return true;
    }


    @Override
    public void start() {
        for (int i = 0; i < 5; i++) {
            String name = String.format("DelayTaskProcessor-ConsumerThread-%s-%d", this.threadName, i);
            Thread thread = new Thread(() -> {
                while (!stop) {
                    try {
                        DelayTaskDTO dto = delayQueue.take();
                        size.decrementAndGet();
                        taskProcessor.process(dto);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, name);
            logger.info( name + " start success");
            thread.start();
            consumerThreadList.add(thread);
        }
    }

    @Override
    public void stop() {
        this.stop= true;
        for (Thread thread : consumerThreadList) {
            thread.interrupt();
        }
    }


    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public DelayQueue<DelayTaskDTO> getDelayQueue() {
        return delayQueue;
    }

    public void setDelayQueue(DelayQueue<DelayTaskDTO> delayQueue) {
        this.delayQueue = delayQueue;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
