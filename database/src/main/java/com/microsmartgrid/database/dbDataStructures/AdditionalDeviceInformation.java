package com.microsmartgrid.database.dbDataStructures;

import java.util.ArrayList;

public class AdditionalDeviceInformation {

	public enum Type {POWERGRID, PRODUCER, CONSUMER, ELECTRICMETER};
	public enum Subtype {POWERGRID, ELECTRICMETER, LIGHT, CHARGINGSTATION, BATTERY};

	private int id;
	/*
	In the case of MQTT, this should be the topic
	 */
	private String name;
	private String description;
	private Integer[] children;
	private Type type;
	private Subtype subtype;

	protected AdditionalDeviceInformation() {
	}

	// minimal constructor
	public AdditionalDeviceInformation(String name) {
		this.name = name;
	}

	public int getId() {return id;}

	public void setId(int id) {this.id = id;}

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

	public Integer[] getChildren() {
		return children;
	}

	public void setChildren(Integer[] children) {
		this.children = children;
	}

	public Enum getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Enum getSubtype() {
		return subtype;
	}

	public void setSubtype(Subtype subtype) {
		this.subtype = subtype;
	}

}
