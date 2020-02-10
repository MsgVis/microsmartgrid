package com.microsmartgrid.database.service.DaiSmartGrid;

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
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Log4j2
public class ReadingsService {

	@Autowired
	private DeviceInformationRepository deviceInfoRepository;
	@Autowired
	private ReadingsRepository repository;

	public List<Readings> getLatestReadings(Period cutoff) {
		List<DeviceInformation> devices = deviceInfoRepository.findAll();
		List<Readings> readings = new ArrayList<>();

		if (cutoff == null) {
			cutoff = Period.ofDays(3);
		}

		for (DeviceInformation id : devices) {
			repository.findFirstByDeviceInformationAndTimeBeforeOrderByTimeDesc(id, Instant.now().minus(cutoff)).ifPresent(readings::add);
		}

		return readings;
	}

	public List<Readings> getAverageAggregate(int id, Period since, Period until, String step) {
		HashMap<String, Object> queryInfo = new HashMap<>();
		queryInfo.put("aggregate", "avg");
		queryInfo.put("interval", step);

		List<Readings> readings = repository.findAllAvg(id, Instant.now().minus(since), Instant.now().minus(until), step);
		readings.forEach(r -> {
			r.setMetaInformation(queryInfo);
		});
		return readings;
	}

	public List<Readings> getStandardDeviationAggregate(int id, Period since, Period until, String step) {
		HashMap<String, Object> queryInfo = new HashMap<>();
		queryInfo.put("aggregate", "std");
		queryInfo.put("interval", step);

		List<Readings> readings = repository.findAllStd(id, Instant.now().minus(since), Instant.now().minus(until), step);
		readings.forEach(r -> {
			r.setMetaInformation(queryInfo);
		});
		return readings;
	}

	public List<Readings> getMinAggregate(int id, Period since, Period until, String step) {
		HashMap<String, Object> queryInfo = new HashMap<>();
		queryInfo.put("aggregate", "min");
		queryInfo.put("interval", step);

		List<Readings> readings = repository.findAllMin(id, Instant.now().minus(since), Instant.now().minus(until), step);
		readings.forEach(r -> {
			r.setMetaInformation(queryInfo);
		});
		return readings;
	}

	public List<Readings> getMaxAggregate(int id, Period since, Period until, String step) {
		HashMap<String, Object> queryInfo = new HashMap<>();
		queryInfo.put("aggregate", "max");
		queryInfo.put("interval", step);

		List<Readings> readings = repository.findAllMax(id, Instant.now().minus(since), Instant.now().minus(until), step);
		readings.forEach(r -> {
			r.setMetaInformation(queryInfo);
		});
		return readings;
	}

	public List<Readings> getReadings(int id, Period since, Period until) {
		return repository.findAll(id, Instant.now().minus(since), Instant.now().minus(until));
	}

	public Readings insertReading(String topic, String json) throws IOException, NotFoundException {
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

			// flush since we would like to get the generated id
			deviceInfo = deviceInfoRepository.saveAndFlush(new DeviceInformation(topic));
			logger.info("Created new device information object for name " + deviceInfo.getName() +
				" with the generated id " + deviceInfo.getId());
		}
		device.setDeviceInformation(deviceInfo);

		if (device instanceof Readings) {
			// write entry to database
			return repository.save((Readings) device);
		} else {
			throw new NotFoundException("The database table for the class " + device.getClass() + " hasn't been implemented yet.");
		}
	}
}
