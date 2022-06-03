/**
 * Copyright (c) Orange, Inc. and its affiliates. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis.liveobjects;

import com.huaweicloud.dis.DIS;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequestEntry;
import com.huaweicloud.dis.iface.data.response.PutRecordsResult;
import com.orange.lo.sample.mqtt2dis.dis.DISMessageSender;
import com.orange.lo.sample.mqtt2dis.dis.DISProperties;
import com.orange.lo.sample.mqtt2dis.liveobjects.MessageHandler;
import com.orange.lo.sample.mqtt2dis.utils.Counters;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageHandlerTest {

    public static final int BATCH_SIZE = 10;
    public static final int MESSAGES_OVER_BATCH = 3;
    public static final String MESSAGE = "hello world";
    @Mock
    private DISMessageSender disMessageSender;
    @Mock
    private DISProperties disProperties;
    @Mock
    private Counter evtReceived;
    @Mock
    private Counters counters;
    @Captor
    private ArgumentCaptor<PutRecordsRequest> captor;
    private Queue<String> messageQueueStub;
    private MessageHandler messageHandler;

    @BeforeEach
    void setUp() {
        when(counters.evtReceived()).thenReturn(evtReceived);
        this.messageQueueStub = new ConcurrentLinkedQueue<>();
        this.messageHandler = new MessageHandler(disMessageSender, disProperties, messageQueueStub, counters);
    }

    @Test
    void shouldAddMessageToQueueAndIncrementReceivedEventsCountWhenMessageArrives() {
        messageHandler.handleMessage(MESSAGE);

        assertEquals(1, messageQueueStub.size());
        verify(evtReceived, times(1)).increment();
    }

    @Test
    void shouldNotCallDISMessageSenderWhenMessageQueueIsEmpty() {
        messageHandler.send();

        assertEquals(0, messageQueueStub.size());
        verify(disMessageSender, times(0)).send(anyList());
    }

    @Test
    void shouldCallDISMessageSenderAndSendAllMessagesWhenMessageQueueIsNoTEmpty() {
        messageHandler.handleMessage(MESSAGE);
        messageHandler.send();

        assertEquals(0, messageQueueStub.size());
        verify(disMessageSender, times(1)).send(anyList());
    }

    @Test
    void shouldCallDISMessageSenderUntilAllMessagesAreSentWhenMessageQueueIsNoTEmpty() {
        when(disProperties.getMessageBatchSize()).thenReturn(BATCH_SIZE);

        int numberOfMessages = BATCH_SIZE * 2 + MESSAGES_OVER_BATCH;
        for (int i = 0; i < numberOfMessages; i++) {
            messageHandler.handleMessage(MESSAGE + i);
        }

        messageHandler.send();

        assertEquals(0, messageQueueStub.size());
        verify(disMessageSender, times(3)).send(anyList());
    }

    @Test
    void shouldPassMessagesCorrectlyToDISClientWhenMessageQueueIsNoTEmpty() {
        DIS disClient = Mockito.mock(DIS.class);
        PutRecordsResult putRecordsResult = Mockito.mock(PutRecordsResult.class);
        Counter evtAttempt = Mockito.mock(Counter.class);
        Counter evtFailed = Mockito.mock(Counter.class);
        Counter evtSent = Mockito.mock(Counter.class);
        when(counters.evtAttempt()).thenReturn(evtAttempt);
        when(counters.evtFailed()).thenReturn(evtFailed);
        when(counters.evtSent()).thenReturn(evtSent);
        when(disClient.putRecords(any(PutRecordsRequest.class))).thenReturn(putRecordsResult);
        when(putRecordsResult.getFailedRecordCount()).thenReturn(new AtomicInteger(0));
        when(putRecordsResult.getRecords()).thenReturn(new ArrayList<>());
        when(disProperties.getMessageBatchSize()).thenReturn(BATCH_SIZE);

        DISMessageSender disMessageSender = new DISMessageSender(disProperties, disClient, counters);
        messageHandler = new MessageHandler(disMessageSender, disProperties, messageQueueStub, counters);

        List<String> messages = new ArrayList<>();
        int numberOfMessages = BATCH_SIZE * 2 + MESSAGES_OVER_BATCH;
        for (int i = 0; i < numberOfMessages; i++) {
            String message = MESSAGE + i;
            messageHandler.handleMessage(message);
            messages.add(message);
        }

        messageHandler.send();

        assertEquals(0, messageQueueStub.size());
        verify(disClient, times(3)).putRecords(captor.capture());
        List<List<PutRecordsRequestEntry>> allValues = captor.getAllValues().stream()
                .map(PutRecordsRequest::getRecords)
                .collect(Collectors.toList());

        List<PutRecordsRequestEntry> firstBatch = allValues.get(0);
        List<PutRecordsRequestEntry> secondBatch = allValues.get(1);
        List<PutRecordsRequestEntry> thirdBatch = allValues.get(2);

        assertEquals(BATCH_SIZE, firstBatch.size());
        assertEquals(BATCH_SIZE, secondBatch.size());
        assertEquals(MESSAGES_OVER_BATCH, thirdBatch.size());

        PutRecordsRequestEntry putRecordsRequestEntry = firstBatch.get(1);
        assertEquals(messages.get(1), StandardCharsets.UTF_8.decode(putRecordsRequestEntry.getData()).toString());

        PutRecordsRequestEntry putRecordsRequestEntry2 = secondBatch.get(5);
        assertEquals(messages.get(15), StandardCharsets.UTF_8.decode(putRecordsRequestEntry2.getData()).toString());

        PutRecordsRequestEntry putRecordsRequestEntry3 = thirdBatch.get(2);
        assertEquals(messages.get(22), StandardCharsets.UTF_8.decode(putRecordsRequestEntry3.getData()).toString());
    }
}
