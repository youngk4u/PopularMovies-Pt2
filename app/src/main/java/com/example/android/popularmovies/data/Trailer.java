package com.example.android.popularmovies.data;

public class Trailer {
    private String trailerKey;
    private String trailerName;

    public Trailer(String trailerKey, String trailerName) {
        this.trailerKey = trailerKey;
        this.trailerName = trailerName;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }
}
