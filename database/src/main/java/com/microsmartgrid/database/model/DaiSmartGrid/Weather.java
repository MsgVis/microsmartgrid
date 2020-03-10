package com.microsmartgrid.database.model.DaiSmartGrid;

import com.microsmartgrid.database.model.AbstractDevice;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Weather extends AbstractDevice {

	//inherent vars
	private String icon;
	private String summary;
	private float temperature;
}
