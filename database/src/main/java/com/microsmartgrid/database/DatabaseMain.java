package com.microsmartgrid.database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DatabaseMain {
	public static void main(String[] args) {
		SpringApplication.run(DatabaseMain.class, args);
	}
}
