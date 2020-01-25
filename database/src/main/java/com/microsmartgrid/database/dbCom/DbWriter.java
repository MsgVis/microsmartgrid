package com.microsmartgrid.database.dbCom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Readings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import static com.microsmartgrid.database.dbCom.DbReader.queryDevices;
import static com.microsmartgrid.database.dbCom.SqlCommands.INSERT_DEVICES;
import static com.microsmartgrid.database.dbCom.SqlCommands.INSERT_READINGS;

public class DbWriter {
	private static final Logger logger = LogManager.getLogger(DbWriter.class);

	public static <T extends AbstractDevice> void writeDeviceToDatabase(String topic, T device) {
		logger.debug("Querying for device with topic " + topic);
		AdditionalDeviceInformation deviceInfo = queryDevices(topic);
		if (deviceInfo == null) {
			// create new additionalDeviceInformation to the corresponding device and save topic to 'name'
			deviceInfo = new AdditionalDeviceInformation(topic);
			deviceInfo.setId(insertDeviceInfo(deviceInfo));
			logger.info("Created new device information object for name " + deviceInfo.getName() +
				" with the generated id " + deviceInfo.getId());
		}
		device.setId(deviceInfo.getId());

		logger.info("Writing " + device.toString() + " to database.");
		if (device instanceof Readings) {
			insertReadings((Readings) device);
		} else {
			logger.warn("Database commands for the class " + device.getClass() + " haven't been implemented yet");
		}
	}

	/**
	 * Insert AdditionalDeviceInformation object into database
	 *
	 * @param deviceInfo
	 */
	public static int insertDeviceInfo(AdditionalDeviceInformation deviceInfo) {
		int generatedId = 0;
		try (Connection conn = new DatabaseConfig().getConnection();
			 PreparedStatement info = conn.prepareStatement(INSERT_DEVICES, Statement.RETURN_GENERATED_KEYS);) {

			info.setString(1, deviceInfo.getName());
			info.setString(2, deviceInfo.getDescription());
			info.setString(3, deviceInfo.getType() == null ? null : deviceInfo.getType().name());
			info.setString(4, deviceInfo.getSubtype() == null ? null : deviceInfo.getSubtype().name());
			info.setArray(5, conn.createArrayOf("INTEGER", deviceInfo.getChildren()));

			if (info.executeUpdate() > 0) {
				ResultSet rs = info.getGeneratedKeys();
				rs.next();
				generatedId = rs.getInt(1);
			} else {
				throw new SQLIntegrityConstraintViolationException("Couldn't create new id for deviceInformattion with name " + deviceInfo.getName());
			}

		} catch (SQLException e) {
			logger.warn("Couldn't commit deviceInformation with name " + deviceInfo.getName() + " to database.");
			e.printStackTrace();
		}
		return generatedId;
	}

	/**
	 * Insert Readings object into database
	 *
	 * @param device
	 */
	public static <T extends Readings> void insertReadings(T device) {
		try (Connection conn = new DatabaseConfig().getConnection();
			 PreparedStatement reading = conn.prepareStatement(INSERT_READINGS)) {
			// This will be refactored anyways
			ObjectMapper objM = new ObjectMapper();

			reading.setTimestamp(1, Timestamp.from(device.getTimestamp()));
			reading.setInt(2, device.getId());
			// SQLType 1 = float
			int f = Types.FLOAT;
			reading.setObject(3, device.getActive_energy_A_minus(), f);
			reading.setObject(4, device.getActive_energy_A_plus(), f);
			reading.setObject(5, device.getReactive_energy_R_minus(), f);
			reading.setObject(6, device.getReactive_energy_R_plus(), f);
			reading.setObject(7, device.getActive_power_P_total(), f);
			reading.setObject(8, device.getActive_power_P1(), f);
			reading.setObject(9, device.getActive_power_P2(), f);
			reading.setObject(10, device.getActive_power_P3(), f);
			reading.setObject(11, device.getReactive_power_Q_total(), f);
			reading.setObject(12, device.getReactive_power_Q1(), f);
			reading.setObject(13, device.getReactive_power_Q2(), f);
			reading.setObject(14, device.getReactive_power_Q3(), f);
			reading.setObject(15, device.getApparent_power_S_total(), f);
			reading.setObject(16, device.getApparent_power_S1(), f);
			reading.setObject(17, device.getApparent_power_S2(), f);
			reading.setObject(18, device.getApparent_power_S3(), f);
			reading.setObject(19, device.getCurrent_I_avg(), f);
			reading.setObject(20, device.getCurrent_I1(), f);
			reading.setObject(21, device.getCurrent_I2(), f);
			reading.setObject(22, device.getCurrent_I3(), f);
			reading.setObject(23, device.getVoltage_U_avg(), f);
			reading.setObject(24, device.getVoltage_U1(), f);
			reading.setObject(25, device.getVoltage_U2(), f);
			reading.setObject(26, device.getVoltage_U3(), f);
			reading.setObject(27, device.getFrequency_grid(), f);
			reading.setString(28, objM.writeValueAsString(device.getMetaInformation()));

			reading.executeUpdate();

		} catch (SQLException e) {
			logger.warn("Couldn't commit reading connected to device with id " +
				(device.getId() > 0 ? device.getId() : "none (no device information set yet)") + " to database.");
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			logger.warn("Couldn't create json from meta information map.");
			e.printStackTrace();
		}
	}
}
