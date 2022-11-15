package com.mus.conceptbanking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * @author Usman
 * @created 10/10/2022 - 11:21 PM
 * @project ob-cards
 */
@EnableJpaAuditing
@Configuration
public class AuditingConfig {


	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of("System");
		/*return () -> Optional.ofNullable(SecurityContextHolder.getContext())
			.map(SecurityContext::getAuthentication)
			.map(authentication -> (UserDto) authentication.getPrincipal())
			.map(UserDto::getUuid)
			.filter(StringUtils::hasLength)
			.or(() -> Optional.of("System"));*/
	}
}
