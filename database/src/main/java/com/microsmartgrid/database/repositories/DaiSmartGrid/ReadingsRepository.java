package com.microsmartgrid.database.repositories.DaiSmartGrid;

import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingsRepository extends JpaRepository<Readings, Integer> {
}
