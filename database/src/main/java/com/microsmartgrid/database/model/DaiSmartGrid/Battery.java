package com.microsmartgrid.database.model.DaiSmartGrid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Battery extends Readings {

	public Battery() {
		super();
	}

	public Battery(@JsonProperty("frequency_Grid") Float frequency) {
		this();
		super.setFrequency_grid(frequency);
	}
}
