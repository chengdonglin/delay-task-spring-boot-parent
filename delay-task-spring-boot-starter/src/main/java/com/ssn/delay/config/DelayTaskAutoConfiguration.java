package com.ssn.delay.config;

import com.ssn.delay.api.DefaultTaskProcessor;
import com.ssn.delay.api.DelayTask;
import com.ssn.delay.api.DelayTaskProperties;
import com.ssn.delay.api.TaskProcessor;
import com.ssn.delay.jdk.JDKDelayTaskHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(DelayTaskProperties.class)
public class DelayTaskAutoConfiguration {


    @Autowired
    private DelayTaskProperties delayTaskProperties;


    @Bean
    @ConditionalOnMissingBean(TaskProcessor.class)
    public TaskProcessor getTaskProcessor() {
        return new DefaultTaskProcessor();
    }


    @Bean
    @ConditionalOnBean(TaskProcessor.class)
    public DelayTask getDelayTask(TaskProcessor taskProcessor) {
        DelayTaskProperties delayTaskProperties = this.delayTaskProperties;
        if (delayTaskProperties.getType().equalsIgnoreCase("jdk")) {
            return new JDKDelayTaskHolder(delayTaskProperties.getName(), delayTaskProperties.getCapacity(),taskProcessor);
        }
        throw new RuntimeException("Unsupported delay task type: " + delayTaskProperties.getType());
    }


    @Bean
    public DelayTaskAwareCommandLineRunner runDelayTask() {
        return new DelayTaskAwareCommandLineRunner();
    }

}
