package com.example.android.favouritemovies;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.android.favouritemovies.db.MovieFragmentT;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    final Fragment fragmentMovie = new MovieFragmentT();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentMovie;
    public String info = "extra";
    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        context = MainActivity.this;

    }





}
