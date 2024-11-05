package com.delay.test;

import com.ssn.delay.api.DelayTask;
import com.ssn.delay.api.model.DelayTaskDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("delay")
public class DelayController {


    private final DelayTask delayTask;

    public DelayController(DelayTask delayTask) {
        this.delayTask = delayTask;
    }

    @GetMapping("{time}")
    public void delay(@PathVariable Long time) {
        DelayTaskDTO<String> dto = new DelayTaskDTO<>();
        dto.setData(UUID.randomUUID().toString());
        dto.setMessageType("test");
        dto.setTaskExecuteTimeMs(System.currentTimeMillis() + time);
        delayTask.put(dto);
        return;
    }
}
