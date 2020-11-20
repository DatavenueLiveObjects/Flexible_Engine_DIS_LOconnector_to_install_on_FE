/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis.liveobjects;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orange.lo.sdk.LOApiClient;
import com.orange.lo.sdk.LOApiClientParameters;
import com.orange.lo.sdk.mqtt.DataManagementMqtt;
import com.orange.lo.sample.mqtt2dis.MessageHandler;

@Configuration
public class LOConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final LOProperties loProperties;
    private final MessageHandler messageHandler;

    public LOConfig(LOProperties loProperties, MessageHandler messageHandler) {
        LOGGER.info("Initializing LOConfig");
        this.loProperties = loProperties;
        this.messageHandler = messageHandler;
    }

    @Bean
    public LOApiClient loApiClient() {
        LOGGER.debug("Initializing LOApiClient");
        LOApiClientParameters parameters = loApiClientParameters();
        LOApiClient loApiClient = new LOApiClient(parameters);
        DataManagementMqtt dataManagementMqtt = loApiClient.getDataManagementMqtt();
        dataManagementMqtt.subscribe();
        return loApiClient;
    }

    LOApiClientParameters loApiClientParameters() {
        return LOApiClientParameters.builder()
                .hostname(loProperties.getHostname())
                .username(loProperties.getUsername())
                .apiKey(loProperties.getApiKey())
                .topics(loProperties.getTopics())
                .dataManagementMqttCallback(messageHandler::handleMessage)
                .build();
    }
}