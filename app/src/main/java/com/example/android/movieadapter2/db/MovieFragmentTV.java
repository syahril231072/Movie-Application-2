package com.example.android.movieadapter2.db;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.movieadapter2.R;
import com.example.android.movieadapter2.viewModel.MoviesViewModel;

import static com.example.android.movieadapter2.provider.SampleContentProviderTV.URI_MENUTV;

public class MovieFragmentTV extends Fragment {
    private ListMovieAdapterTV adapter;
    private ProgressBar progressBar;
    private static final int LOADER_MENU = 1;
    private MoviesViewModel moviesViewModel;
    private Cursor mData;

public MovieFragmentTV() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = new ListMovieAdapterTV();
        View view = inflater.inflate(R.layout.fragment_favorite,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_movie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        LoaderManager.getInstance(this).initLoader(LOADER_MENU, null, mLoaderCallbacks);
        progressBar = view.findViewById(R.id.progressBar);



        showLoading(true);

        return view;
    }


    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    switch (id) {
                        case LOADER_MENU:
                            return new CursorLoader(getContext(),
                                    URI_MENUTV,
                                    new String[] {TaskEntryTV.COLUMN_NAME, TaskEntryTV.COLUMN_RELEASE, TaskEntryTV.COLUMN_DESC, TaskEntryTV.COLUMN_MOVIE},
                                    null, null, null);

                        default:
                            throw new IllegalArgumentException();
                    }
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    switch (loader.getId()) {
                        case LOADER_MENU:
                            adapter.setMenus(data);
                            break;

                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    switch (loader.getId()) {
                        case LOADER_MENU:
                            adapter.setMenus(null);
                            break;
                    }
                }

            };
    }


