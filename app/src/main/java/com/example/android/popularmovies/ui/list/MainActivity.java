package com.example.android.popularmovies.ui.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieRepository;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.network.MovieNetworkDataSource;
import com.example.android.popularmovies.data.network.MovieResponse;
import com.example.android.popularmovies.ui.detail.DetailActivity;
import com.example.android.popularmovies.utilities.InjectorUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MoviesAdapterOnClickHandler {

    private static final String MOVIE_ID = "MOVIE_ID";
    private static String SORT_BY = "sort_by";
    private static int POPULARITY = 0;
    private static int RATING = 1;
    private static int FAVORITES = 2;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private int mPosition = RecyclerView.NO_POSITION;
    private int SORTED_BY = -1;
    private boolean menuChanged = false;

    private MainActivityViewModel mViewModel;
    private MainViewModelFactory factory;
    private MovieRepository repository;
    private MovieNetworkDataSource dataSource;

    private MovieResponse response;

    /* Taken from Udacity Review */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, calculateNoOfColumns(this));

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        if (SORTED_BY == -1) {
            SORTED_BY = POPULARITY;
        }
        repository = InjectorUtils.provideRepository(getApplicationContext());
        dataSource = repository.getDataSource();

        dataSource.getMoviesList().observeForever(new Observer<ArrayList<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable ArrayList<MovieEntry> movieEntries) {
                //clearing old data
                mMovieAdapter.RemoveAllData();
                mMovieAdapter.swapList(movieEntries, response);
                if (movieEntries != null && movieEntries.size() != 0 && mMovieAdapter != null) {
                    mMovieAdapter.setItemCount(movieEntries.size());
                    if (!menuChanged) {
                        mMovieAdapter.notifyDataSetChanged();
                        menuChanged = false;
                    } else {
                        mPosition = 0;
                        mRecyclerView.smoothScrollToPosition(mPosition);
                        mMovieAdapter.notifyDataSetChanged();
                    }
                    showView();
                }
                else {
                    showLoading();
                }
            }
        });
        setupMainViewModel();
    }

    private void setupMainViewModel() {
        factory = InjectorUtils.provideMainActivityViewModelFactory(repository);
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        mViewModel.getMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
            }
        });
    }

    private void showView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * This method handles explicit intent when RecyclerView item gets clicked.
     *
     * @param movieEntry The clicked movie object.
     */
    @Override
    public void onClick(MovieEntry movieEntry) {
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra(MOVIE_ID, movieEntry.getId());
        detailIntent.putExtra(SORT_BY, SORTED_BY);
        startActivity(detailIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This method handles executing the sort when menu item gets pressed.
     *
     * @param item The MenuItem for the menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Popularity" menu option
            case R.id.action_sort_by_popularity:
                if (SORTED_BY != POPULARITY) {
                    SORTED_BY = POPULARITY;
                    response = dataSource.fetchMovies(SORTED_BY);
                    menuChanged = true;
                }
                return true;
            // Respond to a click on the "Ratings" menu option
            case R.id.action_sort_by_ratings:
                if (SORTED_BY != RATING) {
                    SORTED_BY = RATING;
                    response = dataSource.fetchMovies(SORTED_BY);
                    menuChanged = true;
                }
                return true;
            // Respond to a click on the "Favorite" menu option
            case R.id.action_sort_by_favorite:
                if (SORTED_BY != FAVORITES) {
                    SORTED_BY = FAVORITES;
                    response = dataSource.fetchMovies(SORTED_BY);
                    menuChanged = true;
                }
                return true;
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SORT_BY,SORTED_BY);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(SORT_BY)) {
            SORTED_BY = savedInstanceState.getInt(SORT_BY, 0);
        }
    }
}
