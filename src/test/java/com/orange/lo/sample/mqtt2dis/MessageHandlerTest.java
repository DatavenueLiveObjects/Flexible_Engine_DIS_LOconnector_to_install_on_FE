package com.orange.lo.sample.mqtt2dis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orange.lo.sample.mqtt2dis.dis.DISMessageSender;
import com.orange.lo.sample.mqtt2dis.dis.DISProperties;
import com.orange.lo.sample.mqtt2dis.utils.Counters;

import io.micrometer.core.instrument.Counter;

@ExtendWith(MockitoExtension.class)
class MessageHandlerTest {

    public static final int BATCH_SIZE = 10;
    public static final String MESSAGE = "hello world";
    @Mock
    private DISMessageSender disMessageSender;
    @Mock
    private DISProperties disProperties;
    @Mock
    private Counter evtReceived;
    @Mock
    private Counters counters;
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

        int numberOfMessages = BATCH_SIZE * 2 + 3;
        for (int i = 0; i < numberOfMessages; i++) {
            messageHandler.handleMessage(MESSAGE + i);
        }

        messageHandler.send();

        assertEquals(0, messageQueueStub.size());
        verify(disMessageSender, times(3)).send(anyList());
    }
}