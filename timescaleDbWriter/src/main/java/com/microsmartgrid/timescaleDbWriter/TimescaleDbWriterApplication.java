package com.microsmartgrid.timescaleDbWriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.microsmartgrid.database"})
@EntityScan("com.microsmartgrid.database.model")
@EnableJpaRepositories(basePackages = {"com.microsmartgrid.database"})
public class TimescaleDbWriterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimescaleDbWriterApplication.class, args);
	}
}
