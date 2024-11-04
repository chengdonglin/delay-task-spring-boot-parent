package com.ssn.delay.jdk;

import com.ssn.delay.api.AbstractDelayTaskHolder;
import com.ssn.delay.api.DelayTaskProperties;
import org.slf4j.Logger;
import com.ssn.delay.api.TaskProcessor;
import com.ssn.delay.api.model.DelayTaskDTO;
import org.slf4j.LoggerFactory;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class JDKDelayTaskHolder extends AbstractDelayTaskHolder {

    private final Logger logger = LoggerFactory.getLogger(JDKDelayTaskHolder.class);

    private Integer capacity = 10000;


    //延迟队列
    private DelayQueue<DelayTaskDTO> delayQueue = new DelayQueue<>();

    //当前待处理的任务数量
    private AtomicInteger size = new AtomicInteger(0);


    public JDKDelayTaskHolder(DelayTaskProperties properties, TaskProcessor taskProcessor) {
        super(taskProcessor, properties);
        if (capacity <= 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
        this.capacity = properties.getCapacity();
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
        for (int i = 0; i < properties.getConcurrentConsumers(); i++) {
            String name = getThreadName(i);
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
            logger.info( name + " start success with jdk");
            thread.start();
            consumerThreadList.add(thread);
        }
    }

}
