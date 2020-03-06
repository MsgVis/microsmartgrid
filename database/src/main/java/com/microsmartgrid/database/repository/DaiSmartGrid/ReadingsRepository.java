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

	Optional<Readings> findFirstByDeviceInformationAndTimestampAfterOrderByTimestampDesc(DeviceInformation id, Instant cutoff);

	@Query(value = QUERY_READINGS_AVERAGES + OPTIONAL_BY_OFFSET, nativeQuery = true)
	List<Readings> findReadingsAvg(@Param("id") int id, @Param("since") String since, @Param("until") String until, @Param("step") String step);

	@Query(value = QUERY_READINGS_STDDEV + OPTIONAL_BY_OFFSET, nativeQuery = true)
	List<Readings> findReadingsStd(@Param("id") int id, @Param("since") String since, @Param("until") String until, @Param("step") String step);

	@Query(value = QUERY_READINGS_MIN + OPTIONAL_BY_OFFSET, nativeQuery = true)
	List<Readings> findReadingsMin(@Param("id") int id, @Param("since") String since, @Param("until") String until, @Param("step") String step);

	@Query(value = QUERY_READINGS_MAX + OPTIONAL_BY_OFFSET, nativeQuery = true)
	List<Readings> findReadingsMax(@Param("id") int id, @Param("since") String since, @Param("until") String until, @Param("step") String step);

	@Query(value = QUERY_READINGS + OPTIONAL_BY_OFFSET, nativeQuery = true)
	List<Readings> findReadings(@Param("id") int id, @Param("since") String since, @Param("until") String until);
}
