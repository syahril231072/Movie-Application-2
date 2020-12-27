package com.example.android.favouritemovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class TVShow implements Parcelable {


    private Integer id;
    private String title;
    private String release;
    private String desc;
    private String movieBg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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


    public TVShow(JSONObject object) {
        try {
            String name = object.getString("name");

            String first_air_date = object.getString("first_air_date");

            String overview = object.getString("overview");
            String poster_path = object.getString("poster_path");


            this.id = object.getInt("id");
            this.title = name;
            this.desc = overview;
            this.release = first_air_date;
            this.movieBg = poster_path;


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.release);
        dest.writeString(this.desc);
        dest.writeString(this.movieBg);
    }

    protected TVShow(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.release = in.readString();
        this.desc = in.readString();
        this.movieBg = in.readString();
    }

    public static final Creator<TVShow> CREATOR = new Creator<TVShow>() {
        @Override
        public TVShow createFromParcel(Parcel source) {
            return new TVShow(source);
        }

        @Override
        public TVShow[] newArray(int size) {
            return new TVShow[size];
        }
    };
}
