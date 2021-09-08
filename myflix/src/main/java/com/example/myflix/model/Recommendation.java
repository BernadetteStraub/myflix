package com.example.myflix.model;

import java.util.List;

public class Recommendation {
    private String genre;
    private List<Movie> movies;

    public Recommendation(){}

    public Recommendation(String genre, List<Movie> movies) {
        this.genre = genre;
        this.movies = movies;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
