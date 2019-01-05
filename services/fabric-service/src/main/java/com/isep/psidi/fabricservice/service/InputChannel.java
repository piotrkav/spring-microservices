package com.isep.psidi.fabricservice.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface InputChannel {
    String YARN_NEEDED_INPUT = "yarnNeededInput";

    @Input(YARN_NEEDED_INPUT)
    SubscribableChannel yarnNeededInput();


}
