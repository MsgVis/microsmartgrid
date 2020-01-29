package com.microsmartgrid.timescaleDbWriter.controller;

import com.microsmartgrid.timescaleDbWriter.TimescaleDbWriterApplication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
// TODO: Access noch richtig einstellen. Momentan kann jeder darauf zugreifen.
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TimescaleDbWriterController {

	@PostMapping()
	@ExceptionHandler({IOException.class})
	public void writeDeviceToDatabase(@RequestParam("topic") String topic, @RequestParam("json") String json) throws IOException {
		new TimescaleDbWriterApplication().writeDeviceToDatabase(topic, json);
	}
}
