package com.example.genericrecommendations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenericRecommendationClient {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String apiHost;
    private HttpHeaders headers;

    public GenericRecommendationClient(RestTemplate restTemplate,
                                       @Value("${api-key}")String apiKey,
                                       @Value("${api-host}")String apiHost) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.apiHost = apiHost;
        this.setHeaders();
    }

    public List<Recommendation> getRecommendations(){
        List<Recommendation> moviesByCategories = getPopularCategories().getGenres().stream()
                .limit(1)
                .map(genre ->
                        new Recommendation(genre.getDescription(),
                                getFiveMovieIdsByCategory(genre.getEndpoint())
                                        .stream()
                                        .map(id -> getMovieById(id)).collect(Collectors.toList())))
                .collect(Collectors.toList());

        return moviesByCategories;
    }

    // This format is not accepted by IMDB API when using the tconst param
    // tconst accepts only ttxxxxx format
    private String extractMovieId(String id){
        return id.substring(id.indexOf("tt"));
    }

    private Movie getMovieById(String id){

        String movieDetailUrl = "/title/get-details";
        String fullUrl = getFullHost() + movieDetailUrl;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(fullUrl)
                // tconst is the request param for a movie Id
                .queryParam("tconst", extractMovieId(id));

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Movie> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, Movie.class);
        return response.getBody();
    }

    private List<String> getFiveMovieIdsByCategory(String categoryEndpoint){
        // From the IMDB API specification
        String popularMoviesByGenreEndpoint = "/title/get-popular-movies-by-genre";
        String fullUrl = getFullHost() + popularMoviesByGenreEndpoint;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(fullUrl)
                .queryParam("genre", categoryEndpoint);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, String[].class);

        // We limit the amount of ids to an arbitrary value to reduce the amount of requests
        return Arrays.stream(response.getBody()).limit(1).collect(Collectors.toList());
    }


    private PopularCategories getPopularCategories(){
        // From the IMDB API specification
        String popularCategoriesEndpoint = "/title/list-popular-genres/";

        String fullUrl = getFullHost() + popularCategoriesEndpoint;


        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<PopularCategories> responseEntity = restTemplate.exchange(fullUrl, HttpMethod.GET, httpEntity, PopularCategories.class);
        return responseEntity.getBody();
    }

    private String getFullHost(){
        return "https://" + apiHost;
    }

    private void setHeaders(){
        //Two of these are custom rapidApi headers needed for requests
        this.headers = new HttpHeaders();
        headers.set("x-rapidapi-host", apiHost);
        headers.set("x-rapidapi-key", apiKey);
        headers.set("Accept", "application/json");
    }
}

