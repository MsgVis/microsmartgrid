package com.microsmartgrid.database.service.DaiSmartGrid;

import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DaiSmartGrid.ReadingsRepository;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
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
}
