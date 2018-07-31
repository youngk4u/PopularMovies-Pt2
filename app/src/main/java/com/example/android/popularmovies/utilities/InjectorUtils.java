package com.example.android.popularmovies.utilities;

import android.arch.persistence.room.Database;
import android.content.Context;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.data.MovieRepository;
import com.example.android.popularmovies.data.database.MovieDatabase;
import com.example.android.popularmovies.data.network.MovieNetworkDataSource;
import com.example.android.popularmovies.ui.detail.DetailViewModelFactory;
import com.example.android.popularmovies.ui.list.MainViewModelFactory;

import java.util.Date;

public class InjectorUtils {
    public static MovieRepository provideRepository(Context context) {
        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MovieNetworkDataSource networkDataSource =
                MovieNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return MovieRepository.getInstance(database.movieDao(), networkDataSource, executors);
    }

    public static MovieNetworkDataSource provideNetworkDataSource(Context context) {
        provideRepository(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return MovieNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(MovieRepository repository) {
        return new MainViewModelFactory(repository);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, int id) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new DetailViewModelFactory(repository, id);
    }
}
