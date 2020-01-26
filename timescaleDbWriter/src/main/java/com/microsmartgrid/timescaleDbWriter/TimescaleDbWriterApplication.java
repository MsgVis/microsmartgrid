package com.microsmartgrid.timescaleDbWriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.HelperFunctions;
import com.microsmartgrid.database.dbCom.DatabaseConfig;
import com.microsmartgrid.database.model.AbstractDevice;
import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repositories.DaiSmartGrid.ReadingsRepository;
import com.microsmartgrid.database.repositories.DeviceInformationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.*;
import java.util.List;

import static com.microsmartgrid.database.dbCom.SqlCommands.INSERT_READINGS;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class TimescaleDbWriterApplication {
	private static final Logger logger = LogManager.getLogger(TimescaleDbWriterApplication.class);

	@Autowired
	private ReadingClient dbReader;
	@Autowired
	private DeviceInformationRepository deviceInfoRepository;
	@Autowired
	private ReadingsRepository readingsRepository;

	public static void main(String[] args) {
		SpringApplication.run(TimescaleDbWriterApplication.class, args);
	}

	/**
	 * Insert Readings object into database
	 *
	 * @param device
	 */
	public <T extends Readings> void insertReadings(T device) {
		try (Connection conn = new DatabaseConfig().getConnection();
			 PreparedStatement reading = conn.prepareStatement(INSERT_READINGS)) {
			// This will be refactored anyways
			ObjectMapper objM = new ObjectMapper();

			reading.setTimestamp(1, Timestamp.from(device.getTimestamp()));
			reading.setInt(2, device.getDeviceInformation().getId());
			// SQLType 1 = float
			int f = Types.FLOAT;
			reading.setObject(3, device.getActive_energy_A_minus(), f);
			reading.setObject(4, device.getActive_energy_A_plus(), f);
			reading.setObject(5, device.getReactive_energy_R_minus(), f);
			reading.setObject(6, device.getReactive_energy_R_plus(), f);
			reading.setObject(7, device.getActive_power_P_total(), f);
			reading.setObject(8, device.getActive_power_P1(), f);
			reading.setObject(9, device.getActive_power_P2(), f);
			reading.setObject(10, device.getActive_power_P3(), f);
			reading.setObject(11, device.getReactive_power_Q_total(), f);
			reading.setObject(12, device.getReactive_power_Q1(), f);
			reading.setObject(13, device.getReactive_power_Q2(), f);
			reading.setObject(14, device.getReactive_power_Q3(), f);
			reading.setObject(15, device.getApparent_power_S_total(), f);
			reading.setObject(16, device.getApparent_power_S1(), f);
			reading.setObject(17, device.getApparent_power_S2(), f);
			reading.setObject(18, device.getApparent_power_S3(), f);
			reading.setObject(19, device.getCurrent_I_avg(), f);
			reading.setObject(20, device.getCurrent_I1(), f);
			reading.setObject(21, device.getCurrent_I2(), f);
			reading.setObject(22, device.getCurrent_I3(), f);
			reading.setObject(23, device.getVoltage_U_avg(), f);
			reading.setObject(24, device.getVoltage_U1(), f);
			reading.setObject(25, device.getVoltage_U2(), f);
			reading.setObject(26, device.getVoltage_U3(), f);
			reading.setObject(27, device.getFrequency_grid(), f);
			reading.setString(28, objM.writeValueAsString(device.getMetaInformation()));

			reading.executeUpdate();

		} catch (SQLException e) {
			logger.warn("Couldn't commit reading connected to device with id " +
				(device.getDeviceInformation().getId() > 0 ? device.getDeviceInformation().getId() : "none (no device information set yet)") + " to database.");
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			logger.warn("Couldn't create json from meta information map.");
			e.printStackTrace();
		}
	}

	/**
	 * Insert AdditionalDeviceInformation object into database
	 *
	 * @param deviceInfo
	 */
	public DeviceInformation insertDeviceInfo(DeviceInformation deviceInfo) {
		return deviceInfoRepository.save(deviceInfo);
	}

	@PostMapping("/")
	public void writeDeviceToDatabase(@RequestParam("topic") String topic, @RequestParam("json") String json) throws IOException, IllegalArgumentException {
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
		DeviceInformation deviceInfo = deviceInfoRepository.findByName(topic).orElse(null);
		if (deviceInfo == null) {
			// create new DeviceInformation to the corresponding device and save topic to 'name'
			deviceInfo = new DeviceInformation(topic);
			deviceInfo.setId(insertDeviceInfo(deviceInfo).getId());
			logger.info("Created new device information object for name " + deviceInfo.getName() +
				" with the generated id " + deviceInfo.getId());
		}
		device.setDeviceInformation(deviceInfo);

		logger.info("Writing " + device.toString() + " to database.");
		if (device instanceof Readings) {
			// write entry to database
			insertReadings((Readings) device);
		} else {
			logger.warn("Database commands for the class " + device.getClass() + " haven't been implemented yet");
		}
	}

	@FeignClient("timescaleDbReader")
	interface ReadingClient {
		@RequestMapping(path = "/deviceList", method = RequestMethod.GET)
		List<DeviceInformation> queryDeviceList();

		@RequestMapping(path = "/deviceById", method = RequestMethod.GET)
		DeviceInformation queryDevices(@RequestParam("id") int id);

		@RequestMapping(path = "/deviceByName", method = RequestMethod.GET)
		DeviceInformation queryDevices(@RequestParam("name") String name);
	}
}
