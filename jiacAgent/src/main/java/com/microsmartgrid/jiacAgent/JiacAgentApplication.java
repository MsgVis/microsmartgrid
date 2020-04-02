package com.microsmartgrid.jiacAgent;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.microsmartgrid.jiacAgent"})
@EnableDiscoveryClient
public class JiacAgentApplication {

	/**
	 * Start an agent node, wait a few seconds, and stop the node again.
	 *
	 * @param args: configfile: slash-delimeted path to config file (from classpath), nodename: name of the node to start
	 */
	public static void main(String[] args) {
		NodeStarter.startNode(
			"rest.xml",
			"RESTPlatform");
	}
}
