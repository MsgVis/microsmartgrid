package com.microsmartgrid.timescaleDbReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.microsmartgrid.database", "com.microsmartgrid.timescaleDbReader"})
@EntityScan("com.microsmartgrid.database.model")
@EnableJpaRepositories(basePackages = {"com.microsmartgrid.database"})
@EnableDiscoveryClient
public class TimescaleDbReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimescaleDbReaderApplication.class, args);
	}
}
