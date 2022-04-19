/**
 * Copyright (c) Orange, Inc. and its affiliates. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis.dis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DISPropertiesTest {

    @Test
    void shouldSetEndpointCorrectly() {
        String endpoint = "https://dis.orange-business.com";
        DISProperties disProperties = new DISProperties();
        disProperties.setEndpoint(endpoint);

        assertEquals(endpoint, disProperties.getEndpoint());
    }

    @Test
    void shouldSetAskCorrectly() {
        String ask = "A5KK3YF0RD1S";
        DISProperties disProperties = new DISProperties();
        disProperties.setAsk(ask);

        assertEquals(ask, disProperties.getAsk());
    }

    @Test
    void shouldSetSkCorrectly() {
        String sk = "SKK3YF0RD1S";
        DISProperties disProperties = new DISProperties();
        disProperties.setSk(sk);

        assertEquals(sk, disProperties.getSk());
    }

    @Test
    void shouldSetProjectIdCorrectly() {
        String projectId = "pr0j3ct1d";
        DISProperties disProperties = new DISProperties();
        disProperties.setProjectId(projectId);

        assertEquals(projectId, disProperties.getProjectId());
    }

    @Test
    void shouldSetRegionCorrectly() {
        String region = "eu-region-0";
        DISProperties disProperties = new DISProperties();
        disProperties.setRegion(region);

        assertEquals(region, disProperties.getRegion());
    }

    @Test
    void shouldSetStreamNameCorrectly() {
        String streamName = "dis-connector-stream-name";
        DISProperties disProperties = new DISProperties();
        disProperties.setStreamName(streamName);

        assertEquals(streamName, disProperties.getStreamName());
    }
    
    @Test
    void shouldSetMessageBatchSizeCorrectly() {
        int messageBatchSize = 10;
        DISProperties disProperties = new DISProperties();
        disProperties.setMessageBatchSize(messageBatchSize);

        assertEquals(messageBatchSize, disProperties.getMessageBatchSize());
    }

    @Test
    void shouldSetMessageSendingFixedDelayCorrectly() {
        int fixedDelay = 1000;
        DISProperties disProperties = new DISProperties();
        disProperties.setMessageSendingFixedDelay(fixedDelay);

        assertEquals(fixedDelay, disProperties.getMessageSendingFixedDelay());
    }
}