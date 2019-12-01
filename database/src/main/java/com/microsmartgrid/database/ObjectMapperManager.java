package com.microsmartgrid.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.*;

public final class ObjectMapperManager {

	private static ObjectMapper objMapper = configureMapper(new ObjectMapper());

	public static ObjectMapper getObjectMapper() {
		return objMapper;
	}

	private static ObjectMapper configureMapper(ObjectMapper objMapper){
		objMapper
			.registerModule(new JavaTimeModule())

		// MixIns
			.addMixIn(Battery.class, Battery.BatteryMixIn.class)
			.addMixIn(Ladestation.class, Ladestation.LadestationMixIn.class)
			.addMixIn(Main.class, Main.MainMixIn.class)
			.addMixIn(PV.class, PV.PVMixIn.class)
			.addMixIn(Weather.class, Weather.WeatherMixIn.class);

		return objMapper;
	}
}
