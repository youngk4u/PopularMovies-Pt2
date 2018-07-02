package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.DataParsingUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MoviesAdapterOnClickHandler {

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private URL mUrl;
    private boolean mIsSortedByPopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);
        showLoading();

        mUrl = NetworkUtils.buildPopularListJsonUrl();
        mIsSortedByPopularity = true;
        new MoviesFetchTask().execute();
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
     * @param movie The clicked movie object.
     */
    @Override
    public void onClick(Movie movie) {
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        String serializedData = getString(R.string.Serialized);
        detailIntent.putExtra(serializedData, movie);
        startActivity(detailIntent);
    }

    /**
     * AsyncTask class that runs the JSON data parsing and Http response processing
     * on a background thread.
     */
    private class MoviesFetchTask extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            showLoading();
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected List<Movie> doInBackground(Void... voids) {

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(mUrl);
                return DataParsingUtils.getMovieDataListFromString(jsonResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mMovieAdapter.swapList(movies);
            showView();
        }
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
                if (!mIsSortedByPopularity) {
                    mUrl = NetworkUtils.buildPopularListJsonUrl();
                    mIsSortedByPopularity = true;
                    new MoviesFetchTask().execute();
                }
                return true;
            // Respond to a click on the "Ratings" menu option
            case R.id.action_sort_by_ratings:
                if (mIsSortedByPopularity) {
                    mUrl = NetworkUtils.buildTopRatedListJsonUrl();
                    mIsSortedByPopularity = false;
                    new MoviesFetchTask().execute();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
