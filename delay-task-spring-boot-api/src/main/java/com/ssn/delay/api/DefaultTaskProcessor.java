package com.ssn.delay.api;


import com.ssn.delay.api.model.DelayTaskDTO;

public class DefaultTaskProcessor implements TaskProcessor {

    @Override
    public void process(DelayTaskDTO dto) {
        System.out.println(dto.toString());
    }


}
