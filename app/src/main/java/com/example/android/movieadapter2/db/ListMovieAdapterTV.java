package com.example.android.movieadapter2.db;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.movieadapter2.R;
import com.example.android.movieadapter2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ListMovieAdapterTV extends RecyclerView.Adapter<ListMovieAdapterTV.MovieViewHolder> {
    private Cursor mCursor;
    private Context context;
    private ArrayList<Movie> mData = new ArrayList<>();
    public void setData(ArrayList<Movie> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_layout, viewGroup, false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        if (mCursor.moveToPosition(position)) {
            movieViewHolder.textViewTitle.setText(mCursor.getString(
                    mCursor.getColumnIndexOrThrow(TaskEntryTV.COLUMN_NAME)));
            movieViewHolder.textViewDateReleased.setText(mCursor.getString(
                    mCursor.getColumnIndexOrThrow(TaskEntryTV.COLUMN_RELEASE)));

            movieViewHolder.textViewDescription.setText(mCursor.getString(
                    mCursor.getColumnIndexOrThrow(TaskEntryTV.COLUMN_DESC)));
            String url_image =  mCursor.getString(
                    mCursor.getColumnIndexOrThrow(TaskEntryTV.COLUMN_MOVIE));

            Picasso.get().load(url_image)
                    .placeholder(R.color.colorAccent).into(movieViewHolder.imgPhoto);

        }
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    void setMenus(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView textViewTitle;
        TextView textViewDateReleased;
        TextView textViewDescription;


        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.homeTitle);
            textViewDateReleased = itemView.findViewById(R.id.homeRelease);
            textViewDescription = itemView.findViewById(R.id.descDetail);

            imgPhoto = itemView.findViewById(R.id.blackBg);



        }


    }


}
