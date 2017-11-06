package androidclassproject.weatherapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.location.LocationManager;
import android.location.LocationListener;
import android.content.Context;
import android.util.Log;
import android.support.v4.app.ActivityCompat;
import android.os.AsyncTask;

import java.io.IOException;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import android.view.View;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import android.util.Log;
import android.widget.Toast;

import androidclassproject.weatherapplication.Common.Common;
import androidclassproject.weatherapplication.Helper.Helper;
import androidclassproject.weatherapplication.Model.OpenWeatherMap;
import androidclassproject.weatherapplication.Model.Weather;

public class DisplayWeather extends AppCompatActivity implements LocationListener, View.OnClickListener {

    // Declare variables for the widgets on the DisplayWeather page.
    TextView txtCity, txtLastUpdate, txtDescription, txtHumidity, txtTime, txtCelsius;
    ImageView imageView;
    Button refreshButton;

    // Declare a location manager, provider, and location variables.
    LocationManager locationManager;
    String provider;
    Location location;

    // Declare variables to store the latitude and longitude values.
    static double lat = 0;
    static double lng = 0;

    // Declare variables determining if location services are enabled or not.
    boolean networkLocationEnabled, gpsLocationEnabled;

    // Declare a new OpenWeatherMap object.
    OpenWeatherMap openWeatherMap = new OpenWeatherMap();

    // Declare the default amount for the temperature and time display variables.
    String display_fahrenheit = "";
    String display_celsius = "";
    String time_display_sunrise = "";
    String temp_time_display = "";
    String city,lastUpdate,description, humidity;

    int temp_setting = 0;
    int time_setting = 0;

    private SharedPreferences prefs;

    int MY_MYPERMISSION = 0;

    int theme_settings = 0;
    int theme_settings_check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the default values for the preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the value of the theme.
        theme_settings = Integer.parseInt(prefs.getString("pref_theme", "0"));

        if (theme_settings == 0) {

            // Set the theme.
            setTheme(R.style.theme_light);

            // Set the theme settings check.
            theme_settings_check = 0;

        } else if (theme_settings == 1){

            // Set the theme.
            setTheme(R.style.theme_dark);

            // Set the theme settings check.
            theme_settings_check = 1;

        }

        // Display the application icon in the title bar.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_display_weather);


        // Declare variables for the widgets on the DisplayWeather page.
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtLastUpdate = (TextView) findViewById(R.id.txtLastUpdate);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtCelsius = (TextView) findViewById(R.id.txtCelsius);
        imageView = (ImageView) findViewById(R.id.imageView);
        refreshButton = (Button)findViewById(R.id.refreshButton);
        // Set OnClickListener for the button
        refreshButton.setOnClickListener(this);



