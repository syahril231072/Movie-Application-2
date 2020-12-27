package com.example.android.movieadapter2.pagesearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.movieadapter2.R;
import com.example.android.movieadapter2.model.Movie;

import java.util.ArrayList;

public class MovieFragment extends Fragment {
    private ListMovieAdapter adapter;
    private ProgressBar progressBar;

    public MovieFragment () {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = new ListMovieAdapter();
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_movie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);

        SearchResultsActivity.MoviesViewModelSearch moviesViewModel = ViewModelProviders.of(this).get(SearchResultsActivity.MoviesViewModelSearch.class);
        moviesViewModel.getMovies().observe(this, getMovie);
        moviesViewModel.setMovies();

        showLoading(true);

        return view;
    }
    private Observer<ArrayList<Movie>> getMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if (movies != null) {
                adapter.setData(movies);
            }

            showLoading(false);

        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
