module com.microsmartgrid.database {
	requires java.sql;
	requires org.apache.logging.log4j;
	requires spring.web;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.databind;
	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.cloud.commons;
	requires com.fasterxml.jackson.dataformat.yaml;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires org.eclipse.paho.client.mqttv3;
	exports com.microsmartgrid.database;
}
