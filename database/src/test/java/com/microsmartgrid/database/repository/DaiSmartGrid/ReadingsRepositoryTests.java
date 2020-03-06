package com.microsmartgrid.database.repository.DaiSmartGrid;

import com.microsmartgrid.database.AbstractIntegrationTest;
import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

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
	DeviceInformationRepository deviceInformationRepository;

	void saveBasicDevices() {
		deviceInformation1 = new DeviceInformation("unique name");
		deviceInformation1 = deviceInformationRepository.save(deviceInformation1);

		readings1 = new Readings();
		readings1.setDeviceInformation(deviceInformation1);
		readings1.setTimestamp(transactionTime.minus(Duration.ofDays(1)));
		readings1 = readingsRepository.save(readings1);

		readings2 = new Readings();
		readings2.setDeviceInformation(deviceInformation1);
		readings2.setTimestamp(transactionTime.minus(Duration.ofDays(2)));
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
	@Disabled("Can't test without timescaledb")
	void testFindReadings() {

	}
}
