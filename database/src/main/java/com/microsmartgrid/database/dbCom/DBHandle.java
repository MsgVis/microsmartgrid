package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.DaiSmartGrid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.microsmartgrid.database.dbDataStructures.AbstractDevice;

/**
 * Connection to PostgreSQL database
 */
public class DBHandle {

	private static final Logger logger = LogManager.getLogger(DBHandle.class.getName());

	private Connection conn;
	private Statement stmt;
	private ResultSet rs;

	/**
	 * Connect to postgres database on same machine (localhost)
	 *
	 * @param database
	 * @param username
	 * @param password
	 */
	public void connect(String database, String username, String password) throws SQLException {
		try {
			if (conn != null && conn.isValid(0)) throw new IllegalStateException("Handle already connected");
			conn = DriverManager.getConnection(database, username, password);
		} catch (SQLException e) {
			logger.fatal("Couldn't establish connection to database.");
			throw new SQLException(e);
		}
	}

	/**
	 * Execute SQL command
	 *
	 * @param command
	 */
	public void execute(String command) throws SQLException {
		try {
			if (conn == null || conn.isClosed()) throw new NullPointerException("Handle not connected");
			stmt = conn.createStatement();
			stmt.execute(command);
		} catch (SQLException e) {
			logger.error("Couldn't excecute: " + command);
			logger.error(e);
		}
	}

	/**
	 * Execute SQL command which returns ResultSet
	 *
	 * @param command
	 * @return
	 */
	public ResultSet executeQuery(String command) {
		try {
			if (conn == null || conn.isClosed()) throw new NullPointerException("Handle not connected");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(command);
		} catch (SQLException e) {
			logger.error("Couldn't execute query: " + command);
			logger.error(e);
		}
		return rs;
	}

	/**
	 * Insert device object into database
	 * @param device
	 */
	public void insertObject(AdditionalDeviceInformation deviceInfo, DaiSmartGrid device) throws SQLException{
		PreparedStatement insertReadings = null;
		PreparedStatement insertDevices = null;

		String insertReadingsString =
			"insert into " +
			"readings" +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		String insertDevicesString =
			"insert into " +
			"devices" +
			" values (?, ?, ?, ?, ?, ?, ?);";

		try{
			conn.setAutoCommit(false);
			insertReadings = conn.prepareStatement(insertReadingsString);
			insertDevices = conn.prepareStatement(insertDevicesString);

			if(device instanceof DaiSmartGrid){
				insertReadings.setTimestamp(1, Timestamp.from(device.getTimestamp()));
				insertReadings.setInt(2, device.getId());
				insertReadings.setFloat(3, device.getActive_energy_A_minus());
				insertReadings.setFloat(4, device.getActive_energy_A_plus());
				insertReadings.setFloat(5, device.getReactive_energy_R_minus());
				insertReadings.setFloat(6, device.getReactive_energy_R_plus());
				insertReadings.setFloat(7, device.getActive_power_P_total());
				insertReadings.setFloat(8, device.getActive_power_P_total());
				insertReadings.setFloat(9, device.getActive_power_P1());
				insertReadings.setFloat(10, device.getActive_power_P2());
				insertReadings.setFloat(11, device.getActive_power_P3());
				insertReadings.setFloat(12, device.getReactive_power_Q_total());
				insertReadings.setFloat(13, device.getReactive_power_Q1());
				insertReadings.setFloat(14, device.getReactive_power_Q2());
				insertReadings.setFloat(15, device.getReactive_power_Q3());
				insertReadings.setFloat(16, device.getApparent_power_S_total());
				insertReadings.setFloat(17, device.getApparent_power_S1());
				insertReadings.setFloat(18, device.getApparent_power_S2());
				insertReadings.setFloat(19, device.getApparent_power_S3());
				insertReadings.setFloat(20, device.getCurrent_I_avg());
				insertReadings.setFloat(21, device.getCurrent_I1());
				insertReadings.setFloat(22, device.getCurrent_I2());
				insertReadings.setFloat(23, device.getCurrent_I3());
				insertReadings.setFloat(24, device.getVoltage_U_avg());
				insertReadings.setFloat(25, device.getVoltage_U1());
				insertReadings.setFloat(26, device.getVoltage_U2());
				insertReadings.setFloat(27, device.getVoltage_U3());
				insertReadings.setFloat(28, device.getFrequency_grid());

				insertDevices.setString(1, deviceInfo.getName());
				insertDevices.setString(2, deviceInfo.getDescription());
				insertDevices.setString(3, deviceInfo.getType().toString());
				insertDevices.setString(4, deviceInfo.getSubtype().toString());
				insertDevices.setInt(5, deviceInfo.getDepth());
				insertDevices.setString(6, deviceInfo.getIcon());
				// insertDevices.setArray(7, Arrays.stream(deviceInfo.getChildren()).map(t -> t.getId()).toArray());
			}

			conn.commit();

		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			if(insertReadings != null) {
				insertReadings.close();
			}
			conn.setAutoCommit(true);
		}

	}

	/**
	 * Close connection
	 */
	public void cleanUp() throws SQLException {
		try {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			logger.fatal("Couldn't close connection to database.");
			throw new SQLException(e);
		}
	}
}
