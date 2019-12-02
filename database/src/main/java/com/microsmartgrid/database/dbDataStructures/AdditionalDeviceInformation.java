package com.microsmartgrid.database.dbDataStructures;

public class AdditionalDeviceInformation {

	private String icon;
	/*
	In the case of MQTT, this should be the topic
	 */
	private String name;
	private String description;
	/*
	Since Timescale doesn't support references on it's hypertables,
	we might need to switch to simple ids instead of objects
	@Id
	 */
	private Device device;
	private Device[] children;
	private Enum type;
	private Enum subtype;
	private int depth;

	protected AdditionalDeviceInformation() {
	}

	// minimal constructor
	public AdditionalDeviceInformation(String name, Device device) {
		this.name = name;
		this.device = device;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Device[] getChildren() {
		return children;
	}

	public void setChildren(Device[] children) {
		this.children = children;
	}

	public Enum getType() {
		return type;
	}

	public void setType(Enum type) {
		this.type = type;
	}

	public Enum getSubtype() {
		return subtype;
	}

	public void setSubtype(Enum subtype) {
		this.subtype = subtype;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

}
