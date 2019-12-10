package com.microsmartgrid.database.dbDataStructures.DaiSmartGrid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Battery extends Readings {

	public Battery() {
		super();
	}

	public Battery(@JsonProperty("frequency_Grid") float frequency) {
		this();
		super.setFrequency_grid(frequency);
	}
}
