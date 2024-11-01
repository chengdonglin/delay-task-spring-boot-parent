package com.ssn.delay.api;

import com.ssn.delay.api.model.DelayTaskDTO;

public interface DelayTask extends CycleLife{

    boolean put(DelayTaskDTO dto);

}
