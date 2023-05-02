package com.mus.composite.proxyunit.account;

import com.mus.composite.enums.EntityType;
import com.mus.composite.enums.ErrorCode;
import com.mus.framework.annotation.IntegrationService;
import com.mus.framework.dto.EnumerationWrapper;
import com.mus.framework.dto.ErrorInfo;
import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.exception.ApplicationException;
import com.mus.framework.handler.TrackCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok .*;

import java.util.List;

import static java.util.logging.Level.FINE;

/**
 * @author Usman
 * @created 10/31/2022 - 1:39 AM
 * @project MyConceptBanking
 */
@Slf4j
@IntegrationService
public class AccountIntegrationServiceImpl implements AccountIntegrationService {
	@Autowired WebClient.Builder loadBalancedWebClientBuilder;
	@Value("${app.account-service.uri}") String serviceUri;

	@Override
	public Flux<AccountDto> getAll() {
		String uri = String.format("%s/api/account", serviceUri);
		Flux<AccountDto> flux = loadBalancedWebClientBuilder.build()
			.get()
			.uri(uri)
			.retrieve()
			.bodyToFlux(AccountDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Flux.empty());
		return flux;
	}

	@Override
	public Flux<AccountDto> search(AccountDto dto, int pageNo, int pageSize) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.SEARCH)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.ACCOUNT.toString())
			.build();
		String uri = String.format("%s/api/account/%s/%s", serviceUri, pageNo, pageSize);
		Flux<AccountDto> pageMono = loadBalancedWebClientBuilder.build()
			.get()
			.uri(uri)
			.retrieve()
			.bodyToMono(SearchApiResponse.class)
			.map(SearchApiResponse::getData)
			.map(Slice::getContent)
			.flatMapMany(Flux::fromIterable)
			.log(log.getName(), FINE)
			.onErrorMap(throwable -> new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), trackCode, throwable.getMessage()));
		return pageMono;
	}

	@Override
	public Flux<AccountDto> getAllByCustomerUuid(String uuid) {
		String uri = String.format("%s/api/account/customer/%s", serviceUri, uuid);
		Flux<AccountDto> flux = loadBalancedWebClientBuilder.build()
			.get()
			.uri(uri)
			.retrieve()
			.bodyToFlux(new ParameterizedTypeReference<AccountDto>() {})
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Flux.empty());
		return flux;
	}

	@Override
	public Mono<AccountDto> get(String uuid) {
		String uri = String.format("%s/api/account/%s", serviceUri, uuid);
		Mono<AccountDto> mono = loadBalancedWebClientBuilder.build()
			.get()
			.uri(uri)
			.retrieve()
			.bodyToMono(AccountDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.empty());
		return mono;
	}

	@Override
	public Mono<AccountDto> save(AccountDto dto) {
		TrackCode errorCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.POST)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.ACCOUNT.toString())
			.build();
		String uri = String.format("%s/api/account", serviceUri);
		Mono<AccountDto> mono = loadBalancedWebClientBuilder.build()
			.post()
			.uri(uri)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(dto)
			.retrieve()
			.bodyToMono(AccountDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), errorCode, "Unable to save customer dto.")));
		return mono;
	}

	@Override
	public Mono<AccountDto> update(String uuid, AccountDto dto) {
		TrackCode errorCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.PUT)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.ACCOUNT.toString())
			.build();
		String uri = String.format("%s/api/account/%s", serviceUri, uuid);
		Mono<AccountDto> mono = loadBalancedWebClientBuilder.build()
			.put()
			.uri(uri)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(dto)
			.retrieve()
			.bodyToMono(AccountDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), errorCode, "Unable to update customer dto.")));
		return mono;
	}

	@Override
	public Mono<AccountDto> partialUpdate(AccountDto dto) {
		TrackCode errorCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.PATCH)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.ACCOUNT.toString())
			.build();
		String uri = String.format("%s/api/account", serviceUri);
		Mono<AccountDto> mono = loadBalancedWebClientBuilder.build()
			.patch()
			.uri(uri)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(dto)
			.retrieve()
			.bodyToMono(AccountDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), errorCode, "Unable to partial update customer dto.")));
		return mono;
	}

	@Override
	public void delete(String uuid) {
		TrackCode errorCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.DELETE)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.ACCOUNT.toString())
			.build();
		String uri = String.format("%s/api/account/%s", serviceUri, uuid);
		loadBalancedWebClientBuilder.build()
			.delete()
			.uri(uri)
			.retrieve()
			.bodyToMono(AccountDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), errorCode, "Unable to delete.")));
	}

	@Override
	public void deleteAllByCustomerUuid(String uuid) {
		TrackCode errorCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.DELETE)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.ACCOUNT.toString())
			.build();
		String uri = String.format("%s/api/account/account/%s", serviceUri, uuid);
		loadBalancedWebClientBuilder.build()
			.delete()
			.uri(uri)
			.retrieve()
			.bodyToMono(AccountDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), errorCode, "Unable to delete.")));
	}

	@Override
	public Mono<Health> getHealth() {
		String healthUri = "/api/account/actuator/health";
		log.debug("Will call the Health API on URL: {}", healthUri);
		return loadBalancedWebClientBuilder.build().get().uri(healthUri).retrieve().bodyToMono(String.class).map(s -> new Health.Builder().up().build())
			.onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build())).log(log.getName(), FINE);
	}



	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	@Builder(toBuilder = true)
	private static class SearchApiResponse {
		private ErrorInfo errorInfo;
		private Page<AccountDto> Data;

		public SearchApiResponse(ErrorInfo errorInfo) {
			this.errorInfo = errorInfo;
		}

		public SearchApiResponse(Page<AccountDto> data) {
			Data = data;
		}
	}
}

