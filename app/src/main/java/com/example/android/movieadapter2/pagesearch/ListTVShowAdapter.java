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
import com.example.android.movieadapter2.activity.DetailActivity1;
import com.example.android.movieadapter2.model.TVShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListTVShowAdapter extends RecyclerView.Adapter<ListTVShowAdapter.TVShowViewHolder> {

    private ArrayList<TVShow> tvData = new ArrayList<>();

    public void setTvData(ArrayList<TVShow> items) {
        tvData.clear();
        tvData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View tvView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_movie, viewGroup, false);
        return new TVShowViewHolder(tvView);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder tvShowViewHolder, int position) {
        tvShowViewHolder.bind(tvData.get(position));
    }

    @Override
    public int getItemCount() {
        return tvData.size();
    }

    class TVShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgPoster;
        TextView textViewName, textViewAirDate, txtDescription;


        TVShowViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.homeTitle);
            textViewAirDate = itemView.findViewById(R.id.homeRelease);
            txtDescription = itemView.findViewById(R.id.descDetail);


            imgPoster = itemView.findViewById(R.id.blackBg);

            itemView.setOnClickListener(this);
        }

        void bind(TVShow tvShow) {

            String url_image = "https://image.tmdb.org/t/p/w185" + tvShow.getMovieBg();


            textViewName.setText(tvShow.getTitle());
            textViewAirDate.setText(tvShow.getRelease());
            txtDescription.setText(tvShow.getDesc());

            Picasso.get().load(url_image)
                    .placeholder(R.color.colorAccent).into(imgPoster);



        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            TVShow tvShow = tvData.get(position);
//
            tvShow.setTitle(tvShow.getTitle());
            tvShow.setDesc(tvShow.getDesc());
            tvShow.setRelease(tvShow.getRelease());
            tvShow.setMovieBg(tvShow.getMovieBg());
            tvShow.setId(tvShow.getId());

            Intent moveWithObjectIntent = new Intent(itemView.getContext(), DetailActivity1.class);
            moveWithObjectIntent.putExtra(DetailActivity1.EXTRA_TV_SHOW, tvShow);
            itemView.getContext().startActivity(moveWithObjectIntent);

        }
    }
}
