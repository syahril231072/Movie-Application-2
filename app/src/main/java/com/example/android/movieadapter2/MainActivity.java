package com.example.android.movieadapter2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.android.movieadapter2.pagesearch.SearchResultsActivity;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity  {
    final Fragment fragmentMovie = new MovieFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentMovie;
    public String info = "extra";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);



    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {
                info = s;
                Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();


                Intent sIntent = new Intent(MainActivity.this, com.example.android.movieadapter2.pagesearch.SearchResultsActivity.class);
                sIntent.putExtra(SearchResultsActivity.EXTRA_NAME, info);
                fm.beginTransaction().remove(active).commit();
                startActivity(sIntent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String m) {

                return true;
            }


        });

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu1) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            fm.beginTransaction().remove(active).commit();
            startActivity(mIntent);
        } else if (item.getItemId() == R.id.menu2) {
            Intent kIntent = new Intent(MainActivity.this, com.example.android.movieadapter2.db.MainActivity.class);
            startActivity(kIntent);
        } else if (item.getItemId() == R.id.menu3) {
            Intent kIntent = new Intent(MainActivity.this, com.example.android.movieadapter2.notification.SettingActivity.class);
            startActivity(kIntent);
        }

        return super.onOptionsItemSelected(item);
    }




}


