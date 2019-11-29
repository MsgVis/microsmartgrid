package com.microsmartgrid.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.*;

public final class ObjectMapperManager {

	private static ObjectMapper objMapper = configureMapper(new ObjectMapper());

	public static ObjectMapper getObjectMapper() {
		return objMapper;
	}

	private static ObjectMapper configureMapper(ObjectMapper objMapper){

		// MixIns
		objMapper.addMixIn(Battery.class, Battery.BatteryMixIn.class);
		objMapper.addMixIn(Ladestation.class, Ladestation.LadestationMixIn.class);
		objMapper.addMixIn(Main.class, Main.MainMixIn.class);
		objMapper.addMixIn(PV.class, PV.PVMixIn.class);
		objMapper.addMixIn(Weather.class, Weather.WeatherMixIn.class);

		return objMapper;
	}
}
