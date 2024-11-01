package com.ssn.delay.redission;

import com.ssn.delay.api.DelayTask;
import com.ssn.delay.api.DelayTaskProperties;
import com.ssn.delay.api.TaskProcessor;
import com.ssn.delay.api.model.DelayTaskDTO;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedissonDelayTaskHolder implements DelayTask {

    private final Logger logger = LoggerFactory.getLogger(RedissonDelayTaskHolder.class);

    private final RedissonClient redissonClient;

    private final DelayTaskProperties delayTaskProperties;

    private final RBlockingQueue<DelayTaskDTO> blockingQueue;

    private final RDelayedQueue<DelayTaskDTO> delayedQueue;

    private final TaskProcessor taskProcessor;

    private List<Thread> consumerThreadList;

    //是否已停止
    private volatile boolean stop = false;

    public RedissonDelayTaskHolder(RedissonClient redissonClient, DelayTaskProperties delayTaskProperties, TaskProcessor taskProcessor) {
        this.redissonClient = redissonClient;
        this.delayTaskProperties = delayTaskProperties;
        this.blockingQueue = redissonClient.getBlockingQueue(delayTaskProperties.getDelayQueueName());
        this.delayedQueue =  redissonClient.getDelayedQueue(this.blockingQueue);
        this.taskProcessor = taskProcessor;
    }

    @Override
    public boolean put(DelayTaskDTO dto) {
        this.delayedQueue.offer(dto,dto.getDelay(TimeUnit.MILLISECONDS),TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public void start() {
        for (int i = 0; i < 5; i++) {
            String name = String.format("DelayTaskProcessor-ConsumerThread-%s-%d", delayTaskProperties.getName(), i);
            Thread thread = new Thread(() -> {
                while (!stop) {
                    try {
                        DelayTaskDTO dto = blockingQueue.take();
                        this.taskProcessor.process(dto);
                    } catch (InterruptedException e) {
                        logger.error("taking task from redisson queue error", e);
                    }
                }
            }, name);
            logger.info( name + " start success with redisson");
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
}
