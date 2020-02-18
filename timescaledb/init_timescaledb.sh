#!/bin/sh

set -e

psql -v ON_ERROR_STOP=1 <<-EOSQL
	    CREATE USER "user" WITH PASSWORD 'pass';
	    CREATE DATABASE "db-name" WITH OWNER "user";

	    \c db-name
		CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;

		CREATE TABLE devices (
			id	        SERIAL PRIMARY KEY,
			name		TEXT NOT NULL UNIQUE,
			description	TEXT,
			type		TEXT,
			subtype		TEXT,
			children	INTEGER[]
		);

		CREATE TABLE readings (
			time		TIMESTAMP,
			device_id	INTEGER	REFERENCES devices (id),
			a_minus		REAL,
			a_plus		REAL,
			r_minus 	REAL,
			r_plus 		REAL,
			p_total 	REAL,
			p_r		    REAL,
			p_s		    REAL,
			p_t		    REAL,
			q_total		REAL,
			q_r		    REAL,
			q_s		    REAL,
			q_t		    REAL,
			s_total		REAL,
			s_r		    REAL,
			s_s		    REAL,
			s_t		    REAL,
			i_avg		REAL,
			i_r		    REAL,
			i_s		    REAL,
			i_t		    REAL,
			u_avg		REAL,
			u_r		    REAL,
			u_s		    REAL,
			u_t		    REAL,
			f		    REAL,
			meta		JSON,
			PRIMARY KEY (time,device_id)
		);

		SELECT create_hypertable('readings', 'time');

		CREATE INDEX ON readings (device_id, time DESC);

	    GRANT ALL PRIVILEGES ON DATABASE "db-name" TO "user";
	    GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "user";
	    GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "user";
EOSQL
