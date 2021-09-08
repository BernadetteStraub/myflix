package com.example.tailoredrecommendations;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tailored-recommendations")
public class TailoredRecommendationEndpoint {

    private final TailoredRecommendationClient tailoredClient;

    public TailoredRecommendationEndpoint(TailoredRecommendationClient tailoredClient) {
        this.tailoredClient = tailoredClient;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getPopularCategories(@RequestParam String searchTerm){
        return ResponseEntity.ok(tailoredClient.getTailoredRecommendation(searchTerm));
    }

}
