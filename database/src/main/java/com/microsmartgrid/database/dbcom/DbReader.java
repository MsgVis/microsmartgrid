package com.microsmartgrid.database.dbcom;

public class DbReader {
	public static Object getDeviceInfo(String topic) {
		try {
			connect();
		} catch (Exception ex) {
			// log and exit
		}
		// TODO: get additional device info object from topic name
		return null;
	}

	private static void connect() {
		// TODO: connect to database
	}
}
