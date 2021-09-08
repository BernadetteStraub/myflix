package com.example.tailoredrecommendations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TailoredRecommendationClient {


    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String apiHost;
    private HttpHeaders headers;

    public TailoredRecommendationClient(RestTemplate restTemplate,
                                        @Value("${api-key}")String apiKey,
                                        @Value("${api-host}")String apiHost) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.apiHost = apiHost;
        this.setHeaders();
    }

    private String extractMovieId(String id){
        return id.substring(id.indexOf("tt"));
    }

    public List<Movie> getTailoredRecommendation(String searchTerm){
        SearchResponse searchResponse = this.searchMovie(searchTerm);

        if(searchResponse == null || searchResponse.getResults() == null  || searchResponse.getResults().isEmpty()){
            return Collections.emptyList();
        }

        List<String> moreLikeThisIds = getMoreLikeThisIds(searchResponse.getResults().get(0).getId()); //get first one

        return moreLikeThisIds.stream()
                .map(id -> this.getMovieById(id))
                .collect(Collectors.toList());
    }


    private List<String> getMoreLikeThisIds(String movieId){
        String moreLikeThisUrl = "/title/get-more-like-this";
        String fullUrl = getFullHost() + moreLikeThisUrl;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(fullUrl)
                .queryParam("tconst", extractMovieId(movieId));

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, String[].class);
        return (response.getBody()!= null) ? Arrays.stream(response.getBody()).limit(1).collect(Collectors.toList()) : Collections.emptyList();
    }

    private SearchResponse searchMovie(String searchTerm){
        String movieSearchUrl = "/title/find";
        String fullUrl = getFullHost() + movieSearchUrl;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(fullUrl)
                .queryParam("q", searchTerm.trim());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<SearchResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, SearchResponse.class);
        return response.getBody();
    }

    private Movie getMovieById(String id){
        String movieDetailUrl = "/title/get-details";
        String fullUrl = getFullHost() + movieDetailUrl;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(fullUrl)
                .queryParam("tconst", extractMovieId(id));

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Movie> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, Movie.class);
        return response.getBody();
    }

    private String getFullHost(){
        return "https://" + apiHost;
    }

    private void setHeaders(){
        this.headers = new HttpHeaders();
        headers.set("x-rapidapi-host", apiHost);
        headers.set("x-rapidapi-key", apiKey);
        headers.set("Accept", "application/json");
    }

}
