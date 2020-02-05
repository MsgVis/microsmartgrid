package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Battery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static com.microsmartgrid.database.dbCom.DbHandle.execute;
import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_DEVICE_TABLE;
import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_READINGS_TABLE;
import static com.microsmartgrid.timescaleDbWriter.TimescaleDbWriterApplication.insertDeviceInfo;
import static com.microsmartgrid.timescaleDbWriter.TimescaleDbWriterApplication.insertReadings;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootApplication
@SpringBootTest
@EnableDiscoveryClient
@EnableFeignClients
public class DbHandleTest {

	@Autowired
	ReadingClient dbReader;

	@FeignClient("timescaleDbReader")
	interface ReadingClient {
		@RequestMapping(path = "/deviceList", method = RequestMethod.GET)
		public List<AdditionalDeviceInformation> queryDeviceList();
		@RequestMapping(path = "/deviceById", method = RequestMethod.GET)
		public AdditionalDeviceInformation queryDevices(@RequestParam("id") int id);
		@RequestMapping(path = "/deviceByName", method = RequestMethod.GET)
		public AdditionalDeviceInformation queryDevices(@RequestParam("name") String name);
	}

	@AfterEach
	public void cleanUp() throws SQLException {
		execute("DROP ALL OBJECTS;");
	}

	@Test
	@Order(1)
	public void testInsertReadings() throws SQLException {
		// TODO hier wird gar nichts getested? Fehlermeldungen
		execute(CREATE_READINGS_TABLE);

		Battery bat = new Battery((float) 0.0);
		bat.setTimestamp(Instant.now());
		insertReadings(bat);
	}

	@Test
	@Order(2)
	public void testInsertDevicesInfo() throws SQLException {
		execute(CREATE_DEVICE_TABLE);

		AdditionalDeviceInformation info = new AdditionalDeviceInformation("topic");
		info.setType(AdditionalDeviceInformation.Type.CONSUMER);
		info.setSubtype(AdditionalDeviceInformation.Subtype.BATTERY);
		info.setChildren(new Integer[]{1, 2});
		insertDeviceInfo(info);

		AdditionalDeviceInformation queried_info = dbReader.queryDevices("topic");
		assertEquals("topic", queried_info.getName());
	}
}

