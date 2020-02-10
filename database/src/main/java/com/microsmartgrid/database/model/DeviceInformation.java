package com.microsmartgrid.database.model;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Stores additional information to the device which is used for presentation in the 'view'
 */
@TypeDefs({
	@TypeDef(name = "int-array", typeClass = IntArrayType.class)
})
@Entity
@Table(name = "devices", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Data
@NoArgsConstructor
public class DeviceInformation implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	@SequenceGenerator(name = "id_sequence", sequenceName = "devices_id_seq", allocationSize = 1)
	private int id;
	/*
	In the case of MQTT, this should be the topic
	 */
	private String name;
	private String description;
	@org.hibernate.annotations.Type(type = "int-array")
	@Column(
		columnDefinition = "integer[]"
	)
	private int[] children;
	@Enumerated
	private Type type;
	@Enumerated
	private Subtype subtype;

	// minimal constructor
	public DeviceInformation(String name) {
		this.name = name;
	}

	public enum Type {POWERGRID, PRODUCER, CONSUMER, ELECTRICMETER}

	public enum Subtype {POWERGRID, ELECTRICMETER, LIGHT, CHARGINGSTATION, BATTERY}

}
