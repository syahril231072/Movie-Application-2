package com.example.android.movieadapter2.db;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    LiveData<List<TaskEntry>> loadAllTasks();

    @Query("SELECT * FROM " + TaskEntry.TABLE_NAME)
    Cursor selectAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(TaskEntry taskEntry);

    @Insert
    long insert(TaskEntry menu);

    @Insert
    long[] insertAll(TaskEntry[] menus);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntry taskEntry);

    @Update
    int update(TaskEntry menu);

    @Delete
    void deleteTask(TaskEntry taskEntry);

    @Query("DELETE FROM " + TaskEntry.TABLE_NAME + " WHERE " + TaskEntry.COLUMN_ID + " = :id" )
    int deleteById(long id);

    @Query("SELECT * FROM " + TaskEntry.TABLE_NAME + " WHERE " + TaskEntry.COLUMN_ID + " = :id" )
    LiveData<TaskEntry> loadTaskById(long id);

    @Query("SELECT * FROM " + TaskEntry.TABLE_NAME + " WHERE " + TaskEntry.COLUMN_ID + " = :id" )
    Cursor selectById(long id);
}



