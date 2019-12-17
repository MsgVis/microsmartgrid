package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import static com.microsmartgrid.database.dbCom.DbHandle.getConnection;

public class DbReader {

	private static final Logger logger = LogManager.getLogger();

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
