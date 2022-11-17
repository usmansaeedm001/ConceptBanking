package com.mus.composite.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Usman
 * @created 11/18/2022 - 1:47 AM
 * @project MyConceptBanking
 */

@RestController
@RequestMapping("/api/checkRefreshScope")
public class RefreshScopeController {

	@Value("${app.refresh.scope.property:default}")
	private String property;

	@GetMapping("/property")
	public String getProperty(){
		return property;
	}

}
