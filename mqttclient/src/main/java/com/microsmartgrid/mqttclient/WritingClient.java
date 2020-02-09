package com.microsmartgrid.mqttclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@FeignClient(name = "timescaleDbWriter")
public interface WritingClient {

	@PostMapping("/reading")
	@ExceptionHandler({IOException.class})
	void writeReadingToDatabase(@RequestParam("topic") String topic, @RequestParam("json") String json) throws IOException;
}
