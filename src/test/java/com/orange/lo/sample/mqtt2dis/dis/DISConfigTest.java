/**
 * Copyright (c) Orange, Inc. and its affiliates. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis.dis;

import com.huaweicloud.dis.DIS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DISConfigTest {

    @Test
    void shouldCorrectlyCreateDis() {
        DISProperties disPropertiesStub = new DISProperties();
        disPropertiesStub.setAsk("sdcs");
        disPropertiesStub.setSk("fdfdf");
        disPropertiesStub.setProjectId("jeiksdk");
        disPropertiesStub.setRegion("asdasdasd");
        DISConfig disConfig = new DISConfig(disPropertiesStub);

        DIS dis = disConfig.dis();
        assertNotNull(dis);
    }
}
