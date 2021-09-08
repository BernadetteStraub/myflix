package com.example.genericrecommendations;


public class Genre {
    String description;
    String endpoint;

    public Genre(){}

    public Genre(String description, String endpoint) {
        this.description = description;
        this.endpoint = endpoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
