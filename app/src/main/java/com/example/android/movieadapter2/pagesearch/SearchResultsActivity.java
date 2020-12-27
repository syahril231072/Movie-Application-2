package com.example.android.movieadapter2.pagesearch;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import com.example.android.movieadapter2.BuildConfig;
import com.example.android.movieadapter2.R;
import com.example.android.movieadapter2.model.Movie;
import com.example.android.movieadapter2.model.TVShow;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchResultsActivity extends AppCompatActivity  {


    final Fragment fragmentMovie = new MovieFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentMovie;
    public final static String EXTRA_NAME = "extra";
    public static String search1;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String search = getIntent().getStringExtra(EXTRA_NAME);
        search1 = search;



        com.example.android.movieadapter2.pagesearch.SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        //use the query to search your data somehow
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);


        }
    }

    public static class MoviesViewModelSearch extends ViewModel {

        private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();


        public void  setMovies() {
            AsyncHttpClient client = new AsyncHttpClient();
            final ArrayList<Movie> listItems = new ArrayList<>();

            String url = "https://api.themoviedb.org/3/search/movie?api_key=" + BuildConfig.API_KEY + "&language=en-US&query=" + search1 ;

            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject weather = list.getJSONObject(i);
                            Movie movieItems = new Movie(weather);
                            listItems.add(movieItems);
                        }
                        listMovies.postValue(listItems);
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                }
            });
        }
        public LiveData<ArrayList<Movie>> getMovies() {
            return listMovies;
        }
    }
    public static class TVShowViewModelSearch extends ViewModel {

        private MutableLiveData<ArrayList<TVShow>> listTvShow = new MutableLiveData<>();

        public void setTvShow() {
            AsyncHttpClient client = new AsyncHttpClient();
            final ArrayList<TVShow> listItem = new ArrayList<>();

            String url = "https://api.themoviedb.org/3/search/tv?api_key=" + BuildConfig.API_KEY + "&language=en-US&query=" + search1;

            client.get(url, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");

                        for (int i = 0; i < list.length(); i++) {

                            JSONObject tv = list.getJSONObject(i);
                            TVShow tvShowItems = new TVShow(tv);
                            listItem.add(tvShowItems);
                        }
                        listTvShow.postValue(listItem);
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                }

            });

        }
        public LiveData<ArrayList<TVShow>> getTvShow() {
            return listTvShow;
        }
    }

}