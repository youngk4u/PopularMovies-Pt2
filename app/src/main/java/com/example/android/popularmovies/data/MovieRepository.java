package com.example.android.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.data.database.MovieDao;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.network.MovieNetworkDataSource;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MovieRepository sInstance;
    private final MovieDao mMovieDao;
    private final MovieNetworkDataSource mMovieNetworkDataSource;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;


    private MovieRepository(MovieDao movieDao,
                            MovieNetworkDataSource movieNetworkDataSource,
                            AppExecutors executors) {
        mMovieDao = movieDao;
        mMovieNetworkDataSource = movieNetworkDataSource;
        mExecutors = executors;

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        MutableLiveData<ArrayList<MovieEntry>> networkData = mMovieNetworkDataSource.getMoviesList();

        networkData.observeForever(newMoviesFromNetwork -> {
            mExecutors.diskIO().execute(() -> {

                for (int i = 0; i < newMoviesFromNetwork.size(); i++) {
                    int mId = newMoviesFromNetwork.get(i).getId();
                    MovieEntry entry = mMovieDao.getMovieById(mId);
                    if (entry == null) {
                        mMovieDao.entryInsert(newMoviesFromNetwork.get(i));
                    }
                }
            });
        });
    }

    public synchronized static MovieRepository getInstance(MovieDao movieDao,
                                                           MovieNetworkDataSource movieNetworkDataSource,
                                                           AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieRepository(movieDao, movieNetworkDataSource, executors);
            }
        }
        return sInstance;
    }

    private synchronized void initializeData() {

        if (mInitialized) return;
        mInitialized = true;

        mExecutors.networkIO().execute(() -> {
            mMovieNetworkDataSource.fetchMovies(0);
        });
    }

    public LiveData<List<MovieEntry>> getMovieEntry() {
        initializeData();
        return mMovieDao.getMovieEntry();
    }

    public LiveData<MovieEntry> getMovieByLiveId(int id) {
        return mMovieDao.getMovieByLiveId(id);
    }

    public void updateMovieEntry(MovieEntry movieEntry) {
        mMovieDao.updateMovie(movieEntry);
    }

    public MovieNetworkDataSource getDataSource() {
        return mMovieNetworkDataSource;
    }
}
