package com.example.genericrecommendations;

import java.util.List;

public class PopularCategories {

    List<Genre> genres;


    public PopularCategories(){}

    public PopularCategories(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
