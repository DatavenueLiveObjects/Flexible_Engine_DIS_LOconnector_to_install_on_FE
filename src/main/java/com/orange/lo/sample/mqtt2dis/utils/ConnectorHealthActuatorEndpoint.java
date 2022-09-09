package com.orange.lo.sample.mqtt2dis.utils;

import com.orange.lo.sdk.LOApiClient;
import com.orange.lo.sdk.fifomqtt.DataManagementFifo;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class ConnectorHealthActuatorEndpoint implements HealthIndicator {

    LOApiClient loApiClient;

    public ConnectorHealthActuatorEndpoint(LOApiClient loApiClient) {
        this.loApiClient = loApiClient;
    }

    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }

    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder(Status.UP);
        DataManagementFifo dataManagementFifo = loApiClient.getDataManagementFifo();

        builder.withDetail("loMqttConnectionStatus", dataManagementFifo.isConnected());
        return builder.build();
    }
}