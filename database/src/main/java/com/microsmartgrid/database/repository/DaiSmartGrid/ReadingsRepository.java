package com.microsmartgrid.database.repository.DaiSmartGrid;

import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingsRepository extends JpaRepository<Readings, DeviceInformation> {
}
