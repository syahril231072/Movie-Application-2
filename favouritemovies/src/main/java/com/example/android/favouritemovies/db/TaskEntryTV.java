package com.example.android.favouritemovies.db;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = TaskEntryTV.TABLE_NAME)
public class TaskEntryTV {
    public static final String TABLE_NAME = "task";

    /** The name of the ID column. */
    public static final String COLUMN_ID = BaseColumns._ID;

    /** The name of the name column. */
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_RELEASE = "release";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_MOVIE = "movie";

    @PrimaryKey(autoGenerate = true)
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
    public TaskEntryTV(String title, String release, String desc, String movieBg) {
        this.title = title;
        this.release = release;
        this.desc = desc;
        this.movieBg = movieBg;
    }

    public TaskEntryTV(long id, String title, String release, String desc, String movieBg) {
        this.id = id;
        this.title = title;
        this.release = release;
        this.desc = desc;
        this.movieBg = movieBg;
    }

    @Ignore
    public TaskEntryTV() {

    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
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

    public void setRelease(String release) {
        this.release = release;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMovieBg() {
        return movieBg;
    }

    public void setMovieBg(String movieBg) {
        this.movieBg = movieBg;
    }

    public static TaskEntryTV fromContentValues(ContentValues values) {
        final TaskEntryTV menu = new TaskEntryTV();
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
