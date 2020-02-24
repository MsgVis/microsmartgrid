package com.microsmartgrid.jiacAgent.controller;

import de.dailab.jiactng.agentcore.action.AbstractMethodExposingBean;
import de.dailab.jiactng.agentcore.action.scope.ActionScope;
import org.springframework.web.bind.annotation.PostMapping;

import javax.ws.rs.QueryParam;

public class RESTfulAgentBean extends AbstractMethodExposingBean {

	@PostMapping("/reading/**")
	@Expose(scope = ActionScope.WEBSERVICE)
	//Will become /api/RESTfulAgent/add
	public int add(@QueryParam("x") int x, @QueryParam("y") int y) {
		return x + y;
	}

	@PostMapping("/writing")
	@Expose(scope = ActionScope.WEBSERVICE)
	public int test() {
		System.out.println("test");
		return 0;
	}
}
