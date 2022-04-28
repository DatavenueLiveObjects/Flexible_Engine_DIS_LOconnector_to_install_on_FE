/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis.dis;

import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequestEntry;
import com.huaweicloud.dis.iface.data.response.PutRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResultEntry;
import com.huaweicloud.dis.DIS;
import com.huaweicloud.dis.exception.DISClientException;
import com.orange.lo.sample.mqtt2dis.utils.Counters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DISMessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DISProperties disProperties;
    private final DIS disClient;
	private Counters counters;

    public DISMessageSender(DISProperties disProperties, DIS disClient, Counters counters) {
        this.disProperties = disProperties;
        this.disClient = disClient;
		this.counters = counters;
    }

    public void send(List<String> messages) {
    	counters.evtAttempt().increment(messages.size());
    	
        List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<>();
        for (String message : messages) {
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
            putRecordsRequestEntry.setData(buffer);
            putRecordsRequestEntry.setPartitionKey(String.valueOf(ThreadLocalRandom.current().nextInt(1_000_000)));
            putRecordsRequestEntryList.add(putRecordsRequestEntry);
        }
        
        PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
        putRecordsRequest.setStreamName(disProperties.getStreamName());
        putRecordsRequest.setRecords(putRecordsRequestEntryList);

        LOGGER.debug("Sending messages");
        PutRecordsResult putRecordsResult = null;
        try {
            putRecordsResult = disClient.putRecords(putRecordsRequest);
        } catch (DISClientException e) {
        	counters.evtFailed().increment(messages.size());
            LOGGER.error("Failed to get a response, please check params and retry. Error message [{}]", e.getMessage(), e);
            return;
        } catch (Exception e) {
        	counters.evtFailed().increment(messages.size());
            LOGGER.error("Unexpected exception {}", e.getMessage(), e);
            return;
        }        
        
        counters.evtFailed().increment(putRecordsResult.getFailedRecordCount().get());
        int recordsSent = putRecordsResult.getRecords().size() - putRecordsResult.getFailedRecordCount().get();
        counters.evtSent().increment(recordsSent);

        if (putRecordsResult.getFailedRecordCount().get() > 0) {
        	for (PutRecordsResultEntry putRecordsResultEntry : putRecordsResult.getRecords()) {
        		if (!StringUtils.isNullOrEmpty(putRecordsResultEntry.getErrorCode())) {
                    LOGGER.error("Put message failed, errorCode [{}], errorMessage [{}]",
                        putRecordsResultEntry.getErrorCode(),
                        putRecordsResultEntry.getErrorMessage());
                }
			}
        }
        
        if (LOGGER.isDebugEnabled()) {        	
        	for (PutRecordsResultEntry putRecordsResultEntry : putRecordsResult.getRecords()) {
        		if (StringUtils.isNullOrEmpty(putRecordsResultEntry.getErrorCode())) {
        			LOGGER.info("Put message success, partitionId [{}], sequenceNumber [{}]",
                            putRecordsResultEntry.getPartitionId(),
                            putRecordsResultEntry.getSequenceNumber());
                }
			}
        }
    }
}