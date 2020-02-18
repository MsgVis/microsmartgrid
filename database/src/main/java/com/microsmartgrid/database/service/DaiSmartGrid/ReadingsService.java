package com.microsmartgrid.database.service.DaiSmartGrid;

import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DaiSmartGrid.ReadingsRepository;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ReadingsService {

	@Autowired
	private DeviceInformationRepository deviceInfoRepository;
	@Autowired
	private ReadingsRepository repository;

	public List<Readings> getLatestReadings(Optional<Period> cutoff) {
		List<DeviceInformation> devices = deviceInfoRepository.findAll();
		List<Readings> readings = new ArrayList<>();

		for (DeviceInformation id : devices) {
			repository.findFirstByDeviceInformationAndTimestampBeforeOrderByTimestampDesc(id, Instant.now().minus(cutoff.orElse(Period.ofDays(3))))
				.ifPresent(readings::add);
		}

		return readings;
	}

	public List<Readings> getAverageAggregate(Optional<Integer> id, Optional<Period> since, Optional<Period> until, String step) {
		HashMap<String, Object> queryInfo = new HashMap<>();
		queryInfo.put("aggregate", "avg");
		queryInfo.put("interval", step);

		List<Readings> readings = repository.findAllAvg(id.orElse(0),
			since.map(s -> Instant.now().minus(s)).orElse(Instant.EPOCH),
			until.map(u -> Instant.now().minus(u)).orElse(Instant.EPOCH),
			step);

		readings.forEach(r -> r.setMetaInformation(queryInfo));
		return readings;
	}

	public List<Readings> getStandardDeviationAggregate(Optional<Integer> id, Optional<Period> since, Optional<Period> until, String step) {
		HashMap<String, Object> queryInfo = new HashMap<>();
		queryInfo.put("aggregate", "std");
		queryInfo.put("interval", step);

		List<Readings> readings = repository.findAllStd(id.orElse(0),
			since.map(s -> Instant.now().minus(s)).orElse(Instant.EPOCH),
			until.map(u -> Instant.now().minus(u)).orElse(Instant.EPOCH),
			step);

		readings.forEach(r -> r.setMetaInformation(queryInfo));
		return readings;
	}

	public List<Readings> getMinAggregate(Optional<Integer> id, Optional<Period> since, Optional<Period> until, String step) {
		HashMap<String, Object> queryInfo = new HashMap<>();
		queryInfo.put("aggregate", "min");
		queryInfo.put("interval", step);

		List<Readings> readings = repository.findAllMin(id.orElse(0),
			since.map(s -> Instant.now().minus(s)).orElse(Instant.EPOCH),
			until.map(u -> Instant.now().minus(u)).orElse(Instant.EPOCH),
			step);

		readings.forEach(r -> r.setMetaInformation(queryInfo));
		return readings;
	}

	public List<Readings> getMaxAggregate(Optional<Integer> id, Optional<Period> since, Optional<Period> until, String step) {
		HashMap<String, Object> queryInfo = new HashMap<>();
		queryInfo.put("aggregate", "max");
		queryInfo.put("interval", step);

		List<Readings> readings = repository.findAllMax(id.orElse(0),
			since.map(s -> Instant.now().minus(s)).orElse(Instant.EPOCH),
			until.map(u -> Instant.now().minus(u)).orElse(Instant.EPOCH),
			step);

		readings.forEach(r -> r.setMetaInformation(queryInfo));
		return readings;
	}

	public List<Readings> getReadings(Optional<Integer> id, Optional<Period> since, Optional<Period> until) {
		return repository.findAll(id.orElse(0),
			since.map(s -> Instant.now().minus(s)).orElse(Instant.EPOCH),
			until.map(u -> Instant.now().minus(u)).orElse(Instant.EPOCH));
	}

}
