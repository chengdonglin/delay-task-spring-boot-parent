package com.delay.test;

import com.ssn.delay.api.TaskProcessor;
import com.ssn.delay.api.model.DelayTaskDTO;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor implements TaskProcessor {
    @Override
    public void process(DelayTaskDTO dto) {
        System.out.println(Thread.currentThread().getName() + "---" +dto.toString());
    }
}
