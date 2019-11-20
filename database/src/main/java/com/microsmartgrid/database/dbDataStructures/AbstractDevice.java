package com.microsmartgrid.database.dbDataStructures;

import com.google.gson.Gson;
import java.time.LocalDateTime;

public interface AbstractDevice {
	LocalDateTime timestamp = null;
	String device_name = null;
	Gson meta_information = null;
}
