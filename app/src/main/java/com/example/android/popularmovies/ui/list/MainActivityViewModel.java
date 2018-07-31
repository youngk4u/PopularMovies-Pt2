package com.example.android.popularmovies.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovies.data.MovieRepository;
import com.example.android.popularmovies.data.database.MovieEntry;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final MovieRepository movieRepository;
    private final LiveData<List<MovieEntry>> mMovie;

    public MainActivityViewModel(MovieRepository repository) {
        movieRepository = repository;
        mMovie = movieRepository.getMovieEntry();
    }

    public LiveData<List<MovieEntry>> getMovies() {
        return mMovie;
    }
}
