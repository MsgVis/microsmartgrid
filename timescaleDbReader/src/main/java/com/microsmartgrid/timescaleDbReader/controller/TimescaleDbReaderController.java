package com.microsmartgrid.timescaleDbReader.controller;

import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.timescaleDbReader.TimescaleDbReaderApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
// TODO: Access noch richtig einstellen. Momentan kann jeder darauf zugreifen.
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TimescaleDbReaderController {

	@RequestMapping(path = "/deviceList", method = RequestMethod.GET)
	public List<AdditionalDeviceInformation> queryDeviceList() {
		return TimescaleDbReaderApplication.queryDeviceList();
	}

	@RequestMapping(path = "/deviceById", method = RequestMethod.GET)
	public AdditionalDeviceInformation queryDevices(@RequestParam("id") int id) {
		return TimescaleDbReaderApplication.queryDevices(id);
	}

	@RequestMapping(path = "/deviceByName", method = RequestMethod.GET)
	public AdditionalDeviceInformation queryDevices(@RequestParam("name") String name) {
		return TimescaleDbReaderApplication.queryDevices(name);
	}
}
