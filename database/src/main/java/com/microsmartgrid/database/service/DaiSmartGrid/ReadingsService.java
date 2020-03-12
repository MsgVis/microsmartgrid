package com.microsmartgrid.database.service.DaiSmartGrid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.ObjectMapperManager;
import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DaiSmartGrid.ReadingsRepository;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Log4j2
public class ReadingsService {

	@Autowired
	private DeviceInformationRepository deviceInfoRepository;
	@Autowired
	private ReadingsRepository repository;

	public List<Readings> getLatestReadings(Optional<Duration> cutoff) {
		List<DeviceInformation> devices = deviceInfoRepository.findAll();
		List<Readings> readings = new ArrayList<>();

		for (DeviceInformation id : devices) {
			repository.findFirstByDeviceInformationAndTimestampAfterOrderByTimestampDesc(id, Instant.now().minus(cutoff.orElse(Duration.ofDays(3))))
				.ifPresent(readings::add);
		}

		return readings;
	}

	public List<Readings> getAverageAggregate(Optional<Integer> id, Optional<String> since, Optional<String> until, Duration step) {

		List<Map<String, Object>> readings = repository.findReadingsAvg(id.orElse(0),
			since.map(this::determineTimestamp).orElse(0L),
			until.map(this::determineTimestamp).orElse(0L),
			step.toString());

		return attachQueryInfo(readings, "max", step);
	}

	public List<Readings> getStandardDeviationAggregate(Optional<Integer> id, Optional<String> since, Optional<String> until, Duration step) {
		List<Map<String, Object>> readings = repository.findReadingsStd(id.orElse(0),
			since.map(this::determineTimestamp).orElse(0L),
			until.map(this::determineTimestamp).orElse(0L),
			step.toString());

		return attachQueryInfo(readings, "max", step);
	}

	public List<Readings> getMinAggregate(Optional<Integer> id, Optional<String> since, Optional<String> until, Duration step) {

		List<Map<String, Object>> readings = repository.findReadingsMin(id.orElse(0),
			since.map(this::determineTimestamp).orElse(0L),
			until.map(this::determineTimestamp).orElse(0L),
			step.toString());

		return attachQueryInfo(readings, "max", step);
	}

	public List<Readings> getMaxAggregate(Optional<Integer> id, Optional<String> since, Optional<String> until, Duration step) {

		List<Map<String, Object>> readings = repository.findReadingsMax(id.orElse(0),
			since.map(this::determineTimestamp).orElse(0L),
			until.map(this::determineTimestamp).orElse(0L),
			step.toString());

		return attachQueryInfo(readings, "max", step);
	}

	public List<Readings> getReadings(Optional<Integer> id, Optional<String> since, Optional<String> until) {
		return repository.findReadings(id.orElse(0),
			since.map(this::determineTimestamp).orElse(0L),
			until.map(this::determineTimestamp).orElse(0L));
	}

	private List<Readings> attachQueryInfo(List<Map<String, Object>> readings, String agg, Duration step) {
		HashMap<String, Object> queryInfo = new HashMap<>();
		queryInfo.put("aggregate", agg);
		queryInfo.put("interval", step.toString());

		List<Readings> convertedReadings = new ArrayList<>();

		ObjectMapper objM = ObjectMapperManager.getMapper();
		// TODO: improve performance on this fix
		readings.forEach(r -> {
			Map<String, Object> map = new HashMap<>();
			r.forEach((key, value) -> map.put(key.replaceFirst("agg_", ""), value));
			map.put("meta", queryInfo);

			convertedReadings.add(objM.convertValue(map, Readings.class));
		});

		return convertedReadings;
	}

	private long determineTimestamp(String s) {
		Instant time;
		try {
			time = Instant.parse(s);
		} catch (DateTimeParseException e) {
			time = Instant.now().minus(Duration.parse(s));
		}
		return time.getEpochSecond();
	}

}
