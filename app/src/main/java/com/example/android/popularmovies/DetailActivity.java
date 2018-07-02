package com.example.android.popularmovies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.databinding.ActivityDetailBinding;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding mDetailDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDetailDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Movie mMovie = (Movie) getIntent().getSerializableExtra(getString(R.string.Serialized));
        bindingContents(mMovie);
    }

    /**
     * This method performs Data binding for the detail view.
     *
     * @param movie the Movie object passed from the main activity.
     */
    private void bindingContents(Movie movie){

        /* POSTER */
        String posterPath = NetworkUtils.buildPosterPath(movie.getPosterPath());
        Picasso.with(this).load(posterPath).into(mDetailDataBinding.posterDetailImage);

        /* TITLE */
        mDetailDataBinding.title.setText(movie.getTitle());

        /* ORIGINAL TITLE */
        mDetailDataBinding.originalTitle.setText(movie.getOriginalTitle());

        /* USER RATING */
        String outOfTen = getString(R.string.out_of_ten);
        String userRating = Double.toString(movie.getUserRating()) + outOfTen;
        mDetailDataBinding.userRating.setText(userRating);

        /* RELEASE DATE*/
        mDetailDataBinding.releaseDate.setText(movie.getReleaseDate());

        /* OVERVIEW */
        mDetailDataBinding.overView.setText(movie.getOverview());
    }
}

