package com.microsmartgrid.database.dbDataStructures.DaiSmartGrid;

import com.microsmartgrid.database.dbDataStructures.AbstractDevice;

import java.time.LocalDateTime;

public class Weather extends AbstractDevice {

	//inherent vars
	private String icon;
	private String summary;
	private float temperature;

	protected Weather () {}

	public class WeatherMixIn {

	}
}
