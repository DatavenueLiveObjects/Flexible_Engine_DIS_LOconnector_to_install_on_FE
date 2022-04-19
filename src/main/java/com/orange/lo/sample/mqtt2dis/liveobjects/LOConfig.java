/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis.liveobjects;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orange.lo.sample.mqtt2dis.MessageHandler;
import com.orange.lo.sdk.LOApiClient;
import com.orange.lo.sdk.LOApiClientParameters;
import com.orange.lo.sdk.fifomqtt.DataManagementFifo;

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
        DataManagementFifo dataManagementFifo = loApiClient.getDataManagementFifo();
        dataManagementFifo.connectAndSubscribe();
        return loApiClient;
    }

    LOApiClientParameters loApiClientParameters() {
        return LOApiClientParameters.builder()
                .hostname(loProperties.getHostname())
                .username(loProperties.getUsername())
                .apiKey(loProperties.getApiKey())
                .topics(loProperties.getTopics())
                .dataManagementMqttCallback(messageHandler::handleMessage)
                .connectorType(loProperties.getConnectorType())
                .connectorVersion(getConnectorVersion())
                .build();
    }
    
    private String getConnectorVersion() {
    	MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = null;
        try {			
	        if ((new File("pom.xml")).exists()) {
	          model = reader.read(new FileReader("pom.xml"));
	        } else {
	          model = reader.read(
	            new InputStreamReader(
	            	LOConfig.class.getResourceAsStream(
	                "/META-INF/maven/com.orange.lo.sample/mqtt2dis/pom.xml"
	              )
	            )
	          );
	        }
	        return model.getVersion().replace(".", "_");
        } catch (Exception e) {
			return "";
		}
    }
    
}