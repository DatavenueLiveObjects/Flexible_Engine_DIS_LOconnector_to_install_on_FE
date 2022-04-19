/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis.liveobjects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "lo-mqtt")
public class LOProperties {

	private static final String CONNECTOR_TYPE = "LO_FE_DIS_ADAPTER";
	
    private String hostname;
    private String username;
    private String apiKey;
    private List<String> topics;
    private int recoveryInterval;
    private int completionTimeout;
    private int connectionTimeout;
    private int qos;
    private int keepAliveIntervalSeconds;

    public LOProperties() {
        this.topics = new ArrayList<>();
    }
    
    public String getConnectorType() {
        return CONNECTOR_TYPE;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public int getRecoveryInterval() {
        return recoveryInterval;
    }

    public void setRecoveryInterval(int recoveryInterval) {
        this.recoveryInterval = recoveryInterval;
    }

    public int getCompletionTimeout() {
        return completionTimeout;
    }

    public void setCompletionTimeout(int completionTimeout) {
        this.completionTimeout = completionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public int getKeepAliveIntervalSeconds() {
        return keepAliveIntervalSeconds;
    }

    public void setKeepAliveIntervalSeconds(int keepAliveIntervalSeconds) {
        this.keepAliveIntervalSeconds = keepAliveIntervalSeconds;
    }

    @Override
    public String toString() {
        return "LOProperties{" +
                "uri='" + hostname + '\'' +
                ", username='" + username + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", topics=" + topics +
                ", recoveryInterval=" + recoveryInterval +
                ", completionTimeout=" + completionTimeout +
                ", connectionTimeout=" + connectionTimeout +
                ", qos=" + qos +
                ", keepAliveIntervalSeconds=" + keepAliveIntervalSeconds +                
                '}';
    }
}