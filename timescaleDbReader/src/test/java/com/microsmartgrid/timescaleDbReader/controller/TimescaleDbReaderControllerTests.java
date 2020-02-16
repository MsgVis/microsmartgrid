package com.microsmartgrid.timescaleDbReader.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DaiSmartGrid.ReadingsRepository;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import com.microsmartgrid.database.service.DaiSmartGrid.ReadingsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TimescaleDbReaderController.class)
public class TimescaleDbReaderControllerTests {

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

	@Test
	public void testDeviceList() throws Exception {
		DeviceInformation device1 = new DeviceInformation();
		device1.setId(1);
		device1.setName("SomeTestName1");
		DeviceInformation device2 = new DeviceInformation();
		device2.setId(2);
		device2.setName("SomeTestName2");

		List<DeviceInformation> devices = Arrays.asList(device1, device2);
		when(deviceRepo.findAll()).thenReturn(devices);

		mvc.perform(get("/deviceList"))
			.andExpect(status().isOk())
			.andExpect(mvcResult ->
				assertThat((List<?>) objM.readValue(mvcResult.getResponse().getContentAsString(),
					objM.getTypeFactory().constructCollectionType(List.class, DeviceInformation.class))).isEqualTo(devices)
			);
	}


}
