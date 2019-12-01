package com.microsmartgrid.database.dbDataStructures;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class AbstractDevice implements Serializable {

	private int id;
	private Instant timestamp;
	@JsonAnySetter
	private Map<String, Object> metaInformation = new HashMap<>();

	protected AbstractDevice() {
	}

	public AbstractDevice(Instant timestamp, Map<String, Object> metaInformation) {
		this.timestamp = timestamp;
		this.metaInformation = metaInformation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public Map<String, Object> getMetaInformation() {
		return metaInformation;
	}

	public void setMetaInformation(Map<String, Object> metaInformation) {
		this.metaInformation = metaInformation;
	}
}
