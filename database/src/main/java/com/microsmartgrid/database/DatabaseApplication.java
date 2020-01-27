package com.microsmartgrid.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsmartgrid.database.dbCom.DbWriter;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Readings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.microsmartgrid.database.dbCom.DbReader.queryDevices;
import static com.microsmartgrid.database.dbCom.DbWriter.insertDeviceInfo;
import static com.microsmartgrid.database.dbCom.DbWriter.insertReadings;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class DatabaseApplication {
	private static final Logger logger = LogManager.getLogger(DbWriter.class);

	public static void main(String[] args) {
		SpringApplication.run(DatabaseApplication.class, args);
	}

	@PostMapping("/")
	public static void writeDeviceToDatabase(@RequestParam("topic") String topic, @RequestParam("json") String json) throws IOException, IllegalArgumentException {
		// Get class mapping
		Class<? extends AbstractDevice> cls = HelperFunctions.getClassFromIdentifier(topic);
		AbstractDevice device;
		try {
			// create object
			device = HelperFunctions.deserializeJson(json, topic, cls);
		} catch (JsonProcessingException e) {
			logger.error("Couldn't construct instance from topic " + topic);
			throw new RuntimeException(e);
		}

		// check for existing deviceInformation
		AdditionalDeviceInformation deviceInfo = queryDevices(topic);
		if (deviceInfo == null) {
			// create new DeviceInformation to the corresponding device and save topic to 'name'
			deviceInfo = new AdditionalDeviceInformation(topic);
			deviceInfo.setId(insertDeviceInfo(deviceInfo));
			logger.info("Created new device information object for name " + deviceInfo.getName() +
				" with the generated id " + deviceInfo.getId());
		}
		device.setId(deviceInfo.getId());

		logger.info("Writing " + device.toString() + " to database.");
		if (device instanceof Readings) {
			// write entry to database
			insertReadings((Readings) device);
		} else {
			logger.warn("Database commands for the class " + device.getClass() + " haven't been implemented yet");
		}
	}

}
