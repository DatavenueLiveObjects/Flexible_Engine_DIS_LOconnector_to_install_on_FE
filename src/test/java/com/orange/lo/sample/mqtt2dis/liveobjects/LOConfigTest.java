package com.orange.lo.sample.mqtt2dis.liveobjects;


import com.orange.lo.sample.mqtt2dis.MessageHandler;
import com.orange.lo.sdk.LOApiClientParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class LOConfigTest {

    public static final String API_KEY = "abcDEfgH123I";
    public static final String URI = "liveobjects.orange-business.com";
    public static final List<String> TOPICS = Arrays.asList("topic-one", "topic-two");

    @Mock
    private MessageHandler messageHandler;
    private LOProperties loPropertiesStub;
    private LOConfig loConfig;

    @BeforeEach
    void setUp() {
        this.loPropertiesStub = new LOProperties();
        this.loPropertiesStub.setApiKey(API_KEY);
        this.loPropertiesStub.setHostname(URI);
        this.loPropertiesStub.setTopics(TOPICS);
        this.loConfig = new LOConfig(loPropertiesStub, messageHandler);
    }

    @Test
    void shouldCorrectlyCreateApiClientParameters() {
        LOApiClientParameters parameters = loConfig.loApiClientParameters();

        assertNotNull(parameters);
        assertEquals(loPropertiesStub.getApiKey(), parameters.getApiKey());
        assertEquals(loPropertiesStub.getHostname(), parameters.getHostname());
        assertEquals(loPropertiesStub.getTopics(), parameters.getTopics());
        assertNotNull(parameters.getDataManagementMqttCallback());
    }
}