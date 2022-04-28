/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis.dis;

import com.huaweicloud.dis.DIS;
import com.huaweicloud.dis.DISClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.invoke.MethodHandles;

@Configuration
public class DISConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DISProperties disProperties;

    public DISConfig(DISProperties disProperties) {
        LOGGER.info("Initializing DISConfig");
        this.disProperties = disProperties;
    }

    @Bean
    public DIS dis() {
        LOGGER.debug("Initializing DIS");
        return DISClientBuilder.standard()
                .withEndpoint(disProperties.getEndpoint())
                .withAk(disProperties.getAsk())
                .withSk(disProperties.getSk())
                .withProjectId(disProperties.getProjectId())
                .withRegion(disProperties.getRegion())
                .build();
    }
}