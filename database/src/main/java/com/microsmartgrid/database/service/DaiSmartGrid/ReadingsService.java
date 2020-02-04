package com.microsmartgrid.database.service.DaiSmartGrid;

import com.microsmartgrid.database.repository.DaiSmartGrid.ReadingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadingsService {

	@Autowired
	private ReadingsRepository repository;
}
