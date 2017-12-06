package androidclassproject.weatherapplication;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidclassproject.weatherapplication.Common.Common;
import androidclassproject.weatherapplication.Helper.Helper;
import androidclassproject.weatherapplication.Model.HourlyWeatherMap;
import androidclassproject.weatherapplication.Model.OpenWeatherMap;

public class WeatherUpdateService extends Service implements LocationListener {

    // Declare a location manager varible.
    LocationManager locationManager;
    String provider;

    // Declare variables to store the latitude and longitude vales.
    static double lat = 0;
    static double lng = 0;

    // Declare variables to determine if location services are enabled or not
    boolean networkLocationEnabled, gpsLocationEnabled;

    // Declare a new OpenWeatherMap object.
    OpenWeatherMap openWeatherMap = new OpenWeatherMap();

    // Declare the default amount for the temperature and time display variables.
    String display_fahrenheit = "";
    String display_celsius = "";
    String time_display_sunrise = "";
    String temp_time_display = "";
    String city, lastUpdate, description, humidity;

    int temp_setting = 0;
    int time_setting = 0;

    private SharedPreferences prefs;
    RecyclerView recyclerView;
    HourAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    int MY_MYPERMISSION = 0;
    TextView hourlyTexts[];
    TextView hourlyDegreeTexts[];
    ImageView hourlyImages[];
    HourlyWeatherMap hourlyWeatherMap = new HourlyWeatherMap();
    int theme_settings = 0;
    int theme_settings_check = 0;

    // Declare variables to store the sunset time, the sunrise time, and the device time.
    long device_time = System.currentTimeMillis() / 1000;
    double device_time_calculate = (double) device_time;
    double sunset_time;
    double sunrise_time;
    Configuration config;
    String start = "";
    Date start_date;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    int hour_check = 0;
    int minute_check = 0;
    int year_check = 0;
    int month_check = 0;
    int day_check = 0;

    private Timer timer;

    @Override
    public void onCreate() {
        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 1, this);

        try {
            startTimer();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Log.d("News reader", "Service started");

        try {
            if (intent.hasExtra("start_time")) {
                start = intent.getStringExtra("start_time");
            }
        } catch (Exception e) {

            Log.v("check", e.toString());

        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Log.d("News reader", "Service bound - not used!");
        return null;
    }

    @Override
    public void onDestroy() {
        // Log.d("News reader", "Service destroyed");
        stopTimer();
    }

    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();

        new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lng)));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void setLocation() {
        provider = locationManager.getBestProvider(new Criteria(), false);
        Location location = null;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        networkLocationEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        gpsLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lng)));

        }
    }

    void startTimer() throws ParseException {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                setLocation();

                // display notification
                sendNotification();


            }
        };

        timer = new Timer(true);

        hour_check = prefs.getInt("Hours", -1);
        minute_check = prefs.getInt("Minutes", -1);
        year_check = Calendar.getInstance().get(Calendar.YEAR);
        month_check = Calendar.getInstance().get(Calendar.MONTH);
        day_check = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        String time = year_check + "-" + (month_check + 1) + "-" + day_check + 'T' + hour_check + ":" + minute_check + ":00Z";

        try {

            start_date = format.parse(time);
        } catch (Exception e) {

            Log.v("check", e.toString());
        }

        // long check = System.currentTimeMillis();


        long interval = 1000 * 30;
        Log.v("Check", Long.toString(interval));
        timer.scheduleAtFixedRate(task, start_date, interval);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void sendNotification() {

        // create the intent for the notification
        Intent notificationIntent = new Intent(this, DisplayWeather.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // create the pending intent
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, flags);

        // create the variables for the notification
        int icon = R.drawable.sun;
        CharSequence tickerText = "View Weather";
        CharSequence contentTitle = getText(R.string.app_name);

        if (city == "null") {

            city = "Not Available";

        }

        temp_setting = Integer.parseInt(prefs.getString("pref_temperature", "0"));

        String display_temperature = "";

        if (temp_setting == 0) {

            display_temperature = display_fahrenheit;

        } else if (temp_setting == 1) {

            display_temperature = display_celsius;

        }

        CharSequence contentText = "City: " + city + " Temperature: " + display_temperature;

        // create the notification and set its data
        Notification notification =
                new Notification.Builder(this)
                        .setSmallIcon(icon)
                        .setTicker(tickerText)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();

        // display the notification
        NotificationManager manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        final int NOTIFICATION_ID = 1;
        manager.notify(NOTIFICATION_ID, notification);
    }

    private class GetWeather extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Create a new Gson object.
            Gson gson = new Gson();

            // Create a new type token.
            Type mType = new TypeToken<OpenWeatherMap>() {
            }.getType();

            // Create an openWeatherMap object.
            openWeatherMap = gson.fromJson(s, mType);

            double temp_celsius = openWeatherMap.getMain().getTemp();
            double check = (double) 9 / 5;
            double temp_fahrenheit = temp_celsius * check + 32;
            temp_fahrenheit = Math.round(temp_fahrenheit);
            display_fahrenheit = Double.toString(temp_fahrenheit);
            display_fahrenheit = String.format("%s °F", display_fahrenheit);
            display_celsius = Double.toString(temp_celsius);
            display_celsius = String.format("%s °C", display_celsius);

            // Set the sunrise and sunset times.
            sunset_time = openWeatherMap.getSys().getSunset();
            sunrise_time = openWeatherMap.getSys().getSunrise();


            temp_setting = Integer.parseInt(prefs.getString("pref_temperature", "0"));

            //Load icons locally
            String now = String.format(Common.getDateNow());
            now = now.substring(11, now.length());

            city = String.format("%s, %s", openWeatherMap.getName(), openWeatherMap.getSys().getCountry());
            description = String.format("%s", openWeatherMap.getWeather().get(0).getDescription().substring(0,1).toUpperCase()
                    + openWeatherMap.getWeather().get(0).getDescription().substring(1));


        }

        @Override
        protected String doInBackground(String... strings) {

            String stream = null;
            String urlString = strings[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);

            return stream;

        }
    }
}
