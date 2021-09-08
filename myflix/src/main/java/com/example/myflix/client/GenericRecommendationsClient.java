package com.example.myflix.client;

import com.example.myflix.model.Recommendation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class GenericRecommendationsClient {

    private final RestTemplate restTemplate;
    private final String url;

    public GenericRecommendationsClient(RestTemplate restTemplate,
                                        @Value("${url.recommendations}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public Recommendation[] getMovies(){
        return restTemplate.getForObject(url, Recommendation[].class);
    }
}
