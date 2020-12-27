package com.example.android.movieadapter2.pagesearch;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.movieadapter2.R;
import com.example.android.movieadapter2.activity.DetailActivity;
import com.example.android.movieadapter2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> mData = new ArrayList<>();

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
        movieViewHolder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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


            itemView.setOnClickListener(this);
        }
        void bind(Movie movies) {

            String url_image = "https://image.tmdb.org/t/p/w185" + movies.getMovieBg();


            textViewTitle.setText(movies.getTitle());
            textViewDateReleased.setText(movies.getRelease());
            textViewDescription.setText(movies.getDesc());


            Picasso.get().load(url_image)
                    .placeholder(R.color.colorAccent).into(imgPhoto);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = mData.get(position);
//
            movie.setTitle(movie.getTitle());
            movie.setDesc(movie.getDesc());
            movie.setRelease(movie.getRelease());
            movie.setMovieBg(movie.getMovieBg());
            movie.setId(movie.getId());



            Intent moveWithObjectIntent = new Intent(itemView.getContext(), DetailActivity.class);
            moveWithObjectIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
            itemView.getContext().startActivity(moveWithObjectIntent);
        }
    }


}
