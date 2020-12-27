/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.movieadapter2;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();



    private static String getCurrentDate() {
        Date current = new Date();
        SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = frmt.format(current);
        return dateString;
    }

    public static String dateN = getCurrentDate();

    private static final String DYNAMIC_WEATHER_URL =
            "https://andfun-weather.udacity.com/weather";

    private static final String STATIC_WEATHER_URL =
           "https://api.themoviedb.org/3/discover/movie?api_key=" +"f04bce2a28b277c0c4ee02124610fef5" +"&primary_release_date.gte="+ dateN +"&primary_release_date.lte="+ dateN;

    private static final String FORECAST_BASE_URL = STATIC_WEATHER_URL;

    public static URL buildUrl() {

        Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    public static URL buildUrl(Double lat, Double lon) {
        /** This will be implemented in a future lesson **/
        return null;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}