package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.Configurations;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Readings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Map;

import static com.microsmartgrid.database.dbCom.SqlCommands.INSERT_DEVICES_SQL;
import static com.microsmartgrid.database.dbCom.SqlCommands.INSERT_READINGS_SQL;

/**
 * Connection to PostgreSQL database
 */
public abstract class DbHandle {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Execute SQL command
	 *
	 * @param command
	 */
	public static void execute(String command) {
		try (Connection conn = getConnection();
			 Statement stmt = conn.createStatement()) {
			stmt.execute(command);
		} catch (SQLException e) {
			logger.error("Couldn't excecute: " + command);
			logger.error(e);
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
				rs.first();
				generatedId = rs.getInt(1);
			} else {
				throw new SQLIntegrityConstraintViolationException("Couldn't create new id for deviceInformattion with name " + deviceInfo.getName());
			}
			conn.commit();

		} catch (SQLException e) {
			logger.warn("Couldn't commit deviceInformation with name " + deviceInfo.getName() + " to database.");
			e.printStackTrace();
		}
		return generatedId;
	}

	private static Connection getConnection() throws SQLException {
		Map<String, String> cfg = Configurations.getJdbcConfiguration();
		return DriverManager.getConnection(cfg.get("url"), cfg.get("username"), cfg.get("password"));
	}

	/**
	 * Insert Readings object into database
	 *
	 * @param device
	 */
	public static void insertReadings(Readings device) {
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
			conn.commit();

		} catch (SQLException e) {
			logger.warn("Couldn't commit reading connected to device with id " +
				(device.getId() > 0 ? device.getId() : "none (no device information set yet)") + " to database");
			e.printStackTrace();
		}
	}


	/**
	 * Query devices table
	 *
	 * @param topic
	 * @return
	 */
	public static AdditionalDeviceInformation queryDevices(String topic) {
		AdditionalDeviceInformation info = null;
		try (Connection conn = getConnection();
			 Statement stmt = conn.createStatement()) {

			ResultSet rs = stmt.executeQuery("SELECT * FROM devices WHERE name='" + topic + "';");

			if (rs == null) throw new SQLException("Database does not exist");
			if (!rs.next()) return info;

			info = new AdditionalDeviceInformation(rs.getString("name"));
			info.setId(rs.getInt("id"));
			info.setDescription(rs.getString("description"));
			String type = rs.getString("type");
			info.setType(type == null ? null : AdditionalDeviceInformation.Type.valueOf(type));
			String subtype = rs.getString("subtype");
			info.setSubtype(subtype == null ? null : AdditionalDeviceInformation.Subtype.valueOf(subtype));

			Array arr = rs.getArray("children");
			Integer[] children;
			if (arr == null) {
				children = new Integer[0];
			} else {
				Object[] sqlArray = (Object[]) arr.getArray();
				children = new Integer[sqlArray.length];
				for (int i = 0; i < sqlArray.length; i++) {
					children[i] = (Integer) sqlArray[i];
				}
			}
			info.setChildren(children);

			rs.close();

		} catch (SQLException e) {
			logger.warn("Could not fetch device info with name='" + topic + "'");
			e.printStackTrace();
		}
		return info;
	}

}
