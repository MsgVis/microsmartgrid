package com.microsmartgrid.database.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsmartgrid.database.HelperFunctions;
import com.microsmartgrid.database.model.AbstractDevice;
import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DaiSmartGrid.ReadingsRepository;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Log4j2
public class AbstractDeviceService {

	@Autowired
	private DeviceInformationRepository deviceInfoRepository;
	@Autowired
	private ReadingsRepository readingsRepository;

	@SuppressWarnings("unchecked")
	public <T extends AbstractDevice> T insertDevice(String name, String json) throws IOException, NotFoundException {
		// Get class mapping
		Class<? extends AbstractDevice> cls = HelperFunctions.getClassFromIdentifier(name);
		AbstractDevice device;
		try {
			// create object
			device = HelperFunctions.deserializeJson(json, cls);
			if (device == null) return null;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		AtomicReference<DeviceInformation> deviceInfo = new AtomicReference<>();
		// check for existing deviceInformation
		deviceInfoRepository.findByName(name).ifPresentOrElse(deviceInfo::set,
			() -> {
				// create new DeviceInformation to the corresponding device and save topic to 'name'
				deviceInfo.set(deviceInfoRepository.save(new DeviceInformation(name)));
				log.info("Created new device information object for name " + deviceInfo.get().getName() +
					" with the generated id " + deviceInfo.get().getId());
			});
		device.setDeviceInformation(deviceInfo.get());

		if (device instanceof Readings) {
			// write entry to database
			return (T) readingsRepository.save((Readings) device);
		} else {
			throw new NotFoundException("The database table for the class " + device.getClass() + " hasn't been implemented yet.");
		}
	}
}
