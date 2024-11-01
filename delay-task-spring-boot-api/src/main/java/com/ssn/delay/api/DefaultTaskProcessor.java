package com.ssn.delay.api;


import com.ssn.delay.api.model.DelayTaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTaskProcessor implements TaskProcessor {


    private static final Logger log = LoggerFactory.getLogger(DefaultTaskProcessor.class);

    @Override
    public void process(DelayTaskDTO dto) {
        log.info("Processing task: {}",dto.toString());
    }


}
