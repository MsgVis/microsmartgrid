package com.microsmartgrid.timescaleDbReader.controller;

import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.DeviceInformation;
import com.microsmartgrid.database.repository.DeviceInformationRepository;
import com.microsmartgrid.database.service.DaiSmartGrid.ReadingsService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Period;
import java.util.List;


@RestController
// TODO: Access noch richtig einstellen. Momentan kann jeder darauf zugreifen.
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TimescaleDbReaderController {

	@Autowired
	private DeviceInformationRepository deviceInfoRepository;
	@Autowired
	private ReadingsService readingsService;

	/**
	 * Queries the database for the last reading from each device
	 *
	 * @param cutoff Optional, default 3 days ago. Readings older than this are considered not attached to the system/deactivated.
	 * @return a list of readings, one for each active device
	 */
	@GetMapping("/latest")
	public List<Readings> queryFlow(@RequestParam("cutoff") Period cutoff) {
		return readingsService.getLatestReadings(cutoff);
	}

	/**
	 * Queries the database for all readings from one device within an interval.
	 * Readings will be aggregated over some smaller interval and the average returned.
	 *
	 * @param id    Optional. Device id.
	 * @param since Optional. Relative period from now. Start of interval.
	 * @param until Optional. Relative period from now. End of interval.
	 * @param step  aggregate interval
	 * @return list of readings objects
	 */
	@GetMapping("/readings/avg")
	public List<Readings> queryAvg(@RequestParam("id") int id, @RequestParam("since") Period since, @RequestParam("until") Period until, @RequestParam("step") String step) {
		return readingsService.getAverageAggregate(id, since, until, step);
	}

	/**
	 * Queries the database for all readings from one device within an interval.
	 * Readings will be aggregated over some smaller interval and the standard deviation returned.
	 *
	 * @param id    Optional. Device id.
	 * @param since Optional. Relative period from now. Start of interval.
	 * @param until Optional. Relative period from now. End of interval.
	 * @param step  aggregate interval
	 * @return list of readings objects
	 */
	@GetMapping("/readings/std")
	public List<Readings> queryStd(@RequestParam("id") int id, @RequestParam("since") Period since, @RequestParam("until") Period until, @RequestParam("step") String step) {
		return readingsService.getStandardDeviationAggregate(id, since, until, step);
	}

	/**
	 * Queries the database for all readings from one device within an interval.
	 * Readings will be aggregated over some smaller interval and the minimum returned.
	 *
	 * @param id    Optional. Device id.
	 * @param since Optional. Relative period from now. Start of interval.
	 * @param until Optional. Relative period from now. End of interval.
	 * @param step  aggregate interval
	 * @return list of readings objects
	 */
	@GetMapping("/readings/min")
	public List<Readings> queryMin(@RequestParam("id") int id, @RequestParam("since") Period since, @RequestParam("until") Period until, @RequestParam("step") String step) {
		return readingsService.getMinAggregate(id, since, until, step);
	}

	/**
	 * Queries the database for all readings from one device within an interval.
	 * Readings will be aggregated over some smaller interval and the maximum returned.
	 *
	 * @param id    Optional. Device id.
	 * @param since Optional. Relative period from now. Start of interval.
	 * @param until Optional. Relative period from now. End of interval.
	 * @param step  aggregate interval
	 * @return list of readings objects
	 */
	@GetMapping("/readings/max")
	public List<Readings> queryMax(@RequestParam("id") int id, @RequestParam("since") Period since, @RequestParam("until") Period until, @RequestParam("step") String step) {
		return readingsService.getMaxAggregate(id, since, until, step);
	}

	/**
	 * @param id    Optional. Device id.
	 * @param since Optional. Relative period from now. Start of interval.
	 * @param until Optional. Relative period from now. End of interval.
	 * @return list of readings objects
	 */
	@GetMapping("/readings")
	public List<Readings> queryReading(int id, Period since, Period until) {
		return readingsService.getReadings(id, since, until);
	}

	/**
	 * Query for all devices
	 *
	 * @return A list of all registered devices
	 */
	@GetMapping("/deviceList")
	public List<DeviceInformation> queryDeviceList() {
		return deviceInfoRepository.findAll();
	}

	/**
	 * Query devices table
	 *
	 * @param id Internally generated id of the device
	 * @return device with input id
	 */
	@GetMapping("/deviceById")
	public DeviceInformation queryDevices(@RequestParam("id") int id) throws NotFoundException {
		return deviceInfoRepository.findById(id).orElseThrow(() -> new NotFoundException("The device with id " + id + " doesn't exist."));
	}

	@GetMapping("/deviceByName")
	public DeviceInformation queryDevices(String name) {
		return deviceInfoRepository.findByName(name).orElse(null);
	}
}
