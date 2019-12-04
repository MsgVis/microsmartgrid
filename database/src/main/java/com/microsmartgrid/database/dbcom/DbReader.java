package com.microsmartgrid.database.dbcom;

import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;

public class DbReader {
	public static AdditionalDeviceInformation getDeviceInfo(String topic) {
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
