package com.microsmartgrid.view;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ViewMain {
	public static void main(String[] args) {
		SpringApplication.run(ViewMain.class, args);
	}
}
