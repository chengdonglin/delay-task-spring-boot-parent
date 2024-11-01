package com.ssn.delay.config;

import com.ssn.delay.api.DelayTask;
import com.ssn.delay.api.DelayTaskProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

public class DelayTaskAwareCommandLineRunner implements CommandLineRunner, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(DelayTaskAwareCommandLineRunner.class);

    private ApplicationContext applicationContext;


    @Override
    public void run(String... args) throws Exception {
        DelayTaskProperties properties = applicationContext.getBean(DelayTaskProperties.class);
        log.info("延时定时任务配置信息 {}", properties);
        DelayTask bean = applicationContext.getBean(DelayTask.class);
        if (!Objects.isNull(bean)) {
            bean.start();
            log.info("延时定时任务启动成功");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
