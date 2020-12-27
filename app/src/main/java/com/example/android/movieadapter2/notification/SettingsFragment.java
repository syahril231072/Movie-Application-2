package com.example.android.movieadapter2.notification;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.android.movieadapter2.AlarmReceiver;
import com.example.android.movieadapter2.AlarmReceiver1;
import com.example.android.movieadapter2.R;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener  {

    private void setSetup(boolean setup) {
        this.setup = setup;
    }

    private boolean setup;

    private void setSetup1(boolean setup1) {
        this.setup1 = setup1;
    }

    private boolean setup1;


    AlarmReceiver1 alarmReceiver1 = new AlarmReceiver1();
    AlarmReceiver alarmReceiver = new AlarmReceiver();

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.pref_visualizer);
        setupSharedPreferences();

    }

    private void setupSharedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity()));
        setSetup(sharedPreferences.getBoolean(getString(R.string.pref_show_notification_key),
                getResources().getBoolean(R.bool.show_notification)));
        setSetup1(sharedPreferences.getBoolean(getString(R.string.pref_show_notification_key1),
                getResources().getBoolean(R.bool.show_notification1)));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_show_notification_key))) {
            setSetup(sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.show_notification)));
            alarmReceiver.setRepeatingAlarm(getActivity(), AlarmReceiver.TYPE_REPEATING,
                    "07:00", "Movie Cataloque Miss You");
        }
        if (key.equals(getString(R.string.pref_show_notification_key1))) {
            setSetup1(sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.show_notification1)));
            alarmReceiver1.setRepeatingAlarm1(getActivity(), AlarmReceiver1.TYPE_REPEATING1,
                    "08:00", "New Film Release Today");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity()))
                .unregisterOnSharedPreferenceChangeListener(this);
    }



}
