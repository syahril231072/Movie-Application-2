package com.example.android.movieadapter2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver1 extends BroadcastReceiver implements MyAsyncCallback {
    public static final String TYPE_ONE_TIME = "OneTimeAlarm";
    public static final String TYPE_REPEATING1 = "RepeatingAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    private static final String EXTRA_TIME = "time";

    // Siapkan 2 id untuk 2 macam alarm, onetime dna repeating
    private final static int ID_ONETIME = 100;
    private final static int ID_REPEATING = 101;

    public void setParsedWeatherData(String parsedWeatherData) {
        this.parsedWeatherData = parsedWeatherData;
    }

    private String parsedWeatherData;
    private static final String DEFAULT_WEATHER_LOCATION = "Movie";

    private Context context;
    private Context scontext;
    private String message;
    private String title;
    private Integer notifId;
    private String time;

    public AlarmReceiver1() {
    }

    private String getPreferredWeatherLocation(Context context) {

        return getDefaultWeatherLocation();
    }

    private String getDefaultWeatherLocation() {

        return DEFAULT_WEATHER_LOCATION;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String location = getPreferredWeatherLocation(context);
        title = type.equalsIgnoreCase(TYPE_ONE_TIME) ? TYPE_ONE_TIME : TYPE_REPEATING1;
        notifId = type.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONETIME : ID_REPEATING;
        new FetchWeatherTask(this).execute(location);




        time = intent.getStringExtra(EXTRA_TIME);


        scontext = context;

//        showAlarmNotification(context, title, message, notifId);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(final String parsedWeatherData) {
        if (parsedWeatherData != null) {
            message = parsedWeatherData;
context = scontext;
            Date currentTime = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String dateString = dateFormat.format(currentTime);
            if (dateString.equals(time)) {
            showAlarmNotification(context, title, message, notifId);}
        }


    }
    public void setRepeatingAlarm1(Context context, String type, String time, String message) {

        // Validasi inputan waktu terlebih dahulu
        if (isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver1.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_TIME, time);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show();
    }

    public boolean isAlarmSet(Context context, String type) {
        Intent intent = new Intent(context, AlarmReceiver1.class);
        int requestCode = type.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONETIME : ID_REPEATING;

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String TIME_FORMAT = "HH:mm";

    // Metode ini digunakan untuk validasi date dan time
    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";
//        mcontext = scontext;


        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.videocamera)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentTitle(title)
                .setContentText("Release Today" + message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
            /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        Materi ini akan dibahas lebih lanjut di modul extended
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }

    }
        class FetchWeatherTask extends AsyncTask<String, Void, String[]> {


            WeakReference<MyAsyncCallback> myListener;

            FetchWeatherTask(MyAsyncCallback myListener) {
                this.myListener = new WeakReference<>(myListener);


            }





            // Gunakan metode ini untuk menampilkan notifikasi

            // Metode ini digunakan untuk menjalankan alarm repeating






            // Gunakan metode ini untuk mengecek apakah alarm tersebut sudah terdaftar di alarm manager



            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                MyAsyncCallback myListener = this.myListener.get();
                if (myListener != null) {
                    myListener.onPreExecute();
                }
            }

            @Override
            protected String[] doInBackground(String... params) {
                if (params.length == 0) {
                    return null;
                }

                String location = params[0];
                URL weatherRequestUrl = NetworkUtils.buildUrl();

                try {
                    String jsonWeatherResponse = NetworkUtils
                            .getResponseFromHttpUrl(weatherRequestUrl);

                    String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                            .getSimpleWeatherStringsFromJson(jsonWeatherResponse);

                    return simpleJsonWeatherData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String[] weatherData) {
                super.onPostExecute(weatherData);


                MyAsyncCallback myListener = this.myListener.get();


                if (weatherData != null) {
                    String A = "";
                    String B;
                    for (String weatherString : weatherData) {

                        A = weatherString + A;
                        B = A + "; " + A;
                        setParsedWeatherData(B);
                    }
//                    message = parsedWeatherData;


                    myListener.onPostExecute(parsedWeatherData);
//                    }

                }


            }
        }

    }
