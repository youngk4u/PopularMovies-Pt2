package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private final Context mContext;
    final private MoviesAdapterOnClickHandler mClickHandler;
    private List<Movie> mMovies;

    /**
     * The interface that receives onClick messages.
     */
    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    /**
     * Movie Adapter.
     */
    public MovieAdapter(Context context, MoviesAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movies_list_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    /**
     * This function gets the poster path from the Movie object
     * and binds the poster data to imageView.
     *
     * @param holder The MovieAdapterViewHolder.
     * @param position The position of the ViewHolder in recyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        // Grab all the Movie objects by the position they're in.
        Movie movie = mMovies.get(position);
        // Parse poster path and load image using Picasso.
        String picPath = movie.getPosterPath();
        String posterPath = NetworkUtils.buildPosterPath(picPath);
        Picasso.with(mContext).load(posterPath).into(holder.mPosterView);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

    /**
     * ViewHolder class for the RecyclerView in MainActivity.
     */
    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mPosterView;

        MovieAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mPosterView = view.findViewById(R.id.poster_image);
        }

        /**
         * This gets called by the views during a click.
         * It will get the ID and pass it to onClick.
         *
         * @param view The View that was clicked
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            // Ensure that item exists even through list swapping, or else app would crash
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Movie movie = mMovies.get(adapterPosition);
                mClickHandler.onClick(movie);
            } else {
                Toast.makeText(view.getContext(), "Click error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This is handy when we get new data from the web but don't want to create a
     * new MovieAdapter to display it.
     *
     * @param newMovies The new list data to be displayed.
     */
    void swapList(List<Movie> newMovies) {
        mMovies = newMovies;
        notifyDataSetChanged();
    }

}
