package com.microsmartgrid.database.model.DaiSmartGrid;

import com.microsmartgrid.database.model.AbstractDevice;

public class Weather extends AbstractDevice {

	//inherent vars
	private String icon;
	private String summary;
	private float temperature;

	protected Weather() {
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public float getTemperature() {
		return this.temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
}
