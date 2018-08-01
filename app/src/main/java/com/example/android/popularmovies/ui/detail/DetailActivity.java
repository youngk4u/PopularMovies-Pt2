package com.example.android.popularmovies.ui.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieRepository;
import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Trailer;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.network.MovieNetworkDataSource;
import com.example.android.popularmovies.databinding.ActivityDetailBinding;
import com.example.android.popularmovies.data.network.NetworkUtils;
import com.example.android.popularmovies.ui.list.MovieAdapter;
import com.example.android.popularmovies.utilities.DataParsingUtils;
import com.example.android.popularmovies.utilities.InjectorUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;


public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerClickHandler {

    private static final String MOVIE_ID = "MOVIE_ID";
    private static final String SORT_BY = "sort_by";
    private static final String YOUTUBE_BASE = "https://www.youtube.com/watch?v=";
    private static final int SORTED_BY_FAVORITE = 2;
    private int SORTED_BY = 0;
    private int favored;

    private ActivityDetailBinding mDetailDataBinding;
    private DetailActivityViewModel mViewModel;

    private MovieNetworkDataSource dataSource;

    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mReviewRecyclerView;

    private URL trailerUrl;
    private URL reviewUrl;
    private List<Trailer> mTrailers;
    private List<Review> mReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDetailDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        int mId = getIntent().getIntExtra(MOVIE_ID, 0);
        SORTED_BY = getIntent().getIntExtra(SORT_BY, 0);

        dataSource = InjectorUtils.provideNetworkDataSource(getApplicationContext());
        DetailViewModelFactory factory = InjectorUtils.provideDetailViewModelFactory(this.getApplicationContext(), mId);
        mViewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);
        mViewModel.getMovie().observe(this, new Observer<MovieEntry>() {
            @Override
            public void onChanged(@Nullable MovieEntry movieEntry) {
                if (movieEntry != null) bindingContents(movieEntry);
            }
        });

        /* Trailers RecyclerView */
        mTrailerRecyclerView = findViewById(R.id.rv_movie_trailers);
        LinearLayoutManager trailerLayoutManager =
                new LinearLayoutManager(this);

        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this, this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        trailerUrl = NetworkUtils.buildTrailerListJsonUrl(mId);
        new TrailerFetchTask().execute();

        /* Reviews RecyclerView */
        mReviewRecyclerView = findViewById(R.id.rv_movie_reviews);
        LinearLayoutManager reviewLayoutManager =
                new LinearLayoutManager(this);

        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter(this);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        reviewUrl = NetworkUtils.buildReviewListJsonUrl(mId);
        new ReviewFetchTask().execute();
    }

    private void buttonStatus(Button button) {
        if (favored == 1) {
            button.setPressed(false);
            button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            button.setTextColor(Color.WHITE);
        } else {
            button.setPressed(true);
            button.setTextColor(Color.BLACK);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                button.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_border) );
            } else {
                button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_border));
            }
        }
    }

    private void executeUpdate(MovieEntry mMovieEntry) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mViewModel.updateMovie(mMovieEntry);
            }
        });
    }

    private void favoriteButton(MovieEntry mMovieEntry) {
        Button button = findViewById(R.id.favorite);
        favored = mMovieEntry.getFavored();
        buttonStatus(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovieEntry.getFavored() == 0) {
                    favored = 1;
                    buttonStatus(button);
                    mMovieEntry.setFavored(favored);
                    executeUpdate(mMovieEntry);
                } else {
                    favored = 0;
                    buttonStatus(button);
                    mMovieEntry.setFavored(favored);
                    executeUpdate(mMovieEntry);
                    if (SORTED_BY == 2) {
                        dataSource.fetchMovies(SORTED_BY_FAVORITE);
                    }
                }
            }
        });
    }

    /**
     * This method performs Data binding for the detail view.
     *
     * @param movieEntry the Movie object passed from the main activity.
     */
    private void bindingContents(MovieEntry movieEntry){

        /* POSTER */
        String posterPath = NetworkUtils.buildPosterPath(movieEntry.getPosterPath());
        Picasso.with(this).load(posterPath).into(mDetailDataBinding.posterDetailImage);

        /* TITLE */
        mDetailDataBinding.title.setText(movieEntry.getTitle());

        /* ORIGINAL TITLE */
        mDetailDataBinding.originalTitle.setText(movieEntry.getOriginalTitle());

        /* USER RATING */
        String outOfTen = getString(R.string.out_of_ten);
        String userRating = Double.toString(movieEntry.getUserRating()) + outOfTen;
        mDetailDataBinding.userRating.setText(userRating);

        /* RELEASE DATE*/
        mDetailDataBinding.releaseDate.setText(movieEntry.getReleaseDate());

        /* OVERVIEW */
        mDetailDataBinding.overView.setText(movieEntry.getOverview());

        /* FAVORITE BUTTON */
        favoriteButton(movieEntry);
    }

    private class TrailerFetchTask extends AsyncTask<Void, Void, List<Trailer>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Trailer> doInBackground(Void... voids) {
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(trailerUrl);
                return DataParsingUtils.getTrailerListFromString(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            mTrailers = trailers;
            mTrailerAdapter.swapList(trailers);
        }
    }

    private class ReviewFetchTask extends AsyncTask<Void, Void, List<Review>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Review> doInBackground(Void... voids) {
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(reviewUrl);
                return DataParsingUtils.getReviewListFromString(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            mReviews = reviews;
            mReviewAdapter.swapList(mReviews);
        }
    }

    @Override
    public void onTrailerClick(int position) {
        Trailer trailer = mTrailers.get(position);

        String youtubeKey = trailer.getTrailerKey();

        Uri trailerLink = Uri.parse(YOUTUBE_BASE + youtubeKey);
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, trailerLink);
        ComponentName trailerComponent = trailerIntent.resolveActivity(getPackageManager());

        if( trailerComponent != null ) {
            startActivity(trailerIntent);
        }
    }
}

