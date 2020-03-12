package com.microsmartgrid.timescaleDbWriter.controller;

import com.fasterxml.jackson.databind.node.TextNode;
import com.microsmartgrid.database.model.AbstractDevice;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import com.microsmartgrid.database.service.AbstractDeviceService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.IOException;


@RestController
// TODO: Access noch richtig einstellen. Momentan kann jeder darauf zugreifen.
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TimescaleDbWriterController {

	@Autowired
	private DeviceInformationRepository deviceInfoRepository;
	@Autowired
	private AbstractDeviceService abstractDeviceService;

	/**
	 * Save or update DeviceInformation to a Device
	 *
	 * @param deviceInfo DeviceInformation object to save
	 * @return saved Object
	 */
	@PutMapping("/device")
	public DeviceInformation saveDeviceInfo(@RequestBody @Valid DeviceInformation deviceInfo) {
		try {
			return deviceInfoRepository.save(deviceInfo);
		} catch (ConstraintViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The 'name' parameter must be unique.", e);
		}
	}


	/**
	 * Deserialize the json to a java object and assign it to a DeviceInformation object. Save the created object to the database.
	 *
	 * @param name unique name to assign the DeviceInformation object
	 * @param json json to save
	 * @throws IOException       if the ClassMap.yml file can't be read
	 * @throws NotFoundException if the assigned database table hasn't been created
	 */
	@PostMapping("/reading")
	@ExceptionHandler({IOException.class, NotFoundException.class})
	public <T extends AbstractDevice> T writeDeviceToDatabase(@RequestParam("name") String name, @RequestBody TextNode json) throws IOException, NotFoundException {
		return abstractDeviceService.insertDevice(name, json.asText());
	}
}
