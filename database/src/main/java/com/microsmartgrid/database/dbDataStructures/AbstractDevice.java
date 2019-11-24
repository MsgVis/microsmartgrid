package com.microsmartgrid.database.dbDataStructures;

import com.google.gson.JsonObject;

import java.time.LocalDateTime;

public abstract class AbstractDevice {

	private int id;
	private LocalDateTime timestamp;
	private JsonObject metaInformation;

	protected AbstractDevice() {
	}

	public AbstractDevice(LocalDateTime timestamp, JsonObject metaInformation) {
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

	public JsonObject getMetaInformation() {
		return metaInformation;
	}

	public void setMetaInformation(JsonObject metaInformation) {
		this.metaInformation = metaInformation;
	}
}
