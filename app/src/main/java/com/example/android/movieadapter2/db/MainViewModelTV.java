package com.example.android.movieadapter2.db;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainViewModelTV extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModelTV.class.getSimpleName();

    private LiveData<List<TaskEntryTV>> tasks;

    public MainViewModelTV(Application application) {
        super(application);
        AppDatabaseTV database = AppDatabaseTV.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        tasks = database.taskDao().loadAllTasks();
    }

    public LiveData<List<TaskEntryTV>> getTasks() {
        return tasks;
    }
}
