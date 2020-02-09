package com.microsmartgrid.mqttclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
