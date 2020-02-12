package com.microsmartgrid.database.repository;

import com.microsmartgrid.database.model.DeviceInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JpaRepository enables basic CRUD operations, paging and sorting, and flush operations. <ManagedClass, IdType>
 * New queries can be automatically generated like findByName() using the spring data syntax.
 * A custom Query can also be assigned to a function with help of the @Query annotation.
 */
@Repository
public interface DeviceInformationRepository extends JpaRepository<DeviceInformation, Integer> {

	Optional<DeviceInformation> findByName(String name);

}
