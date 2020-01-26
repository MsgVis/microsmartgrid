package com.microsmartgrid.database.services;

import com.microsmartgrid.database.repositories.DeviceInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceInformationService {

	@Autowired
	private DeviceInformationRepository repository;

}
