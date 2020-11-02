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
        String message = "hello world";
        messageHandler.handleMessage(message);

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
        String message = "hello world";

        messageHandler.handleMessage(message);
        messageHandler.send();

        assertEquals(0, messageQueueStub.size());
        verify(disMessageSender, times(1)).send(anyList());
    }
}