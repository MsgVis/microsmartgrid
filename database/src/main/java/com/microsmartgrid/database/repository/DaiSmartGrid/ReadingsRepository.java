package com.microsmartgrid.database.repository.DaiSmartGrid;

import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.microsmartgrid.database.SqlCommands.*;

@Repository
public interface ReadingsRepository extends JpaRepository<Readings, DeviceInformation> {

	Optional<Readings> findFirstByDeviceInformationAndTimestampAfterOrderByTimestampDesc(DeviceInformation id, Instant cutoff);

	@Query(value = QUERY_READINGS_AVERAGES + FROM_READINGS_AGG, nativeQuery = true)
	List<Map<String, Object>> findReadingsAvg(@Param("id") int id, @Param("since") long since, @Param("until") long until, @Param("step") String step);

	@Query(value = QUERY_READINGS_STDDEV + FROM_READINGS_AGG, nativeQuery = true)
	List<Map<String, Object>> findReadingsStd(@Param("id") int id, @Param("since") long since, @Param("until") long until, @Param("step") String step);

	@Query(value = QUERY_READINGS_MIN + FROM_READINGS_AGG, nativeQuery = true)
	List<Map<String, Object>> findReadingsMin(@Param("id") int id, @Param("since") long since, @Param("until") long until, @Param("step") String step);

	@Query(value = QUERY_READINGS_MAX + FROM_READINGS_AGG, nativeQuery = true)
	List<Map<String, Object>> findReadingsMax(@Param("id") int id, @Param("since") long since, @Param("until") long until, @Param("step") String step);

	@Query(value = QUERY_READINGS, nativeQuery = true)
	List<Readings> findReadings(@Param("id") int id, @Param("since") long since, @Param("until") long until);
}
