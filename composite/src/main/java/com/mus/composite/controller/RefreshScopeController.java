package com.mus.composite.controller;

import com.mus.composite.config.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
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


	@Autowired ApplicationConfiguration applicationConfiguration;

	@GetMapping("/property")
	public String getProperty(){
		return applicationConfiguration.refreshScopeProperty;
	}

}
