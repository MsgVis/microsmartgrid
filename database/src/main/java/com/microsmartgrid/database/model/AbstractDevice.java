package com.microsmartgrid.database.model;

import com.fasterxml.jackson.annotation.*;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
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
@Data
@IdClass(AbstractDeviceId.class)
public abstract class AbstractDevice implements Serializable {

	@Id
	@ManyToOne
	@JoinColumn(name = "device_id")
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@JsonProperty("Device_id")
	private DeviceInformation deviceInformation;

	@Id
	@Column(name = "time", nullable = false)
	@JsonAlias("timestamp")
	@JsonProperty("Timestamp")
	private Instant timestamp;

	// Not a typo, saves as json. For jsonb replace 'jsonb' in 'columnDefinition.
	@Type(type = "jsonb")
	@Column(name = "meta", columnDefinition = "json")
	@JsonAnySetter
	@JsonProperty("Meta_information")
	private Map<String, Object> metaInformation = new HashMap<>();

}
