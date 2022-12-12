package com.orange.lo.sample.mqtt2dis.modify;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.orange.lo.sample.mqtt2dis.ConnectorApplication;
import com.orange.lo.sample.mqtt2dis.dis.DISProperties;
import com.orange.lo.sample.mqtt2dis.liveobjects.LOProperties;

@Component
public class ModifyConfigurationService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
    private LOProperties loProperties;
	private DISProperties disProperties;
	private File configurationFile;

    public ModifyConfigurationService(LOProperties loProperties, DISProperties disProperties, File configurationFile) {
		this.loProperties = loProperties;
		this.disProperties = disProperties;
		this.configurationFile = configurationFile;
    }

    public ModifyConfigurationProperties getProperties() {
        ModifyConfigurationProperties modifyConfigurationProperties = new ModifyConfigurationProperties();

        modifyConfigurationProperties.setLoApiKey(loProperties.getApiKey());
        modifyConfigurationProperties.setLoTopics(loProperties.getTopics());
        
        modifyConfigurationProperties.setDisAccessKey(disProperties.getAsk());
        modifyConfigurationProperties.setDisSecretKey(disProperties.getSk());
        modifyConfigurationProperties.setDisProjectId(disProperties.getProjectId());
        modifyConfigurationProperties.setDisRegion(disProperties.getRegion());
        modifyConfigurationProperties.setDisStreamName(disProperties.getStreamName());
        
        return modifyConfigurationProperties;
    }

    public void modify(ModifyConfigurationProperties modifyConfigurationProperties) {
    	try {
    		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    		
			ObjectNode root = (ObjectNode) mapper.readTree(configurationFile);
			
			ObjectNode loNode = (ObjectNode) root.get("lo-mqtt");
			setProperty(loNode, "api-key", () -> modifyConfigurationProperties.getLoApiKey());
			setProperty(loNode, "topics", () -> modifyConfigurationProperties.getLoTopics());
			
			ObjectNode disNode = (ObjectNode) root.get("flexible-engine").get("dis");
			setProperty(disNode, "ask", () -> modifyConfigurationProperties.getDisAccessKey());
			setProperty(disNode, "sk", () -> modifyConfigurationProperties.getDisSecretKey());
			setProperty(disNode, "projectId", () -> modifyConfigurationProperties.getDisProjectId());
			setProperty(disNode, "region", () -> modifyConfigurationProperties.getDisRegion());
			setProperty(disNode, "stream-name", () -> modifyConfigurationProperties.getDisRegion());
			
			mapper.writer().writeValue(configurationFile, root);
			
    		ConnectorApplication.restart();
		} catch (IOException e) {
			throw new ModifyException("Error while modifying configuration", e);
		}
    }
    private void setProperty(ObjectNode node, String parameterName, Supplier<Object> parameterSupplier) {

		Object parameter = parameterSupplier.get();
		if (Objects.isNull(parameter)) {
			return;
		}

		if (parameter instanceof Integer) {
			node.put(parameterName, (Integer) parameter);
		} else if (parameter instanceof Long) {
			node.put(parameterName, (Long) parameter);
		} else if (parameter instanceof Boolean) {
			node.put(parameterName, (Boolean) parameter);
		} else if (parameter instanceof List && ((List)parameter).size()>0) {
			ArrayNode arrayNode = node.putArray(parameterName);
			((List)parameter).forEach(t-> arrayNode.add((String)t));
		} else if (parameter instanceof String){
			node.put(parameterName, String.valueOf(parameter));
		}
	}
}