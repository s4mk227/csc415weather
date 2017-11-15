package androidclassproject.weatherapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.util.Date;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import android.util.Log;
import android.widget.Toast;

import androidclassproject.weatherapplication.Common.Common;
import androidclassproject.weatherapplication.Helper.Helper;
import androidclassproject.weatherapplication.Model.HourlyWeatherMap;
import androidclassproject.weatherapplication.Model.OpenWeatherMap;

public class DisplayWeather extends AppCompatActivity implements LocationListener {

    // Declare varaibles for the widgets on the DisplayWeather page.
    TextView txtCity, txtLastUpdate, txtDescription, txtHumidity, txtTime, txtCelsius;
    ImageView imageView;

    // Declare a location manager varible.
    LocationManager locationManager;
    String provider;

    // Declare variables to store the latitude and longitude vales.
    static double lat = 0;
    static double lng = 0;

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

        } else if (theme_settings == 1) {

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
        hourlyTexts = new TextView[]{(TextView) findViewById(R.id.textviewWeatherTime1),
                (TextView) findViewById(R.id.textviewWeatherTime2),(TextView)findViewById(R.id.textviewWeatherTime3)
                ,(TextView)findViewById(R.id.textviewWeatherTime4),(TextView)findViewById(R.id.textviewWeatherTime5),
                (TextView)findViewById(R.id.textviewWeatherTime6)
                ,(TextView)findViewById(R.id.textviewWeatherTime7),(TextView)findViewById(R.id.textviewWeatherTime8)};
        hourlyDegreeTexts = new TextView[]{(TextView) findViewById(R.id.textviewWeatherOne),
                (TextView) findViewById(R.id.textviewWeatherTwo), (TextView) findViewById(R.id.textviewWeatherThree)
                , (TextView) findViewById(R.id.textviewWeatherFour), (TextView) findViewById(R.id.textviewWeatherFive),
                (TextView) findViewById(R.id.textviewWeatherSix)
                , (TextView) findViewById(R.id.textviewWeatherSeven), (TextView) findViewById(R.id.textviewWeatherEight)};
        hourlyImages = new ImageView[]{(ImageView) findViewById(R.id.weatherImageOne), (ImageView) findViewById(R.id.weatherImageTwo)
                , (ImageView) findViewById(R.id.weatherImageThree), (ImageView) findViewById(R.id.weatherImageFour),
                (ImageView) findViewById(R.id.weatherImageFive), (ImageView) findViewById(R.id.weatherImageSix),
                (ImageView) findViewById(R.id.weatherImageSeven), (ImageView) findViewById(R.id.weatherImageEight)};
        // Declare variables for the widgets on the DisplayWeather page.
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtLastUpdate = (TextView) findViewById(R.id.txtLastUpdate);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtCelsius = (TextView) findViewById(R.id.txtCelsius);
        imageView = (ImageView) findViewById(R.id.imageView);


        // Get the user's coordinates.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lng)));

    }

    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();

        new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lng)));
        new GetHourlyWeather().execute(Common.hourlyRequest(String.valueOf(lat), String.valueOf(lng)));
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
        new GetHourlyWeather().execute(Common.hourlyRequest(String.valueOf(lat), String.valueOf(lng)));
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

            /*
            // Convert sunrise and sunset times to long values.
            if (device_time_calculate <= sunset_time && device_time_calculate <= sunrise_time) {
                Log.v("check_sunset", "check");
            }
            */

            temp_setting = Integer.parseInt(prefs.getString("pref_temperature", "0"));

            if (temp_setting == 0) {

                txtCelsius.setText(display_fahrenheit);

            } else {

                txtCelsius.setText(display_celsius);

            }

            time_setting = Integer.parseInt(prefs.getString("pref_temperature", "0"));

            time_display_sunrise = String.format("Sunrise: %s", Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise()));
            temp_time_display = Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise());
            
            Picasso.with(DisplayWeather.this)
                    .load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(imageView);

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

    private class GetHourlyWeather extends AsyncTask<String, Void, String> {

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
            Type mType = new TypeToken<HourlyWeatherMap>() {
            }.getType();

            // Create an openWeatherMap object.
            hourlyWeatherMap = gson.fromJson(s, mType);
            //
            // mLayoutManager = new LinearLayoutManager(t);
            //recyclerView.setLayoutManager(mLayoutManager);
            //mAdapter = new HourAdapter(t,hourlyWeatherMap);
            //recyclerView.setAdapter(mAdapter);
            for (int i = 0; i < hourlyWeatherMap.getCnt(); i++) {
                String text = hourlyWeatherMap.getList().get(i).getDt_txt();
                text = text.substring(hourlyWeatherMap.getList().get(i).getDt_txt().length() - 8, hourlyWeatherMap.getList().get(i).getDt_txt().length() - 3);
                text = HourParseHelper(text);
                hourlyTexts[i].setText(text);
                double check = 1.8;
                double temp_celsius = (int) hourlyWeatherMap.getList().get(i).getMain().getTemp();
                double temp_fahrenheit = temp_celsius * check + 32;
                temp_fahrenheit = Math.round(temp_fahrenheit);
                display_fahrenheit = Double.toString(temp_fahrenheit);
                display_fahrenheit = String.format("%s°F", display_fahrenheit);
                display_celsius = Double.toString(temp_celsius);
                display_celsius = String.format("%s °C", display_celsius);
                Picasso.with(DisplayWeather.this)
                        .load(Common.getImage(hourlyWeatherMap.getList().get(i).getWeather().get(0).getIcon()))
                        .into(hourlyImages[i]);
                if (temp_setting == 0) {

                    hourlyDegreeTexts[i].setText(display_fahrenheit);

                } else {

                    hourlyDegreeTexts[i].setText(display_celsius);

                }
            }


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

        String HourParseHelper(String s) {
            switch (s) {
                case "00:00":
                    s = "12:00am";
                    break;
                case "03:00":
                    s = "3:00am";
                    break;
                case "06:00":
                    s = "6:00am";
                    break;
                case "09:00":
                    s = "9:00am";
                    break;
                case "12:00":
                    s = "12:00pm";
                    break;
                case "15:00":
                    s = "3:00pm";
                    break;
                case "18:00":
                    s = "6:00pm";
                    break;
                case "21:00":
                    s = "9:00pm";
                    break;
                default:
                    s = "";
            }
            return s;
        }

    }
}
//Holden's commit test