        // Get the user's coordinates.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DisplayWeather.this, new String[]{


                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_MYPERMISSION);

        }

        // Check if location is enabled or not
        networkLocationEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        gpsLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // If either is enabled, try to get last known location
        if (networkLocationEnabled || gpsLocationEnabled) {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lng)));
            }
            // There was no last known location for this device, so wait for onLocationChanged to be called
            else {
                Log.e("Tag", "No Location");
                Toast t = Toast.makeText(this, "Retrieving location", Toast.LENGTH_SHORT);
                t.show();
            }
        }
        // Do nothing if location services are not enabled.
        else{
            Toast t = Toast.makeText(this, "Enable location services and try again.", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();

        new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lng)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_app_menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                // Toast.makeText(getBaseContext(), "Setting Selected.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DisplayWeather.this, new String[]{


                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_MYPERMISSION);

        }


        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DisplayWeather.this, new String[]{


                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_MYPERMISSION);
        }

        locationManager.requestLocationUpdates(provider, 400, 1, this);

        temp_setting = Integer.parseInt(prefs.getString("pref_temperature", "0"));

        if (temp_setting == 0) {

            txtCelsius.setText(display_fahrenheit);

        } else {

            txtCelsius.setText(display_celsius);

        }

        time_setting = Integer.parseInt(prefs.getString("pref_temperature", "0"));

        // Store the value of the theme.
        theme_settings = Integer.parseInt(prefs.getString("pref_theme", "0"));

        // If the value of the theme has changed recreate the activity.
        if (theme_settings != theme_settings_check) {
            recreate();
            new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lng)));
        }


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

    @Override
    public void onClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DisplayWeather.this, new String[]{


                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_MYPERMISSION);

        }
        networkLocationEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        gpsLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (networkLocationEnabled || gpsLocationEnabled) {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lng)));
            }
            else {
                Log.e("Tag", "No Location");
                Toast t = Toast.makeText(this, "Retrieving location", Toast.LENGTH_SHORT);
                t.show();
            }
        }

    }

    private class GetWeather extends AsyncTask<String,Void,String>{

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
            Type mType= new TypeToken<OpenWeatherMap>(){}.getType();

            // Create an openWeatherMap object.
            openWeatherMap = gson.fromJson(s, mType);

            double temp_celsius = openWeatherMap.getMain().getTemp();
            double check = (double)9/5;
            double temp_fahrenheit = temp_celsius * check + 32;
            temp_fahrenheit = Math.round(temp_fahrenheit);
            display_fahrenheit = Double.toString(temp_fahrenheit);
            display_fahrenheit = String.format("%s °F", display_fahrenheit);
            display_celsius = Double.toString(temp_celsius);
            display_celsius = String.format("%s °C", display_celsius);

            temp_setting = Integer.parseInt(prefs.getString("pref_temperature", "0"));

            if (temp_setting == 0) {

                txtCelsius.setText(display_fahrenheit);

            } else {

                txtCelsius.setText(display_celsius);

            }

            time_setting = Integer.parseInt(prefs.getString("pref_temperature", "0"));

            time_display_sunrise = String.format("Sunrise: %s", Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise()));

            if(time_display_sunrise == null)
                txtTime.setText(getResources().getString(R.string.timeCheck));
            else
                txtTime.setText(time_display_sunrise);

            temp_time_display = Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise());

            txtCity.setText(String.format("%s, %s", openWeatherMap.getName(), openWeatherMap.getSys().getCountry()));
            txtLastUpdate.setText(String.format("Last Update: %s", Common.getDateNow()));
            txtDescription.setText(String.format("%s", openWeatherMap.getWeather().get(0).getDescription().substring(0,1).toUpperCase() + openWeatherMap.getWeather().get(0).getDescription().substring(1)));
            txtHumidity.setText(String.format("Humidity: %d%%", openWeatherMap.getMain().getHumidity()));
            txtTime.setText(time_display_sunrise);
            /*Picasso.with(DisplayWeather.this)
                    .load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(imageView);
            Load weather icons locally*/
            setIcon(openWeatherMap.getWeather().get(0));

            city = String.format("%s, %s", openWeatherMap.getName(), openWeatherMap.getSys().getCountry());
            lastUpdate = String.format("Last Update: %s", Common.getDateNow());
            description = String.format("%s", openWeatherMap.getWeather().get(0).getDescription().substring(0,1).toUpperCase()
                    + openWeatherMap.getWeather().get(0).getDescription().substring(1));
            humidity = String.format("Humidity: %d%%", openWeatherMap.getMain().getHumidity());

            if(city == null)
                txtCity.setText(getResources().getString(R.string.cityCheck));
            else
                txtCity.setText(city);

            if(lastUpdate == null)
                txtLastUpdate.setText(getResources().getString(R.string.lastUpdateCheck));
            else
                txtLastUpdate.setText(lastUpdate);

            if(description == null)
                txtDescription.setText(getResources().getString(R.string.descCheck));
            else
                txtDescription.setText(description);

            if(humidity == null)
                txtHumidity.setText(getResources().getString(R.string.humidityCheck));
            else
                txtHumidity.setText(humidity);

        }

        public void setIcon(Weather weather){
            // Get the weather description
            String str = weather.getDescription();
            // Look at the string to determine the proper icon
            String icon = checkString(str);
            // Get project resources
            Resources res = getResources();
            int resID = res.getIdentifier(icon, "drawable", getPackageName());
            imageView.setImageResource(resID);
        }

        public String checkString(String str){
            //Android Studio bitched that the files didn't start with letters, so I changed 1 to i and 0 to o
            //Need to change file names to something understandable
            if (str.contains("thunderstorm"))
                return "i1d";
            else if (str.contains("drizzle") || str.contains("shower"))
                return "o9d";
            else if (str.contains("freezing rain") || str.contains("snow") || str.contains("sleet"))
                return "i3d";
            else if ((str.contains("rain"))) //above weathers contain 'rain' but have other words in common, so if str contains none of those and it contains 'rain' here then the icon is known
                return "i0d";
            else if (str.contains("broken") || str.contains("overcast"))
                return "o4d";
            else if (str.equals("scattered clouds"))
                return "o3d";
            else if (str.equals("few clouds"))
                return "o2d";
            else if (str.equals("clear sky"))
                return "o1d";
            else
                return "s0d";
        }

        @Override
        protected String doInBackground(String... strings) {

            String stream = null;
            String urlString = strings[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);

            Log.e("Tag", urlString);

            return stream;

        }
    }

}
//Holden's commit test