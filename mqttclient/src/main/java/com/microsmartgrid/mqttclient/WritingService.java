package com.microsmartgrid.mqttclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Hack in order to get WritingClient Autowired
// Possible cause: Trying to Autowire in a callback function from a class that is not managed by Spring
@Service
public class WritingService {

	static WritingClient databaseWriter;

	public static WritingClient getDataBaseWriter() {
		return databaseWriter;
	}

	@Autowired
	public void setDatabaseWriter(WritingClient dbWriter) {
		databaseWriter = dbWriter;
	}
}
