package com.microsmartgrid.database.model.shellies;

import com.microsmartgrid.database.model.AbstractDevice;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class shellyplug extends AbstractDevice {

	//inherent vars
	private Float power;
	private Float temperature;
	private long energy;
	private boolean over_temperature;
	private boolean off;
}
