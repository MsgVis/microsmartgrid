package com.microsmartgrid.jiacAgent;

import de.dailab.jiactng.agentcore.SimpleAgentNode;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NodeStarter {

	/**
	 * Start an agent node, wait a few seconds, and stop the node again.
	 *
	 * @param configfile slash-delimeted path to config file (from classpath)
	 * @param nodename   name of the node to start
	 */
	public static void startNode(String configfile, String nodename) {

		// use JIAC's default log4j configuraten
		System.setProperty("log4j.configuration", "jiactng_log4j.properties");

		// start node
		SimpleAgentNode node = (SimpleAgentNode) new ClassPathXmlApplicationContext(configfile).getBean(nodename);

	}

}
