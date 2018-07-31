package com.example.android.popularmovies.data.network;

import android.support.annotation.NonNull;

import com.example.android.popularmovies.data.database.MovieEntry;

import java.util.ArrayList;

public class MovieResponse {
    @NonNull
    private final ArrayList<MovieEntry> mMovieEntries;

    public MovieResponse(@NonNull final ArrayList<MovieEntry> movies) {
        mMovieEntries = movies;
    }

    public ArrayList<MovieEntry> getMovieEntries() {
        return mMovieEntries;
    }
}
