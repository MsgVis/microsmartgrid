package com.microsmartgrid.database.dbDataStructures.shellies;

import com.google.gson.Gson;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;

import java.time.LocalDateTime;

public class ShellyPlug extends AbstractDevice {

	//inherent vars
	private float power;
	private float temperature;
	private long energy;
	private boolean over_temperature;
	private boolean off;

	protected ShellyPlug(){}
}