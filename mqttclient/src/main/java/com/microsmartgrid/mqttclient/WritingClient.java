package com.microsmartgrid.mqttclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@FeignClient(name = "timescaleDbWriter")
public interface WritingClient {

	@GetMapping("/reading")
	@ExceptionHandler({IOException.class})
	void writeReadingToDatabase(@RequestParam("topic") String topic, @RequestBody String json) throws IOException;
}
