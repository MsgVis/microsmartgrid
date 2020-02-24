package com.microsmartgrid.jiacAgent.controller;

import de.dailab.jiactng.agentcore.action.AbstractMethodExposingBean;
import de.dailab.jiactng.agentcore.action.scope.ActionScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;

@RestController
public class JiacAgentController extends AbstractMethodExposingBean {

	@PostMapping("/add")
	@Expose(scope = ActionScope.WEBSERVICE)
	//Will become /api/RESTfulAgent/add
	public int add(@QueryParam("x") int x, @QueryParam("y") int y) {
		return x + y;
	}

	@PostMapping("/test")
	@Expose(scope = ActionScope.WEBSERVICE)
	public int test() {
		System.out.println("test");
		return 0;
	}
}
