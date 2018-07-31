package com.example.android.popularmovies.ui.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final Context mContext;
    final private TrailerClickHandler mClickHandler;
    private List<Trailer> mTrailers;


    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);
        String name = trailer.getTrailerName();

        holder.trailerTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) return 0;
        return mTrailers.size();
    }

    public interface TrailerClickHandler {
        void onTrailerClick(int position);
    }

    public TrailerAdapter(Context context, TrailerClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView trailerTextView;

        TrailerViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            trailerTextView = view.findViewById(R.id.tv_trailer_text);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickHandler.onTrailerClick(position);
        }
     }

     public void swapList(List<Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
     }
}
