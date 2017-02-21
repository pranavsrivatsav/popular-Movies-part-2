package com.example.pranavsrivatsav.popularmovies;

/**
 * Created by Pranav Srivatsav on 1/7/2017.
 */

public class Movie {
    private String MovieId;
    private String Title;
    private Double Rating;
    private String Synopsis;
    private String ReleaseDate;
    private String Year;
    private String Poster;

    public Movie(String movieId, String title, Double rating, String synopsis, String releaseDate, String year, String poster) {
        MovieId=movieId;
        Title = title;
        Rating = rating;
        Synopsis = synopsis;
        ReleaseDate = releaseDate;
        Year = year;
        Poster = poster;
    }

    public String getTitle() {
        return Title;
    }

    public Double getRating() {
        return Rating;
    }

    public String getSynopsis() {
        return Synopsis;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public String getYear() {
        return Year;
    }

    public String getPoster() {
        return Poster;
    }

    public String getMovieId(){
        return MovieId;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "MovieId='" + MovieId + '\'' +
                ", Title='" + Title + '\'' +
                ", Rating=" + Rating +
                ", Synopsis='" + Synopsis + '\'' +
                ", ReleaseDate='" + ReleaseDate + '\'' +
                ", Year='" + Year + '\'' +
                ", Poster='" + Poster + '\'' +
                '}';
    }
}
