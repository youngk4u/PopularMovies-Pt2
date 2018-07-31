package com.example.android.popularmovies.ui.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.network.MovieResponse;
import com.example.android.popularmovies.data.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private final Context mContext;
    final private MoviesAdapterOnClickHandler mClickHandler;
    private List<MovieEntry> mMovieEntries;

    private int mItemCount;

    /**
     * The interface that receives onClick messages.
     */
    public interface MoviesAdapterOnClickHandler {
        void onClick(MovieEntry movieEntry);
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
        if (!mMovieEntries.isEmpty()) {
            // Grab all the Movie objects by the position they're in.
            MovieEntry movieEntry = mMovieEntries.get(position);
            // Parse poster path and load image using Picasso.
            String picPath = movieEntry.getPosterPath();
            String posterPath = NetworkUtils.buildPosterPath(picPath);
            Picasso.with(mContext).load(posterPath).into(holder.mPosterView);
        }
    }

    @Override
    public int getItemCount() {
        if (mMovieEntries == null) return 0;
        return mItemCount;
    }

    public void setItemCount(int itemCount) {
        mItemCount = itemCount;
    }

    /**
     * ViewHolder class for the RecyclerView in MainActivity.
     */
    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mPosterView;

        MovieAdapterViewHolder(View view) {
            super(view);
            mPosterView = view.findViewById(R.id.poster_image);

            view.setOnClickListener(this);
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
                MovieEntry movieEntry = mMovieEntries.get(adapterPosition);
                mClickHandler.onClick(movieEntry);
            } else {
                Toast.makeText(view.getContext(), "Click error", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * This is handy when we get new data from the web but don't want to create a
     * new MovieAdapter to display it.
     *
     * @param newMovieEntries The new list data to be displayed.
     */
    void swapList(List<MovieEntry> newMovieEntries, MovieResponse response) {

        if (mMovieEntries == null) {
            mMovieEntries = newMovieEntries;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mMovieEntries.size();
                }

                @Override
                public int getNewListSize() {
                    return newMovieEntries.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mMovieEntries.get(oldItemPosition).getId() ==
                            newMovieEntries.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    MovieEntry newWeather = newMovieEntries.get(newItemPosition);
                    MovieEntry oldWeather = mMovieEntries.get(oldItemPosition);
                    return newWeather.getId() == oldWeather.getId();
                }
            });
            mMovieEntries = newMovieEntries;
            result.dispatchUpdatesTo(this);
        }
    }

    void RemoveAllData() {
        mMovieEntries = new ArrayList<>();
    }
}
