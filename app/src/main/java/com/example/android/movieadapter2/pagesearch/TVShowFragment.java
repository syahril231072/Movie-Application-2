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
import com.example.android.movieadapter2.model.TVShow;

import java.util.ArrayList;

public class TVShowFragment extends Fragment {
    private com.example.android.movieadapter2.pagesearch.ListTVShowAdapter adapter;
    private ProgressBar progressBar;

    public TVShowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = new ListTVShowAdapter();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_movie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);

        SearchResultsActivity.TVShowViewModelSearch tvShowViewModel = ViewModelProviders.of(this).get(SearchResultsActivity.TVShowViewModelSearch.class);
        tvShowViewModel.getTvShow().observe(this, getTVShow);
        tvShowViewModel.setTvShow();

        showLoading(true);

        return view;
    }

    private Observer<ArrayList<TVShow>> getTVShow = new Observer<ArrayList<TVShow>>() {
        @Override
        public void onChanged(ArrayList<TVShow> tvShows) {
            if (tvShows != null) {
                adapter.setTvData(tvShows);
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
