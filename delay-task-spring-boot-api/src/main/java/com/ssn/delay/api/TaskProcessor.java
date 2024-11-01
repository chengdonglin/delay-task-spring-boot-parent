package com.ssn.delay.api;

import com.ssn.delay.api.model.DelayTaskDTO;

public interface TaskProcessor {

    void process(DelayTaskDTO dto);

}
