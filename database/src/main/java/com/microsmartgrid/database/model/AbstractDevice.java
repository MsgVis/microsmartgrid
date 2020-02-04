package com.microsmartgrid.database.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


/**
 * Minimal implementation for application to work. Stores the connected DeviceInformation,
 * a timestamp, and a json with all unrecognized input fields.
 */
@TypeDefs({
	@TypeDef(name = "json", typeClass = JsonStringType.class),
	@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@MappedSuperclass
@IdClass(AbstractDeviceId.class)
public abstract class AbstractDevice implements Serializable {

	@Id
	@ManyToOne
	private DeviceInformation deviceInformation;

	@Id
	@Column(name = "time", nullable = false)
	private Instant timestamp;

	// Not a typo, saves as json. For jsonb replace 'jsonb' in 'columnDefinition.
	@Type(type = "jsonb")
	@Column(name = "meta", columnDefinition = "json")
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
