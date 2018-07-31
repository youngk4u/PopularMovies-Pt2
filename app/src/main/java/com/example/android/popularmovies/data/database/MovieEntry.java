package com.example.android.popularmovies.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "movie")
public class MovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int dbId = 0;
    private int id;
    private String originalTitle;
    private String title;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private double userRating;
    private double popularity;

    private int favored;

    /** Original Constructor */
    @Ignore
    public MovieEntry(String originalTitle, String title, String posterPath, String overview,
                      String releaseDate, double userRating, double popularity, int favored) {
        this.originalTitle = originalTitle;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.popularity = popularity;
        this.favored = favored;
    }

    /** Contructor used by Room **/
    public MovieEntry(int id, String originalTitle, String title, String posterPath, String overview,
                      String releaseDate, double userRating, double popularity, int favored) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.popularity = popularity;
        this.favored = favored;
    }

    /** Getters and Setters */
    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public int getId() {
        return id;
    }

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

    public void setFavored(int favored) {
        this.favored = favored;
    }

    public int getFavored() {
        return favored;
    }
}
