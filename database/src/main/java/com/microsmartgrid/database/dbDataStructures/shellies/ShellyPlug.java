package com.microsmartgrid.database.dbDataStructures.shellies;

import com.microsmartgrid.database.dbDataStructures.AbstractDevice;

public class ShellyPlug extends AbstractDevice {

	//inherent vars
	private float power;
	private float temperature;
	private long energy;
	private boolean over_temperature;
	private boolean off;

	protected ShellyPlug() {
	}
}
