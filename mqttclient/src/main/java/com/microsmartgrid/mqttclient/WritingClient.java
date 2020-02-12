package com.microsmartgrid.mqttclient;

import com.fasterxml.jackson.databind.node.TextNode;
import javassist.NotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@FeignClient(name = "timescaleDbWriter")
public interface WritingClient {

	@PostMapping("/reading")
	@ExceptionHandler({IOException.class, NotFoundException.class})
	void writeDeviceToDatabase(@RequestParam("name") String topic, @RequestBody TextNode json) throws IOException, NotFoundException;
}
