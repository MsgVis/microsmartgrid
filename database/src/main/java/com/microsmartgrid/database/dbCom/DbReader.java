package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Readings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.microsmartgrid.database.HelperFunctions.getClassFromIdentifier;
import static com.microsmartgrid.database.dbCom.DbHandle.getConnection;
import static com.microsmartgrid.database.dbCom.SqlCommands.*;

//TODO Access noch richtig einstellen. Momentan kann jeder darauf zugreifen.
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class DbReader {

	private static final Logger logger = LogManager.getLogger(DbReader.class);


	@GetMapping("/dummyCall")
	public static String dummyString() throws IOException {
		return "{\"var\": \"Hallo schöner Mensch!\" }";//ObjectMapperManager.getMapper().readValue(new File("./dummy_topology.json"), String.class);
	}

	/**
	 * ''
	 * Query for all devices
	 *
	 * @return A list of all registered devices
	 */
	@GetMapping("/deviceList")
	public static List<AdditionalDeviceInformation> queryDeviceList() {
		List<AdditionalDeviceInformation> infos = new ArrayList<>();
		try (Connection conn = getConnection();
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
		try (Connection conn = getConnection();
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
	@GetMapping("/device")
	public static AdditionalDeviceInformation queryDevices(int id) {
		AdditionalDeviceInformation info = null;
		try (Connection conn = getConnection();
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

	public static <T extends Readings> List<T> queryReadings(int id, String start, String end, String min_interval) {
		List<T> readings = new ArrayList<>();
		try (Connection conn = getConnection();
			 PreparedStatement stmt = conn.prepareStatement(QUERY_READINGS)) {

			stmt.setString(1, min_interval);
			stmt.setInt(2, id);
			stmt.setString(3, start);
			stmt.setString(4, end);

			ResultSet rs = stmt.executeQuery();

			if (rs == null) throw new SQLException("Database does not exist");

			while (rs.next()) {
				int device_id = rs.getInt("device_id");
				AdditionalDeviceInformation info = queryDevices(device_id);
				Class<? extends AbstractDevice> cls = getClassFromIdentifier(info.getName());
				T read = (T) cls.getDeclaredConstructor().newInstance();

				read.setId(device_id);
				read.setTimestamp(rs.getTimestamp("bucket").toInstant());
				read.setCurrent_I_avg(rs.getFloat("i_avg"));
				read.setCurrent_I1(rs.getFloat("i_r"));
				read.setCurrent_I2(rs.getFloat("i_s"));
				read.setCurrent_I3(rs.getFloat("i_t"));
				read.setVoltage_U_avg(rs.getFloat("u_avg"));
				read.setVoltage_U1(rs.getFloat("u_r"));
				read.setVoltage_U2(rs.getFloat("u_s"));
				read.setVoltage_U3(rs.getFloat("u_t"));
				read.setActive_energy_A_plus(rs.getFloat("a_plus"));
				read.setActive_energy_A_minus(rs.getFloat("a_minus"));
				read.setReactive_energy_R_plus(rs.getFloat("r_plus"));
				read.setReactive_energy_R_minus(rs.getFloat("r_minus"));
				read.setActive_power_P_total(rs.getFloat("p_total"));
				read.setActive_power_P1(rs.getFloat("p_r"));
				read.setActive_power_P2(rs.getFloat("p_s"));
				read.setActive_power_P3(rs.getFloat("p_t"));
				read.setReactive_power_Q_total(rs.getFloat("q_total"));
				read.setReactive_power_Q1(rs.getFloat("q_r"));
				read.setReactive_power_Q2(rs.getFloat("q_s"));
				read.setReactive_power_Q3(rs.getFloat("q_t"));
				read.setApparent_power_S_total(rs.getFloat("s_total"));
				read.setApparent_power_S1(rs.getFloat("s_r"));
				read.setApparent_power_S2(rs.getFloat("s_s"));
				read.setApparent_power_S3(rs.getFloat("s_t"));
				read.setFrequency_grid(rs.getFloat("f"));
				read.setMetaInformation(null);

				readings.add(read);
			}

			rs.close();
		} catch (SQLException | IOException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			logger.warn("Could not fetch readings.");
			e.printStackTrace();
		}
		return readings;
	}
}
