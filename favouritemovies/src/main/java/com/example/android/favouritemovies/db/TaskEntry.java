package com.example.android.favouritemovies.db;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = TaskEntry.TABLE_NAME)
public class TaskEntry {
    public static final String TABLE_NAME = "task";

    /** The name of the ID column. */
    public static final String COLUMN_ID = BaseColumns._ID;

    /** The name of the name column. */
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_RELEASE = "release";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_MOVIE = "movie";
    @PrimaryKey()
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;
    @ColumnInfo(name = COLUMN_NAME)
    private String title;
    @ColumnInfo(name = COLUMN_RELEASE)
    private String release;
    @ColumnInfo(name = COLUMN_DESC)
    private String desc;
    @ColumnInfo(name = COLUMN_MOVIE)
    private String movieBg;

    @Ignore
    public TaskEntry(String title, String release, String desc, String movieBg) {
        this.title = title;
        this.release = release;
        this.desc = desc;
        this.movieBg = movieBg;
    }

    public TaskEntry(long id, String title, String release, String desc, String movieBg) {
        this.id = id;
        this.title = title;
        this.release = release;
        this.desc = desc;
        this.movieBg = movieBg;
    }
    @Ignore
    public TaskEntry() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease() {
        return release;
    }

    public String getDesc() {
        return desc;
    }

    public String getMovieBg() {
        return movieBg;
    }

    public static TaskEntry fromContentValues(ContentValues values) {
        final TaskEntry menu = new TaskEntry();
        if (values.containsKey(COLUMN_ID)) {
            menu.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_NAME)) {
            menu.title = values.getAsString(COLUMN_NAME);
        }
        if (values.containsKey(COLUMN_RELEASE)) {
            menu.title = values.getAsString(COLUMN_RELEASE);
        }
        if (values.containsKey(COLUMN_DESC)) {
            menu.title = values.getAsString(COLUMN_DESC);
        }
        if (values.containsKey(COLUMN_MOVIE)) {
            menu.title = values.getAsString(COLUMN_MOVIE);
        }
        return menu;
    }

}
