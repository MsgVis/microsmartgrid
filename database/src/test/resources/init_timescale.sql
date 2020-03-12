CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;

SELECT create_hypertable('readings', 'time');
