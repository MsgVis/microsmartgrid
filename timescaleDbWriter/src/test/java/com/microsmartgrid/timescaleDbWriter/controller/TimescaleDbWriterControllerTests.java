package com.microsmartgrid.timescaleDbWriter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DaiSmartGrid.ReadingsRepository;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import com.microsmartgrid.database.service.AbstractDeviceService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimescaleDbWriterController.class)
public class TimescaleDbWriterControllerTests {

	static DeviceInformation device1 = new DeviceInformation();
	static DeviceInformation device2 = new DeviceInformation();
	static DeviceInformation device3 = new DeviceInformation();
	static List<DeviceInformation> devices;

	@MockBean
	private DeviceInformationRepository deviceRepo;

	@MockBean
	private AbstractDeviceService abstractDeviceService;
	@MockBean
	private ReadingsRepository readingsRepo;

	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objM;

	@BeforeAll
	static void init() {
		device1.setId(1);
		device1.setName("SomeTestName1");
		device2.setId(2);
		device2.setName("SomeTestName2");
		device3.setId(2);
		device3.setName("SomeTestName3");

		devices = Arrays.asList(device1, device2, device3);
	}

	@Test
	void testPutDevicePositive() throws Exception {
		when(deviceRepo.save(any(DeviceInformation.class))).thenAnswer(i -> i.getArgument(0));

		mvc.perform(put("/device").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
			.content(objM.writeValueAsString(device1)))
			.andExpect(status().isOk())
			.andExpect(mvcResult ->
				assertThat(objM.readValue(mvcResult.getResponse().getContentAsString(), DeviceInformation.class))
					.isEqualTo(device1)
			);
	}

	@Test
	void testPutDeviceNew() throws Exception {
		DeviceInformation device4 = new DeviceInformation("new test object");
		when(deviceRepo.save(any(DeviceInformation.class))).thenAnswer(i -> i.getArgument(0));

		mvc.perform(put("/device").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
			.content(objM.writeValueAsString(device4)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("new test object"));
	}

	@Test
	void testPutDeviceNotUnique() throws Exception {
		DeviceInformation device1_2 = new DeviceInformation("SomeTestName1");
		device1_2.setId(10);
		doThrow(new ConstraintViolationException("could not execute statement; SQL [n/a]; constraint [devices_name_key];", null))
			.when(deviceRepo).save(any(DeviceInformation.class));

		mvc.perform(put("/device").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
			.content(objM.writeValueAsString(device1_2)))
			.andExpect(status().isBadRequest())
			.andExpect(mvcResult ->
				assertThat(mvcResult.getResolvedException())
					.hasCauseInstanceOf(ConstraintViolationException.class)
					.hasMessageContaining("The 'name' parameter must be unique.")
			);
	}
}

