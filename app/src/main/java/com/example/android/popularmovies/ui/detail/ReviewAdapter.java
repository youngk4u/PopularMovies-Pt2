package com.example.android.popularmovies.ui.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final Context mContext;
    private List<Review> mReviews;


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);
        String author = review.getAuthor();
        String comment = review.getComment();

        holder.reviewAuthorTextView.setText(author);
        holder.reviewCommentTextView.setText(comment);
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        return mReviews.size();
    }


    public ReviewAdapter(Context context) {
        mContext = context;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        final TextView reviewAuthorTextView;
        final TextView reviewCommentTextView;

        ReviewViewHolder(View view) {
            super(view);
            reviewAuthorTextView = view.findViewById(R.id.tv_reviews_author);
            reviewCommentTextView = view.findViewById(R.id.tv_reviews_comments);
        }
    }

    public void swapList(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }
}
