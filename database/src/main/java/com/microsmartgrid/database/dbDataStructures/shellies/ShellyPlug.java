package com.microsmartgrid.database.dbDataStructures.shellies;

import com.microsmartgrid.database.dbDataStructures.Device;

public class ShellyPlug extends Device {

	//inherent vars
	private float power;
	private float temperature;
	private long energy;
	private boolean over_temperature;
	private boolean off;

	protected ShellyPlug() {
	}
}
