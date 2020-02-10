package com.microsmartgrid.database.repository.DaiSmartGrid;

import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.microsmartgrid.database.SqlCommands.*;

@Repository
public interface ReadingsRepository extends JpaRepository<Readings, DeviceInformation> {

	Optional<Readings> findFirstByDeviceInformationAndTimestampBeforeOrderByTimestampDesc(DeviceInformation id, Instant cutoff);

	@Query(value = QUERY_READINGS_AVERAGES, nativeQuery = true)
	List<Readings> findAllAvg(@Param("id") int id, @Param("since") Instant since, @Param("until") Instant until, @Param("step") String step);

	@Query(value = QUERY_READINGS_STDDEV, nativeQuery = true)
	List<Readings> findAllStd(@Param("id") int id, @Param("since") Instant since, @Param("until") Instant until, @Param("step") String step);

	@Query(value = QUERY_READINGS_MIN, nativeQuery = true)
	List<Readings> findAllMin(@Param("id") int id, @Param("since") Instant since, @Param("until") Instant until, @Param("step") String step);

	@Query(value = QUERY_READINGS_MAX, nativeQuery = true)
	List<Readings> findAllMax(@Param("id") int id, @Param("since") Instant since, @Param("until") Instant until, @Param("step") String step);

	@Query(value = QUERY_READINGS, nativeQuery = true)
	List<Readings> findAll(@Param("id") int id, @Param("since") Instant since, @Param("until") Instant until);
}
