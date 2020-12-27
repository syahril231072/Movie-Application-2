package com.example.android.movieadapter2.db;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.movieadapter2.R;

import java.util.List;

public class TVFragmentFav extends Fragment implements TaskAdapterTV.ItemClickListener{
    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private TaskAdapterTV mAdapter;


    private AppDatabaseTV mDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite,container,false);
        mRecyclerView = view.findViewById(R.id.rv_movie);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new TaskAdapterTV(this.getContext(), this);
        mRecyclerView.setAdapter(mAdapter);




        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete


                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<TaskEntryTV> tasks = mAdapter.getTasks();
                        mDb.taskDao().deleteTask(tasks.get(position));

                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);



        mDb = AppDatabaseTV.getInstance(getContext());
        setupViewModel();
        return view;
    }

    private void setupViewModel() {
        MainViewModelTV viewModel = ViewModelProviders.of(this).get(MainViewModelTV.class);
       viewModel.getTasks().observe(this, new Observer<List<TaskEntryTV>>() {
           @Override
           public void onChanged(@Nullable List<TaskEntryTV> taskEntries) {
               Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
               mAdapter.setTasks(taskEntries);
           }
       });
    }
    @Override
    public void onItemClickListener(long itemId) {

    }
}
