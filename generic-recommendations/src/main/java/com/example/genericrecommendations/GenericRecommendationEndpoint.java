package com.example.genericrecommendations;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/generic-recommendations")
public class GenericRecommendationEndpoint {

    private final GenericRecommendationClient genericClient;

    public GenericRecommendationEndpoint(GenericRecommendationClient genericClient) {
        this.genericClient = genericClient;
    }

    @GetMapping
    public ResponseEntity<List<Recommendation>> getPopularCategories(){
        return ResponseEntity.ok(genericClient.getRecommendations());
    }

}
