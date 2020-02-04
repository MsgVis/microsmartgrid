package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.model.DeviceInformation;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@SpringBootTest
@EnableDiscoveryClient
@EnableFeignClients
public class DbHandleTest {

	@Autowired
	ReadingClient dbReader;

	@AfterEach
	public void cleanUp() {
	}

	@FeignClient("timescaleDbWriter")
	interface ReadingClient {
		@RequestMapping(path = "/deviceList", method = RequestMethod.GET)
		List<DeviceInformation> queryDeviceList();

		@RequestMapping(path = "/deviceById", method = RequestMethod.GET)
		DeviceInformation queryDevices(@RequestParam("id") int id);
	}
}

