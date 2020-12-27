package com.example.android.movieadapter2.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.android.movieadapter2.ListMovieAdapter;
import com.example.android.movieadapter2.R;
import com.example.android.movieadapter2.db.AppDatabaseTV;
import com.example.android.movieadapter2.db.AppExecutors;
import com.example.android.movieadapter2.db.TaskEntryTV;
import com.example.android.movieadapter2.model.Movie;
import com.example.android.movieadapter2.model.TVShow;
import com.example.android.movieadapter2.viewModel.MoviesViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity1 extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_TV_SHOW = "extra_movie";
    TextView titleObject;
    TextView descObject;
    TextView releaseObject;
    private String url_image;
    ImageView movieBgObject;
    private Button button;
    private ProgressBar progressBar;
    private ListMovieAdapter adapter;
    private static final int DEFAULT_TASK_ID = -1;
    private int mTaskId = DEFAULT_TASK_ID;
    private AppDatabaseTV mDb;
    private Integer nomorTV;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail1);
        mDb = AppDatabaseTV.getInstance(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        titleObject = findViewById(R.id.titleDetail);
        descObject = findViewById(R.id.descDetail);
        releaseObject = findViewById(R.id.releaseDetail);
        movieBgObject = findViewById(R.id.movieImageDetail);

        progressBar = findViewById(R.id.progressDetailMovie);
        button = findViewById(R.id.btn_submit);
        button.setVisibility(View.INVISIBLE);
        button.setOnClickListener(this);

        MoviesViewModel moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        moviesViewModel.getMovies().observe(this, getMovie);

        progressBar.setVisibility(View.VISIBLE);


        final Handler handler = new Handler();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                }

                handler.post(new Runnable() {
                    public void run() {
                        TVShow movie = getIntent().getParcelableExtra(EXTRA_TV_SHOW);


                        url_image = "https://image.tmdb.org/t/p/w185" + movie.getMovieBg();

                        nomorTV = movie.getId();

                        titleObject.setText(movie.getTitle());
                        releaseObject.setText(movie.getRelease());
                        descObject.setText(movie.getDesc());
                        descObject.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

                        Picasso.get().load(url_image)
                                .placeholder(R.color.colorAccent).into(movieBgObject);


                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });
            }
        }).start();


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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            String title        = titleObject.getText().toString().trim();

            String overview     = descObject.getText().toString().trim();
            String release_date = releaseObject.getText().toString().trim();



            final TaskEntryTV task = new TaskEntryTV(nomorTV, title, overview, release_date, url_image);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mTaskId == DEFAULT_TASK_ID) {
                        // insert new task
                        mDb.taskDao().insertTask(task);
                    } else {
                        //update task
                        task.setId(mTaskId);
                        mDb.taskDao().updateTask(task);
                    }
                    finish();
                }
            });


        }
    }
}
