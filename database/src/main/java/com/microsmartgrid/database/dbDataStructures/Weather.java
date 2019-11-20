package com.microsmartgrid.database.dbDataStructures;

import com.google.gson.Gson;

import java.time.LocalDateTime;

public class Weather implements AbstractDevice {
	//interface implementation
	private LocalDateTime timestamp;
	private String device_name;
	private Gson meta_information;

	//inherent vars
	private String icon, summary;
	private float temperature;

	public Weather () {}
}
