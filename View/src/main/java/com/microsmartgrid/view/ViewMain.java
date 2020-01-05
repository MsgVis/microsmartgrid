package com.microsmartgrid.view;

import com.microsmartgrid.configserver.ConfigServerApplication;
import com.microsmartgrid.eurekaserver.EurekaServerApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ViewMain {
	public static void main(String[] args) {

		SpringApplication.run(ConfigServerApplication.class, args);
		SpringApplication.run(EurekaServerApplication.class, args);
		SpringApplication.run(ViewMain.class, args);
	}
}
