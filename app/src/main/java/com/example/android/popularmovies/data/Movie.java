package com.example.android.popularmovies.data;

import java.io.Serializable;

public class Movie implements Serializable {
    private String originalTitle;
    private String title;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private double userRating;
    private double popularity;

    /** Constructor */
    public Movie(String originalTitle, String title, String posterPath, String overview,
                 String releaseDate, double userRating, double popularity) {
        this.originalTitle = originalTitle;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.popularity = popularity;
    }

    /** Getters and Setters */

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

}
