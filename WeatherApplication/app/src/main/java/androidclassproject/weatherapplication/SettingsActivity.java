package androidclassproject.weatherapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    // Declare a sharedpreferences varaible.
    private SharedPreferences prefs;

    // Declare a theme varaible.
    int theme_settings = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the value of the theme.
        theme_settings = Integer.parseInt(prefs.getString("pref_theme", "0"));

        if (theme_settings == 0) {

            // Set the theme.
            setTheme(R.style.theme_light);

        } else if (theme_settings == 1){

            // Set the theme.
            setTheme(R.style.theme_dark);

        } else if (theme_settings == 2) {

            // Set the theme.
            setTheme(R.style.theme_sun);

        }

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        // Display the application icon in the title bar.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
}
