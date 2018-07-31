package com.example.android.popularmovies.data.network;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.content.Context;
import android.content.Intent;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.data.MovieRepository;
import com.example.android.popularmovies.data.database.MovieDatabase;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.ui.list.MainActivity;
import com.example.android.popularmovies.ui.list.MovieAdapter;
import com.example.android.popularmovies.utilities.DataParsingUtils;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MovieNetworkDataSource {

    private static final Object LOCK = new Object();
    private static MovieNetworkDataSource sInstance;
    private final Context mContext;
    private static URL mUrl;
    private final AppExecutors mExecutors;
    private MovieResponse response;

    private final MutableLiveData<ArrayList<MovieEntry>> mMovieLoaded;

    private MovieNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mMovieLoaded = new MutableLiveData<>();
    }

    /**
     * Get the singleton for this class
     */
    public static MovieNetworkDataSource getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieNetworkDataSource(context.getApplicationContext(), executors);
            }
        }
        return sInstance;
    }

    public MutableLiveData<ArrayList<MovieEntry>> getMoviesList() {
        return mMovieLoaded;
    }

    /**
     * Fetch the movies.
     */
    public MovieResponse fetchMovies(int sortedBy) {
        if (sortedBy == 0 || sortedBy == 1) {
            mExecutors.networkIO().execute(() -> {
                try {
                    switch (sortedBy) {
                        case 0:
                            mUrl = NetworkUtils.buildPopularListJsonUrl();
                            break;
                        case 1:
                            mUrl = NetworkUtils.buildTopRatedListJsonUrl();
                            break;
                        default:
                            mUrl = NetworkUtils.buildPopularListJsonUrl();
                            break;
                    }
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(mUrl);
                    response = DataParsingUtils.getMovieDataListFromString(jsonResponse);
                    if (response != null && response.getMovieEntries().size() != 0) {
                        // When you are off of the main thread and want to update LiveData, use postValue.
                        // It posts the update to the main thread.
                        mMovieLoaded.postValue(response.getMovieEntries());
                    }
                } catch (Exception e) {
                    // Server probably invalid
                    e.printStackTrace();
                }
            });
        } else {
            mExecutors.diskIO().execute(() -> {
                MovieDatabase db = MovieDatabase.getInstance(mContext);
                ArrayList<MovieEntry> entries =
                        (ArrayList<MovieEntry>) db.movieDao().getFavoriteMovie();
                mMovieLoaded.postValue(entries);
            });
        }
        return response;
    }
}
