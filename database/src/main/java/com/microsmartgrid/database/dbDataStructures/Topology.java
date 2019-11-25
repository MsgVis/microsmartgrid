package com.microsmartgrid.database.dbDataStructures;

public class Topology {

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
	private AbstractDevice device;
	private AbstractDevice[] children;
	private Enum type;
	private Enum subtype;
	private int depth;

	protected Topology() {
	}

	// minimal constructor
	public Topology(String name, AbstractDevice device) {
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

	public AbstractDevice getDevice() {
		return device;
	}

	public void setDevice(AbstractDevice device) {
		this.device = device;
	}

	public AbstractDevice[] getChildren() {
		return children;
	}

	public void setChildren(AbstractDevice[] children) {
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
