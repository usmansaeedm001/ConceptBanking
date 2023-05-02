package com.mus.composite.proxyunit.card;

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

import java.lang.reflect.Type;
import java.util.List;

import static java.util.logging.Level.ALL;
import static java.util.logging.Level.FINE;

/**
 * @author Usman
 * @created 10/31/2022 - 1:10 AM
 * @project MyConceptBanking
 */
@Slf4j
@IntegrationService
public class CardIntegrationServiceImpl implements CardIntegrationService {
	@Autowired WebClient.Builder loadBalancedWebClientBuilder;
	@Value("${app.card-service.uri}") String serviceUri;

	@Override
	public Flux<CardDto> getAll() {
		String uri = String.format("%s/api/card", serviceUri);
		Flux<CardDto> flux = loadBalancedWebClientBuilder.build()
			.get()
			.uri(uri)
			.retrieve()
			.bodyToFlux(CardDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Flux.empty());
		return flux;
	}

	@Override
	public Flux<CardDto> search(CardDto dto, int pageNo, int pageSize) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.SEARCH)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CARD.toString())
			.build();
		String uri = String.format("%s/api/card/search/%s/%s", serviceUri, pageNo, pageSize);
		Flux<CardDto> pageMono = loadBalancedWebClientBuilder.build()
			.post()
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
	public Flux<CardDto> getAllByCustomerUuid(String uuid) {
		String uri = String.format("%s/api/card/customer/%s", serviceUri, uuid);
		Flux<CardDto> flux = loadBalancedWebClientBuilder.build()
			.get()
			.uri(uri)
			.retrieve()
			.bodyToFlux(new ParameterizedTypeReference<CardDto>() {})
			.log(log.getName(), ALL)
			.onErrorResume(throwable -> Flux.empty());
		return flux;
	}

	@Override
	public Mono<CardDto> get(String uuid) {
		String uri = String.format("%s/api/card/%s", serviceUri, uuid);
		Mono<CardDto> mono = loadBalancedWebClientBuilder.build()
			.get()
			.uri(uri)
			.retrieve()
			.bodyToMono(CardDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.empty());
		return mono;
	}

	@Override
	public Mono<CardDto> save(CardDto dto) {
		TrackCode errorCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.POST)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CARD.toString())
			.build();
		String uri = String.format("%s/api/card", serviceUri);
		Mono<CardDto> mono = loadBalancedWebClientBuilder.build()
			.post()
			.uri(uri)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(dto)
			.retrieve()
			.bodyToMono(CardDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), errorCode, "Unable to save customer dto.")));
		return mono;
	}

	@Override
	public Mono<CardDto> update(String uuid, CardDto dto) {
		TrackCode errorCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.PUT)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CARD.toString())
			.build();
		String uri = String.format("%s/api/card/%s", serviceUri, uuid);
		Mono<CardDto> mono = loadBalancedWebClientBuilder.build()
			.put()
			.uri(uri)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(dto)
			.retrieve()
			.bodyToMono(CardDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), errorCode,
				"Unable to update customer dto.")));
		return mono;
	}

	@Override
	public Mono<CardDto> partialUpdate(CardDto dto) {
		TrackCode errorCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.PATCH)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CARD.toString())
			.build();
		String uri = String.format("%s/api/card", serviceUri);
		Mono<CardDto> mono = loadBalancedWebClientBuilder.build()
			.patch()
			.uri(uri)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(dto)
			.retrieve()
			.bodyToMono(CardDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), errorCode, "Unable to partial update customer dto.")));
		return mono;
	}

	@Override
	public void delete(String uuid) {
		TrackCode errorCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.DELETE)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CARD.toString())
			.build();
		String uri = String.format("%s/api/card/%s", serviceUri, uuid);
		loadBalancedWebClientBuilder.build()
			.delete()
			.uri(uri)
			.retrieve()
			.bodyToMono(CardDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), errorCode, "Unable to delete.")));
	}

	@Override
	public void deleteAllByCustomerUuid(String uuid) {
		TrackCode errorCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.DELETE)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CARD.toString())
			.build();
		String uri = String.format("%s/api/card/card/%s", serviceUri, uuid);
		loadBalancedWebClientBuilder.build()
			.delete()
			.uri(uri)
			.retrieve()
			.bodyToMono(CardDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), errorCode, "Unable to delete.")));
	}

	@Override
	public Mono<Health> getHealth() {
		String healthUri = "/api/card/actuator/health";
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
		private Page<CardDto> Data;

		public SearchApiResponse(ErrorInfo errorInfo) {
			this.errorInfo = errorInfo;
		}

		public SearchApiResponse(Page<CardDto> data) {
			Data = data;
		}
	}
}

