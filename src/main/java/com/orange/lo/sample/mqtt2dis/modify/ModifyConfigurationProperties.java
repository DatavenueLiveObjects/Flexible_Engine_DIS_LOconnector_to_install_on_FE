package com.orange.lo.sample.mqtt2dis.modify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyConfigurationProperties {

    private String loApiKey;
    private List<String> loTopics = new ArrayList<>();
    
    private String disAccessKey;
    private String disSecretKey;
    private String disProjectId;
    private String disStreamName;
    private String disRegion;
	public String getLoApiKey() {
		return loApiKey;
	}
	public void setLoApiKey(String loApiKey) {
		this.loApiKey = loApiKey;
	}
	public List<String> getLoTopics() {
		return loTopics;
	}
	public void setLoTopics(List<String> loTopics) {
		this.loTopics = loTopics;
	}
	public String getDisAccessKey() {
		return disAccessKey;
	}
	public void setDisAccessKey(String disAccessKey) {
		this.disAccessKey = disAccessKey;
	}
	public String getDisSecretKey() {
		return disSecretKey;
	}
	public void setDisSecretKey(String disSecretKey) {
		this.disSecretKey = disSecretKey;
	}
	public String getDisProjectId() {
		return disProjectId;
	}
	public void setDisProjectId(String disProjectId) {
		this.disProjectId = disProjectId;
	}
	public String getDisStreamName() {
		return disStreamName;
	}
	public void setDisStreamName(String disStreamName) {
		this.disStreamName = disStreamName;
	}
	public String getDisRegion() {
		return disRegion;
	}
	public void setDisRegion(String disRegion) {
		this.disRegion = disRegion;
	}
	@Override
	public String toString() {
		return "ModifyConfigurationProperties [loApiKey=***" + ", loTopics=" + loTopics + ", disAccessKey=***"
				+ ", disSecretKey=***" + ", disProjectId=" + disProjectId
				+ ", disStreamName=" + disStreamName + ", disRegion=" + disRegion + "]";
	}	
}