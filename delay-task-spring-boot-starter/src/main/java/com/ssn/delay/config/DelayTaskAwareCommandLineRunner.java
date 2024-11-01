package com.ssn.delay.config;

import com.ssn.delay.api.DelayTask;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

public class DelayTaskAwareCommandLineRunner implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;


    @Override
    public void run(String... args) throws Exception {
        DelayTask bean = applicationContext.getBean(DelayTask.class);
        if (!Objects.isNull(bean)) {
            bean.start();
            System.out.println("启动延时队列");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
