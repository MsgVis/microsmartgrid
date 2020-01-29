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

import java.sql.SQLException;
import java.util.List;

import static com.microsmartgrid.database.dbCom.DbHandle.execute;

@SpringBootTest
@EnableDiscoveryClient
@EnableFeignClients
public class DbHandleTest {

	@Autowired
	ReadingClient dbReader;

	@AfterEach
	public void cleanUp() throws SQLException {
		execute("DROP ALL OBJECTS;");
	}

	@FeignClient("timescaleDbWriter")
	interface ReadingClient {
		@RequestMapping(path = "/deviceList", method = RequestMethod.GET)
		public List<DeviceInformation> queryDeviceList();

		@RequestMapping(path = "/deviceById", method = RequestMethod.GET)
		public DeviceInformation queryDevices(@RequestParam("id") int id);
	}
}

