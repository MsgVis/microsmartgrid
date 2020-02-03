package com.microsmartgrid.timescaleDbReader.controller;

import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.timescaleDbReader.TimescaleDbReaderApplication;
import javassist.NotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
// TODO: Access noch richtig einstellen. Momentan kann jeder darauf zugreifen.
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TimescaleDbReaderController {


	/**
	 * Query for all devices
	 *
	 * @return A list of all registered devices
	 */
	@GetMapping("/deviceList")
	public List<DeviceInformation> queryDeviceList() {
		return new TimescaleDbReaderApplication().queryDeviceList();
	}

	/**
	 * Query devices table
	 *
	 * @param id Internally generated id of the device
	 * @return device with input id
	 */
	@GetMapping("/deviceById")
	public DeviceInformation queryDevices(@RequestParam("id") int id) throws NotFoundException {
		return new TimescaleDbReaderApplication().queryDevices(id);
	}
}
