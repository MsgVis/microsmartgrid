package com.microsmartgrid.timescaleDbReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient
public class TimescaleDbReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimescaleDbReaderApplication.class, args);
	}

}
