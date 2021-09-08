package com.example.myflix.endpoint;

import com.example.myflix.client.GenericRecommendationsClient;
import com.example.myflix.client.TailoredRecommendationsClient;
import com.example.myflix.model.Movie;
import com.example.myflix.model.MyflixResponse;
import com.example.myflix.model.Recommendation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/myflix")
public class MyflixEndpoint {

    private final GenericRecommendationsClient genericRecommendationsClient;
    private final TailoredRecommendationsClient tailoredRecommendationsClient;

    public MyflixEndpoint(GenericRecommendationsClient genericRecommendationsClient, TailoredRecommendationsClient tailoredRecommendationsClient) {
        this.genericRecommendationsClient = genericRecommendationsClient;
        this.tailoredRecommendationsClient = tailoredRecommendationsClient;
    }

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_VIEWER"})
    // @RequestParam to extract query parameters from the request
    // We can configure our @RequestParam to be optional with the required attribute
        // URLS have to be encoded because of special characters that are forbidden (like space)
        // Encoding throws UnsupportedEncodingException hence the throws keyword (ONLY IN TAILORED CASE)
    MyflixResponse getRecommendations(@RequestParam(required = false) String searchTerm) throws UnsupportedEncodingException {
       List<Recommendation> genericRecommendations = List.of(genericRecommendationsClient.getMovies());

        if(searchTerm != null && !searchTerm.isEmpty()){
            List<Movie>tailoredRecommendations = List.of(tailoredRecommendationsClient.getMovies(searchTerm));
            return new MyflixResponse(Collections.emptyList(), tailoredRecommendations);
        }

        return new MyflixResponse(genericRecommendations, Collections.emptyList());
    }

}
