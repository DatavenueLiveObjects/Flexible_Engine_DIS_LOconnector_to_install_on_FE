/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.step.StepRegistryConfig;

@Configuration
public class ApplicationConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
    @Bean
    public Queue<String> messageQueue() {
        return new ConcurrentLinkedQueue<>();
    }
    
    @Bean
    public StepRegistryConfig stepRegistryConfig() { 
    	return new StepRegistryConfig() {
		
			@Override
			public Duration step() {
				return Duration.ofMinutes(1);
			}
			
			@Override
			public String prefix() {
				return "";
			}
			
			@Override
			public String get(String key) {
				return null;
			}
    	};
    }
    
    @Bean
    public StepMeterRegistry stepMeterRegistry() {
	    return new StepMeterRegistry(stepRegistryConfig(), Clock.SYSTEM) {
			
			@Override
			protected TimeUnit getBaseTimeUnit() {
				return TimeUnit.MILLISECONDS;
			}
			
			@Override
			protected void publish() {
				getMeters().stream()
				    .filter(m -> m.getId().getName().startsWith("message") )
					.map(m -> get(m.getId().getName()).counter())
					.forEach(c -> LOGGER.info(c.getId().getName() + " = " + val(c)));
			}
			@Override
			public void start(ThreadFactory threadFactory) {
				super.start(Executors.defaultThreadFactory());
			}
		};
    }
    
    private long val(Counter cnt) {
        return Math.round(cnt.count());
    }
}