package com.mus.composite.proxyunit.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.logging.Level.FINE;

/**
 * @author Usman
 * @created 10/30/2022 - 7:22 PM
 * @project MyConceptBanking
 */
@Slf4j
@IntegrationService
public class CustomerIntegrationServiceImpl implements CustomerIntegrationService {
	@Autowired WebClient.Builder webClientBuilder;
	@Value("${app.customer-service.uri}") String serviceUri;

	@Override
	public Flux<CustomerDto> getAll() {
		String uri = String.format("%s/api/customer/", serviceUri);
		log.info("proxying get all customer to uri [{}]", uri);
		Flux<CustomerDto> flux = webClientBuilder.build()
			.get()
			.uri(uri)
			.retrieve()
			.bodyToFlux(CustomerDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Flux.empty());
		return flux;
	}

	@Override
	public Flux<CustomerDto> search(CustomerDto dto, int pageNo, int pageSize) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.SEARCH)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CUSTOMER.toString())
			.build();
				String uri = String.format("%s/api/customer/search/%s/%s", serviceUri, pageNo, pageSize);
//		String uri = String.format("http://localhost:7081/api/customer/search/%s/%s",pageNo, pageSize);
		log.info("proxying search all customer with input details [{}] to uri [{}]", dto, uri);

		return webClientBuilder.build()
			.post()
			.uri(uri)
			.bodyValue(dto)
			.retrieve()
			.bodyToMono(SearchApiResponse.class)
			.map(SearchApiResponse::getData)
			.map(Slice::getContent)
			.flatMapMany(Flux::fromIterable)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> {
			log.error("Error occurred while searching customers with error message [{}]", throwable.getMessage(), throwable);
			return Flux.empty();
		});
	}

	@Override
	public Mono<CustomerDto> get(String uuid) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.POST)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CUSTOMER.toString())
			.build();
		//		class GetMonoApiResponse extends ApiResponse<CustomerDto> {}
				String uri = String.format("%s/api/customer/%s", serviceUri, uuid);
//		String uri = String.format("http://localhost:7081/api/customer/%s", uuid);
		log.info("proxying get customer by uuid [{}] to uri [{}]", uuid, uri);
		Mono<GetApiResponse> getApiResponseMono = webClientBuilder.build()
			.get()
			.uri(uri)
			.accept(MediaType.APPLICATION_JSON)
			.exchangeToMono(clientResponse -> clientResponse.bodyToMono(GetApiResponse.class));
		getApiResponseMono.subscribe(getApiResponse -> log.debug("customerDto [{}]", getApiResponse));
		return getApiResponseMono.map(GetApiResponse::getData)
			.log(log.getName(), FINE)
			.onErrorMap(Exception.class,
				throwable -> new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), trackCode, throwable.getMessage()));
	}

	@Override
	public Mono<CustomerDto> save(CustomerDto dto) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.POST)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CUSTOMER.toString())
			.build();
				String uri = String.format("%s/api/customer", serviceUri);
//		String uri = String.format("http://localhost:7081/api/customer/", serviceUri);
		log.info("proxying get all customer to uri [{}] with payload [{}]", dto, uri);
		Mono<CustomerDto> mono = webClientBuilder.build()
			.post()
			.uri(uri)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(dto)
			.retrieve()
			.bodyToMono(SaveApiResponse.class)
			.map(SaveApiResponse::getData)
			.log(log.getName(), FINE)
			.onErrorMap(Exception.class, exception ->
				new ApplicationException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, exception.getMessage()));
		return mono;
	}

	@Override
	public Mono<CustomerDto> update(String uuid, CustomerDto dto) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.PUT)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CUSTOMER.toString())
			.build();
		String uri = String.format("%s/api/customer/%s", serviceUri, uuid);
		log.info("proxying update customer to uri [{}] with payload [{}]", uri, dto);
		Mono<CustomerDto> mono = webClientBuilder.build()
			.put()
			.uri(uri)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(dto)
			.retrieve()
			.bodyToMono(CustomerDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(
				new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), trackCode, "Unable to update customer dto.")));
		return mono;
	}

	@Override
	public Mono<CustomerDto> partialUpdate(CustomerDto dto) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.PATCH)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CUSTOMER.toString())
			.build();
		String uri = String.format("%s/api/customer", serviceUri);
		log.info("proxying partial update customer to uri [{}] with payload [{}]", uri, dto);
		return webClientBuilder.build()
			.patch()
			.uri(uri)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(dto)
			.retrieve()
			.bodyToMono(CustomerDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(throwable -> Mono.error(
				new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), trackCode, "Unable to " + "partial update customer dto.")));
	}

	@Override
	public void delete(String uuid) {
		TrackCode trackCode = TrackCode.with(ApiType.AGGREGATE)
			.with(RequestType.DELETE)
			.with(LayerType.AGGREGATION_LAYER)
			.with(EntityType.CUSTOMER.toString())
			.build();
		String uri = String.format("%s/api/customer/%s", serviceUri, uuid);
		log.info("proxying partial update customer with uuid [{}] to uri [{}]", uuid, uri);
		webClientBuilder.build()
			.delete()
			.uri(uri)
			.retrieve()
			.bodyToMono(CustomerDto.class)
			.log(log.getName(), FINE)
			.onErrorResume(
				throwable -> Mono.error(new ApplicationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), trackCode,
					"Unable to " + "delete.")));

	}

	@Override
	public Mono<Health> getHealth() {
				String healthUri = "/api/customer/actuator/health";
//		String healthUri = "http://localhost:7081/api/customer/actuator/health";
		log.debug("Will call the Health API on URL: {}", healthUri);
		return webClientBuilder.build()
			.get()
			.uri(healthUri)
			.retrieve()
			.bodyToMono(String.class)
			.map(s -> new Health.Builder().up()
				.build())
			.onErrorResume(ex -> Mono.just(new Health.Builder().down(ex)
				.build()))
			.log(log.getName(), FINE);
	}

}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
class SaveApiResponse {
	private ErrorInfo errorInfo;
	private CustomerDto Data;

	public SaveApiResponse(ErrorInfo errorInfo) {
		this.errorInfo = errorInfo;
	}

	public SaveApiResponse(CustomerDto data) {
		Data = data;
	}
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
class GetApiResponse {
	private ErrorInfo errorInfo;
	private CustomerDto Data;

	public GetApiResponse(ErrorInfo errorInfo) {
		this.errorInfo = errorInfo;
	}

	public GetApiResponse(CustomerDto data) {
		Data = data;
	}
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SearchApiResponse {
	@JsonProperty("errorInfo")
	private ErrorInfo errorInfo;
	@JsonProperty("Data")
	private Page<CustomerDto> Data;

	public SearchApiResponse(ErrorInfo errorInfo) {
		this.errorInfo = errorInfo;
	}

	public SearchApiResponse(Page<CustomerDto> data) {
		Data = data;
	}
}

