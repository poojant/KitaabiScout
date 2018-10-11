package com.example.poojan.kitaabiscout;

public class Book {
    private String authors;
    private Float average_rating;
    private String genre;
    private Long id;
    private String image_url;
    private String title;
    private Long ratings_count;

    public Book(String authors,Float average_rating, String genre, Long id
            , String image_url, String title, Long ratings_count){
        this.authors = authors;
        this.average_rating = average_rating;
        this.genre = genre;
        this.id = id;
        this.image_url = image_url;
        this.title = title;
        this.ratings_count = ratings_count;
    }

    public Book() {}

    public String getAuthors() {
        return authors;
    }

    public Float getAverage_rating() {
        return average_rating;
    }

    public String getGenre() {
        return genre;
    }

    public Long getId() {
        return id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getTitle() {
        return title;
    }

    public Long getRatings_count() {
        return ratings_count;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setAverage_rating(Float average_rating) {
        this.average_rating = average_rating;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRatings_count(Long ratings_count) {
        this.ratings_count = ratings_count;
    }
}
