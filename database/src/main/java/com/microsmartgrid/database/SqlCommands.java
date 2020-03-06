package com.microsmartgrid.database;

public abstract class SqlCommands {

	public static final String QUERY_READINGS = "SELECT *" +
		" FROM readings" +
		" WHERE (:id = 0 OR device_id = :id)" +
		" AND (:since = '' OR time >= now() - CAST(:since AS INTERVAL))" +
		" AND (:until = '' OR time <= now() - CAST(:until AS INTERVAL))" +
		" GROUP BY time, device_id ORDER BY time DESC";
	public static final String QUERY_READINGS_AVERAGES = "SELECT device_id," +
		" time_bucket(CAST(:step AS INTERVAL), time) AS time," +
		" avg(a_minus) AS a_minus," +
		" avg(a_plus) AS a_plus," +
		" avg(r_minus) AS r_minus," +
		" avg(r_plus) AS r_plus," +
		" avg(p_total) AS p_total," +
		" avg(p_r) AS p_r," +
		" avg(p_s) AS p_s," +
		" avg(p_t) AS p_t," +
		" avg(q_total) AS q_total," +
		" avg(q_r) AS q_r," +
		" avg(q_s) AS q_s," +
		" avg(q_t) AS q_t," +
		" avg(s_total) AS s_total," +
		" avg(s_r) AS s_r," +
		" avg(s_s) AS s_s," +
		" avg(s_t) AS s_t," +
		" avg(i_avg) AS i_avg," +
		" avg(i_r) AS i_r," +
		" avg(i_s) AS i_s," +
		" avg(i_t) AS i_t," +
		" avg(u_avg) AS u_avg," +
		" avg(u_r) AS u_r," +
		" avg(u_s) AS u_s," +
		" avg(u_t) AS u_t," +
		" avg(f) as f," +
		" '{}' as meta" +
		" FROM readings" +
		" WHERE (:id = 0 OR device_id = :id)" +
		" AND (:since = '' OR time >= now() - CAST(:since AS INTERVAL))" +
		" AND (:until = '' OR time <= now() - CAST(:until AS INTERVAL))" +
		" GROUP BY time, device_id ORDER BY time ASC";
	public static final String QUERY_READINGS_STDDEV = "SELECT device_id," +
		" time_bucket(CAST(:step AS INTERVAL), time) AS time," +
		" stddev_pop(a_minus) AS a_minus," +
		" stddev_pop(a_plus) AS a_plus," +
		" stddev_pop(r_minus) AS r_minus," +
		" stddev_pop(r_plus) AS r_plus," +
		" stddev_pop(p_total) AS p_total," +
		" stddev_pop(p_r) AS p_r," +
		" stddev_pop(p_s) AS p_s," +
		" stddev_pop(p_t) AS p_t," +
		" stddev_pop(q_total) AS q_total," +
		" stddev_pop(q_r) AS q_r," +
		" stddev_pop(q_s) AS q_s," +
		" stddev_pop(q_t) AS q_t," +
		" stddev_pop(s_total) AS s_total," +
		" stddev_pop(s_r) AS s_r," +
		" stddev_pop(s_s) AS s_s," +
		" stddev_pop(s_t) AS s_t," +
		" stddev_pop(i_avg) AS i_avg," +
		" stddev_pop(i_r) AS i_r," +
		" stddev_pop(i_s) AS i_s," +
		" stddev_pop(i_t) AS i_t," +
		" stddev_pop(u_avg) AS u_avg," +
		" stddev_pop(u_r) AS u_r," +
		" stddev_pop(u_s) AS u_s," +
		" stddev_pop(u_t) AS u_t," +
		" stddev_pop(f) as f," +
		" '{}' as meta" +
		" FROM readings" +
		" WHERE (:id = 0 OR device_id = :id)" +
		" AND (:since = '' OR time >= now() - CAST(:since AS INTERVAL))" +
		" AND (:until = '' OR time <= now() - CAST(:until AS INTERVAL))" +
		" GROUP BY time, device_id ORDER BY time ASC";
	public static final String QUERY_READINGS_MIN = "SELECT device_id," +
		" time_bucket(CAST(:step AS INTERVAL), time) AS time," +
		" min(a_minus) AS a_minus," +
		" min(a_plus) AS a_plus," +
		" min(r_minus) AS r_minus," +
		" min(r_plus) AS r_plus," +
		" min(p_total) AS p_total," +
		" min(p_r) AS p_r," +
		" min(p_s) AS p_s," +
		" min(p_t) AS p_t," +
		" min(q_total) AS q_total," +
		" min(q_r) AS q_r," +
		" min(q_s) AS q_s," +
		" min(q_t) AS q_t," +
		" min(s_total) AS s_total," +
		" min(s_r) AS s_r," +
		" min(s_s) AS s_s," +
		" min(s_t) AS s_t," +
		" min(i_avg) AS i_avg," +
		" min(i_r) AS i_r," +
		" min(i_s) AS i_s," +
		" min(i_t) AS i_t," +
		" min(u_avg) AS u_avg," +
		" min(u_r) AS u_r," +
		" min(u_s) AS u_s," +
		" min(u_t) AS u_t," +
		" min(f) as f," +
		" '{}' as meta" +
		" FROM readings" +
		" WHERE (:id = 0 OR device_id = :id)" +
		" AND (:since = '' OR time >= now() - CAST(:since AS INTERVAL))" +
		" AND (:until = '' OR time <= now() - CAST(:until AS INTERVAL))" +
		" GROUP BY time, device_id ORDER BY time ASC";
	public static final String QUERY_READINGS_MAX = "SELECT device_id," +
		" time_bucket(CAST(:step AS INTERVAL), time) AS time," +
		" max(a_minus) AS a_minus," +
		" max(a_plus) AS a_plus," +
		" max(r_minus) AS r_minus," +
		" max(r_plus) AS r_plus," +
		" max(p_total) AS p_total," +
		" max(p_r) AS p_r," +
		" max(p_s) AS p_s," +
		" max(p_t) AS p_t," +
		" max(q_total) AS q_total," +
		" max(q_r) AS q_r," +
		" max(q_s) AS q_s," +
		" max(q_t) AS q_t," +
		" max(s_total) AS s_total," +
		" max(s_r) AS s_r," +
		" max(s_s) AS s_s," +
		" max(s_t) AS s_t," +
		" max(i_avg) AS i_avg," +
		" max(i_r) AS i_r," +
		" max(i_s) AS i_s," +
		" max(i_t) AS i_t," +
		" max(u_avg) AS u_avg," +
		" max(u_r) AS u_r," +
		" max(u_s) AS u_s," +
		" max(u_t) AS u_t," +
		" max(f) as f," +
		" '{}' as meta" +
		" FROM readings" +
		" WHERE (:id = 0 OR device_id = :id)" +
		" AND (:since = '' OR time >= now() - CAST(:since AS INTERVAL))" +
		" AND (:until = '' OR time <= now() - CAST(:until AS INTERVAL))" +
		" GROUP BY time, device_id ORDER BY time ASC";
}
