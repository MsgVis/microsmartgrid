package com.microsmartgrid.database.repository.DaiSmartGrid;

import com.microsmartgrid.database.AbstractIntegrationTest;
import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import com.microsmartgrid.database.service.DaiSmartGrid.ReadingsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ReadingsRepositoryTests extends AbstractIntegrationTest {

	static final Instant transactionTime = Instant.now();

	Readings readings1;
	Readings readings2;
	DeviceInformation deviceInformation1;

	@Autowired
	ReadingsRepository readingsRepository;
	@Autowired
	ReadingsService readingsService;
	@Autowired
	DeviceInformationRepository deviceInformationRepository;

	void saveBasicDevices() {
		deviceInformation1 = new DeviceInformation("unique name");
		deviceInformation1 = deviceInformationRepository.save(deviceInformation1);

		readings1 = new Readings();
		readings1.setDeviceInformation(deviceInformation1);
		readings1.setTimestamp(transactionTime.minus(Duration.ofDays(1)));
		readings1.setF(59f);
		readings1 = readingsRepository.save(readings1);

		readings2 = new Readings();
		readings2.setDeviceInformation(deviceInformation1);
		readings2.setTimestamp(transactionTime.minus(Duration.ofDays(2)));
		readings2.setF(60f);
		readings2 = readingsRepository.save(readings2);
	}

	@Test
	void testFindSingleWithoutCutoff() {
		saveBasicDevices();
		assertThat(readingsRepository.findFirstByDeviceInformationAndTimestampAfterOrderByTimestampDesc(deviceInformation1,
			// default cutoff value
			transactionTime.minus(Duration.ofDays(3))))
			.hasValue(readings1);
	}

	@Test
	void testFindSingleWithCutoff() {
		saveBasicDevices();
		assertThat(readingsRepository.findFirstByDeviceInformationAndTimestampAfterOrderByTimestampDesc(deviceInformation1,
			transactionTime.minus(Duration.ofDays(1).plusSeconds(1))))
			.hasValue(readings1);
	}

	@Test
	void testFindNoneWithCutoff() {
		saveBasicDevices();
		assertThat(readingsRepository.findFirstByDeviceInformationAndTimestampAfterOrderByTimestampDesc(deviceInformation1,
			transactionTime.minus(Duration.ofDays(0))))
			.isEmpty();
	}

	@Test
	void testFindNoneWithWrongDeviceId() {
		saveBasicDevices();
		DeviceInformation deviceInformation2 = new DeviceInformation("some test name");
		deviceInformation2 = deviceInformationRepository.save(deviceInformation2);

		assertThat(readingsRepository.findFirstByDeviceInformationAndTimestampAfterOrderByTimestampDesc(deviceInformation2,
			transactionTime.minus(Duration.ofDays(3))))
			.isEmpty();
	}

	@Test
	void testFindAllReadingsWithNoneOptional() {
		saveBasicDevices();
		assertThat(readingsRepository.findReadings(0, 0, 0))
			.containsExactly(readings1, readings2);
	}

	@Test
	void testFindReadingsFiltered() {
		saveBasicDevices();
		assertThat(readingsRepository.findReadings(deviceInformation1.getId(),
			transactionTime.minus(Duration.ofDays(3)).getEpochSecond(),
			transactionTime.minus(Duration.ofDays(2).minusSeconds(1)).getEpochSecond()))
			.containsExactly(readings2);
	}

	@Test
	void testFindReadingsMax() {
		saveBasicDevices();
		assertThat(readingsRepository.findReadingsMax(deviceInformation1.getId(), 0, 0, "2 days"))
			.hasSize(1)
			.first().returns(60f, stringObjectMap -> Float.parseFloat(stringObjectMap.get("agg_f").toString()));
		assertThat(readingsService.getMaxAggregate(Optional.of(deviceInformation1.getId()),
			Optional.empty(), Optional.empty(), Duration.parse("P2D")))
			.hasSize(1)
			.first().hasFieldOrPropertyWithValue("f", 60f);
	}

	@Test
	void testFindReadingsMin() {
		saveBasicDevices();
		assertThat(readingsRepository.findReadingsMin(deviceInformation1.getId(), 0, 0, "P2D"))
			.hasSize(1)
			.first().returns(59f, stringObjectMap -> Float.parseFloat(stringObjectMap.get("agg_f").toString()));
		assertThat(readingsService.getMinAggregate(Optional.of(deviceInformation1.getId()),
			Optional.of("P31D"), Optional.of(transactionTime.toString()), Duration.parse("P2D")))
			.hasSize(1)
			.first().hasFieldOrPropertyWithValue("f", 59f);
	}

	@Test
	void testFindReadingsAvg() {
		saveBasicDevices();
		assertThat(readingsRepository.findReadingsAvg(deviceInformation1.getId(), 0, 0, "P2D"))
			.hasSize(1)
			.first().returns(59.5f, stringObjectMap -> Float.parseFloat(stringObjectMap.get("agg_f").toString()));
		assertThat(readingsService.getAverageAggregate(Optional.of(deviceInformation1.getId()),
			Optional.empty(), Optional.empty(), Duration.parse("P2D")))
			.hasSize(1)
			.first().hasFieldOrPropertyWithValue("f", 59.5f);
	}

	@Test
	void testFindReadingsStdDev() {
		saveBasicDevices();
		assertThat(readingsRepository.findReadingsStd(deviceInformation1.getId(), 0, 0, "P2D"))
			.hasSize(1)
			.first().returns(0.5f, reading -> Float.parseFloat(reading.get("agg_f").toString()));
		assertThat(readingsService.getStandardDeviationAggregate(Optional.of(deviceInformation1.getId()),
			Optional.empty(), Optional.empty(), Duration.parse("P2D")))
			.hasSize(1)
			.first().hasFieldOrPropertyWithValue("f", 0.5f);
	}

}
