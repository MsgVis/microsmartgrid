package com.microsmartgrid.database.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.Instant;

@Data
@EqualsAndHashCode
public class AbstractDeviceId implements Serializable {
	int deviceInformation;
	Instant timestamp;
}
