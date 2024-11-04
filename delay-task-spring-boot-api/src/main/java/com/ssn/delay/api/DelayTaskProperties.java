package com.ssn.delay.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "delay")
public class DelayTaskProperties {

    // 延时队列类型
    private String type = "jdk"; // JDK, redis, redisson

    // 线程池名称
    private String name = "delay-task-";

    // 容量，只对jdk有效
    private Integer capacity = 10000;

    // 延时队列名称，只对redisson有效
    private String delayQueueName = "delay-task-queue";

    // 消费者数量
    private Integer concurrentConsumers = 5;


    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public String getDelayQueueName() {
        return delayQueueName;
    }

    public void setDelayQueueName(String delayQueueName) {
        this.delayQueueName = delayQueueName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public void setConcurrentConsumers(Integer concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    @Override
    public String toString() {
        return "DelayTaskProperties{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", delayQueueName='" + delayQueueName + '\'' +
                ", concurrentConsumers=" + concurrentConsumers +
                ", url='" + url + '\'' +
                '}';
    }
}
