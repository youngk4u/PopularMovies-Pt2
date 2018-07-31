package com.example.android.popularmovies.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovies.data.MovieRepository;
import com.example.android.popularmovies.data.database.MovieEntry;

import org.json.JSONObject;

import java.util.List;

public class DetailActivityViewModel extends ViewModel {

    private final MovieRepository mRepository;

    // Movie the user is looking at
    private LiveData<MovieEntry> mMovie;

    public DetailActivityViewModel(MovieRepository repository, int id) {
        mRepository = repository;
        mMovie = mRepository.getMovieByLiveId(id);
    }

    public LiveData<MovieEntry> getMovie() {
        return mMovie;
    }

    public void updateMovie(MovieEntry movieEntry) {
        mRepository.updateMovieEntry(movieEntry);
    }

}
