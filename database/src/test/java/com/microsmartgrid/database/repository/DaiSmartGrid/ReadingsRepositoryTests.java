package com.microsmartgrid.database.repository.DaiSmartGrid;

import com.microsmartgrid.database.AbstractIntegrationTest;
import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ReadingsRepositoryTests extends AbstractIntegrationTest {

	static Readings readings1 = new Readings();
	static Readings readings2 = new Readings();
	static DeviceInformation deviceInformation1 = new DeviceInformation("unique name");

	@Autowired
	private ReadingsRepository readingsRepository;
	@Autowired
	private DeviceInformationRepository deviceInformationRepository;

	@BeforeAll
	static void init() {
		readings1.setDeviceInformation(deviceInformation1);
		readings2.setDeviceInformation(deviceInformation1);

		readings1.setTimestamp(Instant.now().minus(Period.ofDays(1)));
		readings2.setTimestamp(Instant.now().minus(Period.ofDays(2)));
	}

	@Test
	void testFindSingleWithoutCutoff() {
		readingsRepository.save(readings1);
		readingsRepository.save(readings2);
		deviceInformationRepository.save(deviceInformation1);

		assertThat(readingsRepository.findFirstByDeviceInformationAndTimestampAfterOrderByTimestampDesc(deviceInformation1, Instant.now().minus(Period.ofDays(3))))
			.hasValue(readings1);
	}

	@Test
	void testFindSingleWithCutoff() {
		readingsRepository.save(readings1);
		readingsRepository.save(readings2);
		deviceInformationRepository.save(deviceInformation1);

		assertThat(readingsRepository.findFirstByDeviceInformationAndTimestampAfterOrderByTimestampDesc(deviceInformation1, Instant.now().minus(Period.ofDays(1))))
			.hasValue(readings1);
	}

	@Test
	void testFindNoneWithCutoff() {
		readingsRepository.save(readings1);
		readingsRepository.save(readings2);
		deviceInformationRepository.save(deviceInformation1);

		assertThat(readingsRepository.findFirstByDeviceInformationAndTimestampAfterOrderByTimestampDesc(deviceInformation1, Instant.now().minus(Period.ofDays(0))))
			.isEmpty();
	}

	@Test
	void testFindNoneWithWrongDeviceId() {
		readingsRepository.save(readings1);
		readingsRepository.save(readings2);
		DeviceInformation deviceInformation2 = new DeviceInformation("some test name");
		deviceInformationRepository.save(deviceInformation2);

		assertThat(readingsRepository.findFirstByDeviceInformationAndTimestampAfterOrderByTimestampDesc(deviceInformation2, Instant.now().minus(Period.ofDays(3))))
			.isEmpty();
	}

	@Test
	@Disabled("Can't test without timescaledb")
	void testFindReadings() {

	}
}
