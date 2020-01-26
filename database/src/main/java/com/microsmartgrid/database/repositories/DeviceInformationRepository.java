package com.microsmartgrid.database.repositories;

import com.microsmartgrid.database.model.DeviceInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceInformationRepository extends JpaRepository<DeviceInformation, Integer> {

	Optional<DeviceInformation> findByName(String name);

}
