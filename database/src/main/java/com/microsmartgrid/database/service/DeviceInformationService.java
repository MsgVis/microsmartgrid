package com.microsmartgrid.database.service;

import com.microsmartgrid.database.repository.DeviceInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceInformationService {

	@Autowired
	private DeviceInformationRepository repository;

}
