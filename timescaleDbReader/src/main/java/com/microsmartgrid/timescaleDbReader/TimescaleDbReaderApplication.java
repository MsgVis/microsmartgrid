package com.microsmartgrid.timescaleDbReader;

import com.microsmartgrid.database.dbCom.DatabaseConfig;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.microsmartgrid.database.dbCom.SqlCommands.*;

@SpringBootApplication
@EnableDiscoveryClient
public class TimescaleDbReaderApplication {
	private static final Logger logger = LogManager.getLogger(TimescaleDbReaderApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TimescaleDbReaderApplication.class, args);
	}

	/**
	 * Query for all devices
	 *
	 * @return A list of all registered devices
	 */
	public static List<AdditionalDeviceInformation> queryDeviceList() {
		List<AdditionalDeviceInformation> infos = new ArrayList<>();
		try (Connection conn = new DatabaseConfig().getConnection();
			 PreparedStatement stmt = conn.prepareStatement(QUERY_ALL_DEVICES)) {

			ResultSet rs = stmt.executeQuery();

			if (rs == null) throw new SQLException("Database does not exist");

			while (rs.next()) {
				infos.add(deserializeInfo(rs));
			}

			rs.close();
		} catch (SQLException e) {
			logger.warn("Could not fetch device list.");
			e.printStackTrace();
		}
		return infos;
	}

	/**
	 * Query devices table by name for internal use when there is no id available
	 *
	 * @param name Mqtt topic, ...
	 * @return device with input name or null if none could be found
	 */
	public static AdditionalDeviceInformation queryDevices(String name) {
		AdditionalDeviceInformation info = null;
		try (Connection conn = new DatabaseConfig().getConnection();
			 PreparedStatement stmt = conn.prepareStatement(QUERY_DEVICES)) {

			stmt.setString(1, name);

			ResultSet rs = stmt.executeQuery();

			if (rs == null) throw new SQLException("Database does not exist");
			if (!rs.next()) return info;

			info = deserializeInfo(rs);

			rs.close();

		} catch (SQLException e) {
			logger.warn("Could not fetch device info with name=" + name);
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * Query devices table
	 *
	 * @param id Internally generated id of the device
	 * @return device with input id or null if none could be found
	 */
	public static AdditionalDeviceInformation queryDevices(int id) {
		AdditionalDeviceInformation info = null;
		try (Connection conn = new DatabaseConfig().getConnection();
			 PreparedStatement stmt = conn.prepareStatement(QUERY_DEVICES_BY_ID)) {

			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery();

			if (rs == null) throw new SQLException("Database does not exist");
			if (!rs.next()) return info;

			info = deserializeInfo(rs);

			rs.close();

		} catch (SQLException e) {
			logger.warn("Could not fetch device info with id=" + id);
			e.printStackTrace();
		}
		return info;
	}

	private static AdditionalDeviceInformation deserializeInfo(ResultSet rs) throws SQLException {
		AdditionalDeviceInformation info = new AdditionalDeviceInformation(rs.getString("name"));
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

		return info;
	}
}
