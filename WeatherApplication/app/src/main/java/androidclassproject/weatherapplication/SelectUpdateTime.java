package androidclassproject.weatherapplication;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TommyArnzen on 12/2/17.
 */

public class SelectUpdateTime extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private SharedPreferences prefs;
    int hour_check = 0;
    int minute_check = 0;
    int theme_settings = 0;
    int theme_settings_check = 0;
    int hour;
    int minute;
    long time_difference;

    Button set_update_button, remove_update_button;
    TextView updateStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the value of the theme.
        theme_settings = Integer.parseInt(prefs.getString("pref_theme", "0"));

        set_theme();

        setContentView(R.layout.select_time);

        display();

        set_update_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View var) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(SelectUpdateTime.this, SelectUpdateTime.this,
                        hour, minute, true);
                timePickerDialog.show();

            }
        });

        final Intent intent = new Intent(this, WeatherUpdateService.class);
        intent.putExtra("start_time", "");

        remove_update_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View var) {

                stopService(intent);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("Hours", -1);
                editor.putInt("Minutes", -1);
                editor.commit();

                updateStatusTextView.setText("No Update Set.");
                remove_update_button.setVisibility(View.GONE);

            }
        });

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
                return true;
            case R.id.menu_select_update_time:
                startActivity(new Intent(getApplicationContext(), SelectUpdateTime.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void display() {

        set_update_button = (Button) findViewById(R.id.selectUpdateButton);
        remove_update_button = (Button) findViewById(R.id.removeUpdateButton);
        updateStatusTextView = (TextView) findViewById(R.id.updateStatusTextView);


        hour_check = prefs.getInt("Hours", -1);
        minute_check = prefs.getInt("Minutes", -1);

        String hour_check_value = "";
        String minute_check_value = "";

        if (hour_check == -1 || minute_check == -1) {

            updateStatusTextView.setText("No Update Set.");
            remove_update_button.setVisibility(View.GONE);

        } else {

            if (hour_check < 10) {

                hour_check_value = "0" + Integer.toString(hour_check);

            } else {

                hour_check_value = Integer.toString(hour_check);

            }

            if (minute_check < 10) {
                minute_check_value = "0" + Integer.toString(minute_check);
            } else {
                minute_check_value = Integer.toString(minute_check);
            }
            String time_setting = "Current Update Time: " + hour_check_value + ":" + minute_check_value;
            updateStatusTextView.setText(time_setting);
            remove_update_button.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        hour_check = prefs.getInt("Hours", -1);
        minute_check = prefs.getInt("Minutes", -1);

        if (hour_check != 0 && minute_check != 0) {
            Intent intent = new Intent(this, WeatherUpdateService.class);
            stopService(intent);

        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Hours", i);
        editor.putInt("Minutes", i1);
        editor.commit();

        // startService(new Intent(this, WeatherUpdateService.class));

        String hour_check_value = "";
        String minute_check_value = "";

        if (i < 10) {

          hour_check_value = "0" + Integer.toString(i);

        } else {

           hour_check_value = Integer.toString(i);
        }

        if (i1 < 10) {
            minute_check_value = "0" + Integer.toString(i1);
        } else {
            minute_check_value = Integer.toString(i1);
        }

        String start = hour_check_value + ":" + minute_check_value + ":00";

        Intent intent = new Intent(this, WeatherUpdateService.class);
        intent.putExtra("start_time", start);
        startService(intent);


        AlertDialog alertDialog = new AlertDialog.Builder(SelectUpdateTime.this).create();
        alertDialog.setTitle("Update Added");
        alertDialog.setMessage("A weather update will be sent everyday at " + i + ":" + minute_check_value + ".");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }

        });
        alertDialog.show();


        display();

    }

    private void set_theme() {

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

        } else if (theme_settings == 2) {

            // Set the theme.
            setTheme(R.style.theme_sun);

            // Set the theme settings check.
            theme_settings_check = 2;

        }
    }


}
