/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis.dis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flexible-engine.dis")
public class DISProperties {

    private String endpoint;
    private String ask;
    private String sk;
    private String projectId;
    private String region;
    private String streamName;
    private int messageBatchSize;
    private int messageSendingFixedDelay;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public int getMessageBatchSize() {
        return messageBatchSize;
    }

    public void setMessageBatchSize(int messageBatchSize) {
        this.messageBatchSize = messageBatchSize;
    }

    public int getMessageSendingFixedDelay() {
        return messageSendingFixedDelay;
    }

    public void setMessageSendingFixedDelay(int messageSendingFixedDelay) {
        this.messageSendingFixedDelay = messageSendingFixedDelay;
    }
    
    @Override
    public String toString() {
        return "DISProperties{" +
                "endpoint='" + endpoint + '\'' +
                ", ask='" + ask + '\'' +
                ", sk='" + sk + '\'' +
                ", projectId='" + projectId + '\'' +
                ", region='" + region + '\'' +
                ", streamName='" + streamName + '\'' +
                ", messageBatchSize=" + messageBatchSize +
                ", messageSendingFixedDelay=" + messageSendingFixedDelay +
                '}';
    }
}