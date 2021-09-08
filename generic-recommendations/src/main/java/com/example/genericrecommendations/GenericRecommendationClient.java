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

    public List<Recommendation> getRecommendations(){ //MAIN method
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

    // IMPORTANT METHOD
    // Requests that return movie ids return them as /title/ttxxxxxx
    // This format is not accepted by IMDB API when using the tconst param
    // tconst accepts only ttxxxxx format
    private String extractMovieId(String id){
        return id.substring(id.indexOf("tt"));
    }

    private Movie getMovieById(String id){

        String movieDetailUrl = "/title/get-details";
        String fullUrl = getFullHost() + movieDetailUrl;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(fullUrl)
                // Add query parameter
                // tconst is the request param for a movie Id
                // tconst is the key like searchTerm in our requests
                // "tconst" is from IMDB <params: {tconst:>
                .queryParam("tconst", extractMovieId(id));

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Movie> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, Movie.class);
        return response.getBody();
    }

    private List<String> getFiveMovieIdsByCategory(String categoryEndpoint){ //Genre Endpoint
        //// From the IMDB API specification
        String popularMoviesByGenreEndpoint = "/title/get-popular-movies-by-genre";
        String fullUrl = getFullHost() + popularMoviesByGenreEndpoint;

        // Boilerplate code to add request parameters to a request e.g. (/etc?param=value)
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(fullUrl)
                // Add query parameter, in our case the endpoint of the genre objects
                // "genre" is from IMDB <params: {genre:>
                // Query parameters are added to the url after the ? mark, while a path parameter is part of the regular URL.
                .queryParam("genre", categoryEndpoint);

        //Create a UriComponentsBuilder with one of the static factory methods (such as fromUri(URI))
        //Set the various URI components through the respective methods like queryParam(String, Object)

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        // Returns a list of Movie Ids (STRINGS) for the category/genre that we provided as request param
        ResponseEntity<String[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, String[].class);

        // We limit the amount of ids to an arbitrary value to reduce the amount of requests
        // We have to use .getBody() becuase its packed in ResponseEntity
        return Arrays.stream(response.getBody()).limit(1).collect(Collectors.toList());
    }


    private PopularCategories getPopularCategories(){
        // From the IMDB API specification
        String popularCategoriesEndpoint = "/title/list-popular-genres/";
        // https:// + apiHost + endpoint
        String fullUrl = getFullHost() + popularCategoriesEndpoint;

        // Boilerplate code
        // Represents an HTTP request or response entity, consisting of headers and body.
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        // Generic !exchange! method that allows us to specify !request-params! and headers better than getForObject()
        // Exchange returns ResponseEntity ALWAYS! To get the data out of it, you use .getBody() -> Like Optional<>
        ResponseEntity<PopularCategories> responseEntity = restTemplate.exchange(fullUrl, HttpMethod.GET, httpEntity, PopularCategories.class);
        return responseEntity.getBody();
        //A GET request will be performed to the given URL sending the HTTP headers that are wrapped in the HttpEntity instance.
        //The response entity will be returned as a PopularCategories wrapped into a ResponseEntity instance.
    }

    //Helper method that concatenates apiHost with https://
    private String getFullHost(){
        return "https://" + apiHost;
    }

    private void setHeaders(){
        //Boilerplate code to set headers
        //Two of these are custom rapidApi headers needed for requests
        this.headers = new HttpHeaders();
        headers.set("x-rapidapi-host", apiHost); //(K,V)
        headers.set("x-rapidapi-key", apiKey);
        // Standard header in HTTP Requests
        //The Accept request HTTP header advertises which content types the client is able to understand.
        headers.set("Accept", "application/json");
    }
}

