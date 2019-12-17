package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Readings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import static com.microsmartgrid.database.dbCom.DbHandle.getConnection;
import static com.microsmartgrid.database.dbCom.SqlCommands.INSERT_DEVICES_SQL;
import static com.microsmartgrid.database.dbCom.SqlCommands.INSERT_READINGS_SQL;

public class DbWriter {
	private static final Logger logger = LogManager.getLogger();

	public static <T extends AbstractDevice> void writeDeviceToDatabase(int id, T device) {
		device.setId(id);

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
		try (Connection conn = getConnection();
			 PreparedStatement info = conn.prepareStatement(INSERT_DEVICES_SQL, Statement.RETURN_GENERATED_KEYS);) {

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
		try (Connection conn = getConnection();
			 PreparedStatement reading = conn.prepareStatement(INSERT_READINGS_SQL)) {

			reading.setTimestamp(1, Timestamp.from(device.getTimestamp()));
			reading.setInt(2, device.getId());
			reading.setFloat(3, device.getActive_energy_A_minus());
			reading.setFloat(4, device.getActive_energy_A_plus());
			reading.setFloat(5, device.getReactive_energy_R_minus());
			reading.setFloat(6, device.getReactive_energy_R_plus());
			reading.setFloat(7, device.getActive_power_P_total());
			reading.setFloat(8, device.getActive_power_P1());
			reading.setFloat(9, device.getActive_power_P2());
			reading.setFloat(10, device.getActive_power_P3());
			reading.setFloat(11, device.getReactive_power_Q_total());
			reading.setFloat(12, device.getReactive_power_Q1());
			reading.setFloat(13, device.getReactive_power_Q2());
			reading.setFloat(14, device.getReactive_power_Q3());
			reading.setFloat(15, device.getApparent_power_S_total());
			reading.setFloat(16, device.getApparent_power_S1());
			reading.setFloat(17, device.getApparent_power_S2());
			reading.setFloat(18, device.getApparent_power_S3());
			reading.setFloat(19, device.getCurrent_I_avg());
			reading.setFloat(20, device.getCurrent_I1());
			reading.setFloat(21, device.getCurrent_I2());
			reading.setFloat(22, device.getCurrent_I3());
			reading.setFloat(23, device.getVoltage_U_avg());
			reading.setFloat(24, device.getVoltage_U1());
			reading.setFloat(25, device.getVoltage_U2());
			reading.setFloat(26, device.getVoltage_U3());
			reading.setFloat(27, device.getFrequency_grid());

			reading.executeUpdate();

		} catch (SQLException e) {
			logger.warn("Couldn't commit reading connected to device with id " +
				(device.getId() > 0 ? device.getId() : "none (no device information set yet)") + " to database");
			e.printStackTrace();
		}
	}
}
