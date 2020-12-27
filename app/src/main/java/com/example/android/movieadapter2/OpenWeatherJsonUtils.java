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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public final class OpenWeatherJsonUtils {

    public static String[] getSimpleWeatherStringsFromJson(String forecastJsonStr)
            throws JSONException {


        final String OWM_LIST = "results";


        final String OWM_TEMPERATURE = "title";



        final String OWM_MESSAGE_CODE = "total results";


        String[] parsedWeatherData;

        JSONObject forecastJson = new JSONObject(forecastJsonStr);


        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);


        parsedWeatherData = new String[weatherArray.length()];



        for (int i = 0; i < weatherArray.length(); i++) {

            String high;

            JSONObject dayForecast = weatherArray.getJSONObject(i);
high = dayForecast.getString(OWM_TEMPERATURE);


            parsedWeatherData[i] = high;
        }

        return parsedWeatherData;
    }

}