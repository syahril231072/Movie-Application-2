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

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver2 extends BroadcastReceiver {
    private static final String TYPE_ONE_TIME = "Movie Cataloque";
    public static final String TYPE_REPEATING1 = "Movie Cataloque";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TYPE = "type";
    private static final String EXTRA_TIME = "time";


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

    // Siapkan 2 id untuk 2 macam alarm, onetime dna repeating
    private final static int ID_ONETIME = 100;
    private final static int ID_REPEATING = 101;

    public AlarmReceiver2() {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String location = getPreferredWeatherLocation(context);
        new FetchWeatherTask().execute(location);

        String type1 = intent.getStringExtra(EXTRA_TYPE);
//        String message1 = parsedWeatherData;
        String time1 = intent.getStringExtra(EXTRA_TIME);
        String title1 = type1.equalsIgnoreCase(TYPE_ONE_TIME) ? TYPE_ONE_TIME : TYPE_REPEATING1;
        int notifId1 = type1.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONETIME : ID_REPEATING;

//        message = message1;
        title = title1;
        notifId = notifId1;
        time = time1;
        scontext = context;


        //Jika Anda ingin menampilkan dengan Notif anda bisa menghilangkan komentar pada baris dibawah ini.
        //        //showAlarmNotification(context, title, message, notifId);

    }

    public void setRepeatingAlarm1(Context context, String type, String time, String message) {

        // Validasi inputan waktu terlebih dahulu
        if (isDateInvalid(time, TIME_FORMAT)) return;


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(context, AlarmReceiver2.class);
        intent1.putExtra(EXTRA_MESSAGE, message);
        intent1.putExtra(EXTRA_TYPE, type);
        intent1.putExtra(EXTRA_TIME, time);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, ID_REPEATING, intent1, PendingIntent.FLAG_ONE_SHOT);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
        }
//

    }


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
    public static String getPreferredWeatherLocation(Context context) {

        return getDefaultWeatherLocation();
    }
    private static String getDefaultWeatherLocation() {

        return DEFAULT_WEATHER_LOCATION;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {


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

            if (weatherData != null) {
                String A = "";
                String B;
                for (String weatherString : weatherData) {

                    A = weatherString + A;
                    B = A + "; " + A;
                    setParsedWeatherData(B);}
                message = parsedWeatherData;
                    Date currentTime = Calendar.getInstance().getTime();
                    //Jika Anda ingin menampilkan dengan Notif anda bisa menghilangkan komentar pada baris dibawah ini.
                    //showAlarmNotification(context, title, message, notifId);
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
//        time = dateFormat.format(currentTime);
                    String dateString = dateFormat.format(currentTime);
//                    if (dateString.equals(time)) {
                context = scontext;
                showAlarmNotification(title, message, notifId);
//                    }

                }


            }

        }

    private void showAlarmNotification(String title, String message, int notifId) {
            String CHANNEL_ID = "Channel_1";
            String CHANNEL_NAME = "AlarmManager channel";
        Context mcontext = scontext;


            NotificationManager notificationManagerCompat = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
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
    }

