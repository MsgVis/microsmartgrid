package com.microsmartgrid.database;

import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles(profiles = "postgres")
public class AbstractIntegrationTest {

	@Container
	static final GenericContainer timescale = new FixedHostPortGenericContainer<>("timescale/timescaledb:1.6.0-pg11")
		// TODO: switch to random port by using standard GenericContainer and assigning the port in the config
		.withFixedExposedPort(6543, 5432)
		.withEnv("POSTGRES_PASSWORD", "postgres")
		.waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));
}
