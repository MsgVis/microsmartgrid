module com.microsmartgrid.eurekaserver {
	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.cloud.netflix.eureka.server;
	exports com.microsmartgrid.eurekaserver;
}
