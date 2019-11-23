package com.microsmartgrid.database.dbWriter;

public interface dbWriterAgent {
	/*
	The dbWriterAgent:
		- takes raw data from an implementation of the ProtocolHandlerAgent interface (e.g. raw json-mqtt-data)
		- instantiates dbDataStructure over raw json data
		- writes dbDataStructure-object to database (over jiac node or directly (?))
	 */
}
