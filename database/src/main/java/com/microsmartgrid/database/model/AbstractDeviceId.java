package com.microsmartgrid.database.model;

import java.io.Serializable;
import java.time.Instant;

class AbstractDeviceId implements Serializable {
	DeviceInformation deviceInformation;
	Instant time;
}
