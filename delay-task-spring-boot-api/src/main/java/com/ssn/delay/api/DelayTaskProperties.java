package com.ssn.delay.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "delay")
public class DelayTaskProperties {

    private String type = "jdk"; // JDK, redis, redisson

    private String name = "delay-task-";

    private Integer capacity = 10000;

    private String delayQueueName = "delay-task-queue";


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

    @Override
    public String toString() {
        return "DelayTaskProperties{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", delayQueueName='" + delayQueueName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
