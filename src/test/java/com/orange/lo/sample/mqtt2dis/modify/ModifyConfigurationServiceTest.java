/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.mqtt2dis.modify;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orange.lo.sample.mqtt2dis.ConnectorApplication;
import com.orange.lo.sample.mqtt2dis.dis.DISProperties;
import com.orange.lo.sample.mqtt2dis.liveobjects.LOProperties;

@ExtendWith(MockitoExtension.class)
public class ModifyConfigurationServiceTest {

	private static final String LO_API_KEY = "abcd";
	private static final String LO_API_KEY_UPDATED = "dcba";
	
	private static final String PROJECT_ID = "abc11";
	private static final String PROJECT_ID_UPDATED = "abc22";
	
	@TempDir
	File tempDir;
	
	private File configurationFile;
	private File pubSubAuthFile;
	
	private ModifyConfigurationService modifyConfigurationService;
	
	@BeforeEach
    void setUp() throws IOException {
		LOProperties loProperties = new LOProperties();
		loProperties.setApiKey(LO_API_KEY);
		
		DISProperties disProperties = new DISProperties();
		disProperties.setProjectId(PROJECT_ID);
		
		
		configurationFile = new File(tempDir, "application.yml");
		FileUtils.fileWrite(configurationFile, "lo-mqtt:\n  api-key: " + LO_API_KEY + "\nflexible-engine:\n  dis:\n    projectId: " + PROJECT_ID);

		modifyConfigurationService = new ModifyConfigurationService(loProperties, disProperties, configurationFile);
	}
	
	@Test
	public void shouldReadParameters() {
		//when
		ModifyConfigurationProperties properties = modifyConfigurationService.getProperties();
		
		//then
		Assertions.assertEquals(LO_API_KEY, properties.getLoApiKey());
		Assertions.assertEquals(PROJECT_ID, properties.getDisProjectId());
	}
	
	@Test
	public void shouldUpdateParameters() throws IOException {
		//given
		MockedStatic<ConnectorApplication> connectorApplication = Mockito.mockStatic(ConnectorApplication.class);
		ModifyConfigurationProperties modifyConfigurationProperties = new ModifyConfigurationProperties();
		modifyConfigurationProperties.setDisProjectId(PROJECT_ID_UPDATED);
		modifyConfigurationProperties.setLoApiKey(LO_API_KEY_UPDATED);
		
		//when
		modifyConfigurationService.modify(modifyConfigurationProperties);
		
		//then
		String configuratioFileContent = FileUtils.fileRead(configurationFile);
		
		connectorApplication.verify(ConnectorApplication::restart);
		Assertions.assertTrue(configuratioFileContent.contains(LO_API_KEY_UPDATED));
		Assertions.assertTrue(configuratioFileContent.contains(PROJECT_ID_UPDATED));
	}
}