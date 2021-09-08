package com.example.myflix.model;

import java.util.List;

public class MyflixResponse {
    private List<Recommendation> genericRecommendations;
    private List<Movie> tailoredRecommendations;

    public MyflixResponse(){}

    public MyflixResponse(List<Recommendation> genericRecommendations, List<Movie> tailoredRecommendations) {
        this.genericRecommendations = genericRecommendations;
        this.tailoredRecommendations = tailoredRecommendations;
    }

    public List<Recommendation> getGenericRecommendations() {
        return genericRecommendations;
    }

    public void setGenericRecommendations(List<Recommendation> genericRecommendations) {
        this.genericRecommendations = genericRecommendations;
    }

    public List<Movie> getTailoredRecommendations() {
        return tailoredRecommendations;
    }

    public void setTailoredRecommendations(List<Movie> tailoredRecommendations) {
        this.tailoredRecommendations = tailoredRecommendations;
    }
}
