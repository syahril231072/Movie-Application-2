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
public interface TaskDaoTV {
    @Query("SELECT * FROM task")
    LiveData<List<TaskEntryTV>> loadAllTasks();

    @Query("SELECT * FROM " + TaskEntryTV.TABLE_NAME)
    Cursor selectAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(TaskEntryTV taskEntryTV);

    @Insert
    long insert(TaskEntryTV menu);

    @Insert
    long[] insertAll(TaskEntryTV[] menus);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntryTV taskEntryTV);

    @Update
    int update(TaskEntryTV menu);

    @Delete
    void deleteTask(TaskEntryTV taskEntryTV);

    @Query("DELETE FROM " + TaskEntryTV.TABLE_NAME + " WHERE " + TaskEntryTV.COLUMN_ID + " = :id" )
    int deleteById(long id);

    @Query("SELECT * FROM " + TaskEntryTV.TABLE_NAME + " WHERE " + TaskEntryTV.COLUMN_ID + " = :id" )
    LiveData<TaskEntryTV> loadTaskById(long id);

    @Query("SELECT * FROM " + TaskEntryTV.TABLE_NAME + " WHERE " + TaskEntryTV.COLUMN_ID + " = :id" )
    Cursor selectById(long id);
}



