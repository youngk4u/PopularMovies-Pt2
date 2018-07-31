package com.example.android.popularmovies.ui.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.data.MovieRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MovieRepository mRepository;

    public MainViewModelFactory(MovieRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(mRepository);
    }
}
