package com.microsmartgrid.database.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


/**
 * Minimal implementation for application to work. Stores the connected DeviceInformation,
 * a timestamp, and a json with all unrecognized input fields.
 */
@Entity(name = "readings")
public abstract class AbstractDevice implements Serializable {

	@Id
	@Column(name = "device_id")
	@ManyToOne
	private DeviceInformation deviceInformation;

	@Column(name = "time", nullable = false)
	private Instant timestamp;

	@Column(name = "meta")
	@ElementCollection
	@JsonAnySetter
	private Map<String, Object> metaInformation = new HashMap<>();

	protected AbstractDevice() {
	}

	public AbstractDevice(Instant timestamp, Map<String, Object> metaInformation) {
		this.timestamp = timestamp;
		this.metaInformation = metaInformation;
	}

	public DeviceInformation getDeviceInformation() {
		return deviceInformation;
	}

	public void setDeviceInformation(DeviceInformation deviceInformation) {
		this.deviceInformation = deviceInformation;
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
