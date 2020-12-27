package com.example.android.movieadapter2.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TaskEntryTV.class}, version = 2, exportSchema = false)

public abstract class AppDatabaseTV extends RoomDatabase {

    private static final String LOG_TAG = AppDatabaseTV.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "todolistTV";
    private static AppDatabaseTV sInstance;

    public static AppDatabaseTV getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabaseTV.class, AppDatabaseTV.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract TaskDaoTV taskDao();

}
