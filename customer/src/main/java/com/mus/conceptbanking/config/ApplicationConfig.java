package com.mus.conceptbanking.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Usman
 * @created 9/29/2022 - 12:38 AM
 * @project ob-cards
 */
@Configuration
@Getter
public class ApplicationConfig {


	@Value("${api.search.max_page_size:10}")
	public String maxPageSize;

}
