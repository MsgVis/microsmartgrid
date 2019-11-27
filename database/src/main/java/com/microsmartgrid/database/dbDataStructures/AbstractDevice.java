package com.microsmartgrid.database.dbDataStructures;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDevice {

	private int id;
	private LocalDateTime timestamp;
	private Map<String, Object> metaInformation = new HashMap<>();

	protected AbstractDevice() {
	}

	public AbstractDevice(LocalDateTime timestamp, Map<String, Object> metaInformation) {
		this.timestamp = timestamp;
		this.metaInformation = metaInformation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@JsonAnyGetter
	public Map<String, Object> getMetaInformation() {
		return metaInformation;
	}

	@JsonAnySetter
	public void setMetaInformation(Map<String, Object> metaInformation) {
		this.metaInformation = metaInformation;
	}
}
