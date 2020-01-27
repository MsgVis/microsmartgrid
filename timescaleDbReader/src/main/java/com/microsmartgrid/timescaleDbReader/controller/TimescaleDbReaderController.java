package com.microsmartgrid.timescaleDbReader.controller

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/timescaleDbReader")
public class DemoController {

	@RequestMapping(path = "/subDemo", method = RequestMethod.GET)
	public HelloObject getHelloWordObject() {
		return "Hello Demo";
	}
}
