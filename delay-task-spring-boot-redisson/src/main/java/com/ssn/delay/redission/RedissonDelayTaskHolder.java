package com.ssn.delay.redission;

import com.ssn.delay.api.AbstractDelayTaskHolder;
import com.ssn.delay.api.DelayTaskProperties;
import com.ssn.delay.api.TaskProcessor;
import com.ssn.delay.api.model.DelayTaskDTO;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class RedissonDelayTaskHolder extends AbstractDelayTaskHolder {

    private final Logger logger = LoggerFactory.getLogger(RedissonDelayTaskHolder.class);

    private final RedissonClient redissonClient;

    private final DelayTaskProperties delayTaskProperties;

    private final RBlockingQueue<DelayTaskDTO> blockingQueue;

    private final RDelayedQueue<DelayTaskDTO> delayedQueue;

    public RedissonDelayTaskHolder(RedissonClient redissonClient, DelayTaskProperties delayTaskProperties, TaskProcessor taskProcessor) {
        super(taskProcessor,delayTaskProperties);
        this.redissonClient = redissonClient;
        this.delayTaskProperties = delayTaskProperties;
        this.blockingQueue = this.redissonClient.getBlockingQueue(delayTaskProperties.getDelayQueueName());
        this.delayedQueue =  this.redissonClient.getDelayedQueue(this.blockingQueue);
    }

    @Override
    public boolean put(DelayTaskDTO dto) {
        this.delayedQueue.offer(dto,dto.getDelay(TimeUnit.MILLISECONDS),TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public void start() {
        for (int i = 0; i < this.delayTaskProperties.getConcurrentConsumers(); i++) {
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

}
