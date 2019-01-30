package org.activiti.cloud.examples.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UntappdService {

	@Value("${untappd.api.client.id}")
	String clientId;

	@Value("${untappd.api.client.secret}")
	String clientSecret;

	private final RestTemplate restTemplate;

	public UntappdService(RestTemplateBuilder restTemplateBuilder) {
		restTemplate = restTemplateBuilder
				.rootUri("https://api.untappd.com/v4")
				.build();
	}

	@Cacheable("untappdSearchResults")
	public ObjectNode search(String query) {
//		curl -q "https://api.untappd.com/v4/search/beer?q=Pliny&client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET" | jq

		String url = addAuthorization("/search/beer")
				.queryParam("q", query).toUriString();

		JsonNode result = restTemplate.getForObject(url, JsonNode.class);

		ObjectNode responseResult = (ObjectNode) result.path("response");

		return responseResult;
	}

	private UriComponentsBuilder addAuthorization(String url) {
		return UriComponentsBuilder.fromUriString(url)
			.queryParam("client_id", clientId)
			.queryParam("client_secret", clientSecret);
	}

}
