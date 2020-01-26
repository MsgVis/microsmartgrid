package com.microsmartgrid.database.services.DaiSmartGrid;

import com.microsmartgrid.database.repositories.DaiSmartGrid.ReadingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadingsService {

	@Autowired
	private ReadingsRepository repository;
}
