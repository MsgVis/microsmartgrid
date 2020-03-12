package com.microsmartgrid.timescaleDbReader.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DaiSmartGrid.ReadingsRepository;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import com.microsmartgrid.database.service.DaiSmartGrid.ReadingsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimescaleDbReaderController.class)
public class TimescaleDbReaderControllerTests {

	static DeviceInformation device1 = new DeviceInformation();
	static DeviceInformation device2 = new DeviceInformation();
	static DeviceInformation device3 = new DeviceInformation();
	static List<DeviceInformation> devices;

	@MockBean
	private DeviceInformationRepository deviceRepo;

	@MockBean
	private ReadingsService readingsService;
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
	public void testDeviceList() throws Exception {
		when(deviceRepo.findAll()).thenReturn(devices);

		mvc.perform(get("/deviceList"))
			.andExpect(status().isOk())
			.andExpect(mvcResult ->
				assertThat((List<?>) objM.readValue(mvcResult.getResponse().getContentAsString(),
					objM.getTypeFactory().constructCollectionType(List.class, DeviceInformation.class)))
					.isEqualTo(devices)
			);
	}

	@Test
	public void testDevicesByIdPositive() throws Exception {
		when(deviceRepo.findById(anyInt())).thenAnswer(i -> Optional.of(devices.get(i.getArgument(0))));

		mvc.perform(get("/deviceById").param("id", "0"))
			.andExpect(status().isOk())
			.andExpect(mvcResult ->
				assertThat(objM.readValue(mvcResult.getResponse().getContentAsString(), DeviceInformation.class))
					.isEqualTo(device1)
			);
	}

	@Test
	public void testDevicesByIdNegative() throws Exception {
		when(deviceRepo.findById(anyInt())).thenReturn(Optional.empty());

		mvc.perform(get("/deviceById").param("id", "3"))
			.andExpect(status().isNotFound());
	}

	@Test
	public void testDevicesByNameNegative() throws Exception {
		when(deviceRepo.findByName(anyString())).thenReturn(Optional.empty());

		mvc.perform(get("/deviceByName").param("name", "unique"))
			.andExpect(status().isOk())
			.andExpect(mvcResult ->
				assertThat(mvcResult.getResponse().getContentType())
					.isEqualTo(null)
			);
	}


}
