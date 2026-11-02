package com.reeltrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Service
public class TmdbService {
    @Value("${tmdb.api-key:}") private String apiKey;
    private final RestClient client = RestClient.builder().baseUrl("https://api.themoviedb.org/3").build();

    public Object get(String path, Map<String, String> params) {
        if (apiKey == null || apiKey.isBlank()) throw new IllegalStateException("TMDB_API_KEY is not configured yet.");
        var request = client.get().uri(uri -> {
            var b = uri.path(path).queryParam("api_key", apiKey);
            params.forEach(b::queryParam);
            return b.build();
        });
        return request.retrieve().body(Object.class);
    }
}
