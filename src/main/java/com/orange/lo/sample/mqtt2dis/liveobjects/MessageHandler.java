/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis.liveobjects;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.orange.lo.sample.mqtt2dis.dis.DISMessageSender;
import com.orange.lo.sample.mqtt2dis.dis.DISProperties;
import com.orange.lo.sample.mqtt2dis.utils.Counters;

import io.micrometer.core.instrument.Counter;

@Component
public class MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DISMessageSender disMessageSender;
    private final DISProperties disProperties;
    private final Queue<String> messageQueue;
    private final Counter mesasageReadCounter;

    public MessageHandler(DISMessageSender disMessageSender, DISProperties disProperties, Queue<String> messageQueue,
                            Counters counters) {
        this.disMessageSender = disMessageSender;
        this.disProperties = disProperties;
        this.messageQueue = messageQueue;
        this.mesasageReadCounter = counters.getMesasageReadCounter();
    }

    public void handleMessage(String message) {
    	mesasageReadCounter.increment();
        messageQueue.add(message);
    }

    @Scheduled(fixedDelayString = "${flexible-engine.dis.message-sending-fixed-delay}")
    public void send() {
        if (!messageQueue.isEmpty()) {
            LOGGER.info("Start retriving messages...");
            List<String> messageBatch = new ArrayList<>(disProperties.getMessageBatchSize());
            while (!messageQueue.isEmpty()) {
                messageBatch.add(messageQueue.poll());
                if (messageBatch.size() == disProperties.getMessageBatchSize()) {
                    disMessageSender.send(messageBatch);
                    messageBatch.clear();
                }
            }
            if (!messageBatch.isEmpty()) {
                disMessageSender.send(messageBatch);
            }
        }
    }
}