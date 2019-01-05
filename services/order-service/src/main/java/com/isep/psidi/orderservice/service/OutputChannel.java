package com.isep.psidi.orderservice.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OutputChannel {


    String YARN_NEEDED_OUTPUT = "yarnNeededOutput";

    @Output(YARN_NEEDED_OUTPUT)
    MessageChannel colorCreatedOutput();


}
