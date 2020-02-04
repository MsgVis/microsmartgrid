package com.microsmartgrid.database.model;

import javax.persistence.*;
import java.util.List;


/**
 * Stores additional information to the device which is used for presentation in the 'view'
 */
@Entity(name = "devices")
@Table(name = "devices")
public class DeviceInformation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	/*
	In the case of MQTT, this should be the topic
	 */
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "children")
	@ElementCollection
	private List<Integer> children;
	@Column(name = "type")
	@Enumerated
	private Type type;
	@Column(name = "subtype")
	@Enumerated
	private Subtype subtype;

	protected DeviceInformation() {
	}

	// minimal constructor
	public DeviceInformation(String name) {
		this.name = name;
	}

	public DeviceInformation(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Integer> getChildren() {
		return children;
	}

	public void setChildren(List<Integer> children) {
		this.children = children;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Subtype getSubtype() {
		return subtype;
	}

	public void setSubtype(Subtype subtype) {
		this.subtype = subtype;
	}

	public enum Type {POWERGRID, PRODUCER, CONSUMER, ELECTRICMETER}

	public enum Subtype {POWERGRID, ELECTRICMETER, LIGHT, CHARGINGSTATION, BATTERY}

}
