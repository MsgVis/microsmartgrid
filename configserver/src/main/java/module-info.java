module com.microsmartgrid.configserver {
	requires spring.boot;
	requires spring.cloud.config.server;
	requires spring.boot.autoconfigure;
	exports com.microsmartgrid.configserver;
}
