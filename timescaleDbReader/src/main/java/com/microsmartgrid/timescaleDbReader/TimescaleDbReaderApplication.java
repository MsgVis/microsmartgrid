package com.microsmartgrid.timescaleDbReader;

import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repositories.DeviceInformationRepository;
import com.microsmartgrid.database.services.DeviceInformationService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class TimescaleDbReaderApplication {

	private static final Logger logger = LogManager.getLogger(TimescaleDbReaderApplication.class);

	@Autowired
	private DeviceInformationRepository deviceInfoRepository;
	@Autowired
	private DeviceInformationService deviceInfoService;

	public static void main(String[] args) {
		SpringApplication.run(TimescaleDbReaderApplication.class, args);
	}

	/**
	 * Query devices table
	 *
	 * @param id Internally generated id of the device
	 * @return device with input id or null if none could be found
	 */
	@GetMapping("/device")
	public DeviceInformation queryDevices(int id) throws NotFoundException {
		return deviceInfoRepository.findById(id).orElseThrow(() -> new NotFoundException("The device with id " + id + " doesn't exist."));
	}

	/**
	 * Query for all devices
	 *
	 * @return A list of all registered devices
	 */
	@GetMapping("/deviceList")
	public List<DeviceInformation> queryDeviceList() {
		return deviceInfoRepository.findAll();
	}
}
