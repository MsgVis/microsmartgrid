package com.microsmartgrid.timescaleDbWriter.controller;

import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.timescaleDbWriter.TimescaleDbWriterApplication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
// TODO: Access noch richtig einstellen. Momentan kann jeder darauf zugreifen.
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TimescaleDbWriterController {

	/**
	 * Save or update the device
	 *
	 * @param device
	 * @return
	 */
	@PutMapping("/reading")
	public <T extends Readings> T saveReading(T device) {
		return new TimescaleDbWriterApplication().saveReading(device);
	}

	/**
	 * Save or update DeviceInformation to a Device
	 *
	 * @param deviceInfo
	 * @return
	 */
	@PutMapping("/device")
	public DeviceInformation saveDeviceInfo(DeviceInformation deviceInfo) {
		return new TimescaleDbWriterApplication().saveDeviceInfo(deviceInfo);
	}


	@PostMapping("/")
	@ExceptionHandler({IOException.class})
	public void writeDeviceToDatabase(@RequestParam("topic") String topic, @RequestParam("json") String json) throws IOException {
		new TimescaleDbWriterApplication().writeReadingToDatabase(topic, json);
	}
}
