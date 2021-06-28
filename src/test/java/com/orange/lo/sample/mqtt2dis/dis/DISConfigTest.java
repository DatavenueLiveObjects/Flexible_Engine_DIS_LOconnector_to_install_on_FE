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
