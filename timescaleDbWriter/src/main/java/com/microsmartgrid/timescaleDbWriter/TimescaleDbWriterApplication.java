package com.microsmartgrid.timescaleDbWriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsmartgrid.database.HelperFunctions;
import com.microsmartgrid.database.model.AbstractDevice;
import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DaiSmartGrid.ReadingsRepository;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"com.microsmartgrid.database"})
@EntityScan("com.microsmartgrid.database.model")
@EnableJpaRepositories(basePackages = {"com.microsmartgrid.database"})
@EnableDiscoveryClient
public class TimescaleDbWriterApplication {
	private static final Logger logger = LogManager.getLogger(TimescaleDbWriterApplication.class);

	@Autowired
	private DeviceInformationRepository deviceInfoRepository;
	@Autowired
	private ReadingsRepository readingsRepository;

	public static void main(String[] args) {
		SpringApplication.run(TimescaleDbWriterApplication.class, args);
	}

	@PutMapping("/reading")
	public <T extends Readings> T saveReading(T device) {
		return readingsRepository.save(device);
	}

	@PutMapping("/device")
	public DeviceInformation saveDeviceInfo(DeviceInformation deviceInfo) {
		// flush since we would like to get the generated id
		return deviceInfoRepository.saveAndFlush(deviceInfo);
	}

	@PostMapping("/")
	public void writeReadingToDatabase(@RequestParam("topic") String topic, @RequestParam("json") String json) throws IOException, IllegalArgumentException {
		// Get class mapping
		Class<? extends AbstractDevice> cls = HelperFunctions.getClassFromIdentifier(topic);
		AbstractDevice device;
		try {
			// create object
			device = HelperFunctions.deserializeJson(json, cls);
		} catch (JsonProcessingException e) {
			logger.error("Couldn't construct instance from topic " + topic);
			throw new RuntimeException(e);
		}

		// check for existing deviceInformation
		DeviceInformation deviceInfo = deviceInfoRepository.findByName(topic).orElse(null);
		if (deviceInfo == null) {
			// create new DeviceInformation to the corresponding device and save topic to 'name'
			deviceInfo = saveDeviceInfo(new DeviceInformation(topic));
			logger.info("Created new device information object for name " + deviceInfo.getName() +
				" with the generated id " + deviceInfo.getId());
		}
		device.setDeviceInformation(deviceInfo);

		logger.info("Writing " + device.toString() + " to database.");
		if (device instanceof Readings) {
			// write entry to database
			saveReading((Readings) device);
		} else {
			logger.warn("The database table for the class " + device.getClass() + " hasn't been implemented yet.");
		}
	}
}
