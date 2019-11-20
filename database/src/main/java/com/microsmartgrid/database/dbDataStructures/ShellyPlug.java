package com.microsmartgrid.database.dbDataStructures;

import com.google.gson.Gson;

import java.time.LocalDateTime;

public class ShellyPlug implements AbstractDevice{
	//inherent vars
	private float power, temperature;
	private long energy;
	private boolean over_temperature;
	private boolean off;

	//interface implementation
	private LocalDateTime timestamp;
	private String device_name;
	private Gson meta_information;

	public ShellyPlug(){}
}
