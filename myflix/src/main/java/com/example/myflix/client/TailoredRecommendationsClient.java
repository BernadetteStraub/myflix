package com.example.myflix.client;

import com.example.myflix.model.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class TailoredRecommendationsClient {

    private final RestTemplate restTemplate;
    private final String url;

    public TailoredRecommendationsClient(RestTemplate restTemplate,
                                 @Value("${url.tailoredrecommendations}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public Movie[] getMovies(String searchTerm) throws UnsupportedEncodingException {
        // URLS have to be encoded because of special characters that are forbidden (like space)
        // Encoding throws UnsupportedEncodingException hence the throws keyword
        String encoded = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8.name());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("searchTerm", encoded);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Movie[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, Movie[].class);

        return response.getBody();
    }
}
