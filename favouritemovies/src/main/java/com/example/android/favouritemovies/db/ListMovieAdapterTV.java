package com.example.android.favouritemovies.db;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.favouritemovies.R;
import com.example.android.favouritemovies.model.Movie;

import java.util.ArrayList;


public class ListMovieAdapterTV extends RecyclerView.Adapter<ListMovieAdapterTV.MovieViewHolder> {
    private Cursor mCursor;
    private Context context;
    private ArrayList<Movie> mData = new ArrayList<>();

    public ListMovieAdapterTV(MovieFragmentTV movieFragmentTV) {
    }


    public void setData(ArrayList<Movie> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_movie, viewGroup, false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        if (mCursor.moveToPosition(position)) {
            movieViewHolder.textViewTitle.setText(mCursor.getString(
                    mCursor.getColumnIndexOrThrow(TaskEntryTV.COLUMN_NAME)));
            movieViewHolder.textViewDateReleased.setText(mCursor.getString(
                    mCursor.getColumnIndexOrThrow(TaskEntryTV.COLUMN_DESC)));

            movieViewHolder.textViewDescription.setText(mCursor.getString(
                    mCursor.getColumnIndexOrThrow(TaskEntryTV.COLUMN_RELEASE)));
            String url_image =  mCursor.getString(
                    mCursor.getColumnIndexOrThrow(TaskEntryTV.COLUMN_MOVIE));

            Glide.with(movieViewHolder.itemView.getContext())
                    .load(url_image)
                    .apply(new RequestOptions().override(55, 55))
                    .into(movieViewHolder.imgPhoto);

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


//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            int position = getAdapterPosition();
//            Movie movie = mData.get(position);
////
//            movie.setTitle(movie.getTitle());
//            movie.setDesc(movie.getDesc());
//            movie.setRelease(movie.getRelease());
//            movie.setMovieBg(movie.getMovieBg());
//
//            Intent moveWithObjectIntent = new Intent(itemView.getContext(), DetailActivity.class);
//            moveWithObjectIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
//            itemView.getContext().startActivity(moveWithObjectIntent);
//        }
    }


}
