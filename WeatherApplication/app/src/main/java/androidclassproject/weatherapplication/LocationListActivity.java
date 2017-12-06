package androidclassproject.weatherapplication;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class LocationListActivity extends AppCompatActivity {

    int theme_settings = 0;
    int theme_settings_check = 0;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the default values for the preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the value of the theme.
        theme_settings = Integer.parseInt(prefs.getString("pref_theme", "0"));
        set_theme();

        // Display the application icon in the title bar.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        setContentView(R.layout.locationlist_activity);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.locationFragmentContainer);

        if (fragment == null) {
            fragment = new LocationListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.locationFragmentContainer, fragment)
                    .commit();
        }
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

            // Check the time.

            // Check if the time is before 12:00PM and after the sunrise.
            // Display the white theme.

            // Check if the time is after 12:00PM and before the sunset time.
            // Display the orange theme.

            // Check if the time is after the sunset time and before the sunrise time.
            // Display the dark theme.

            // Set the theme.
            setTheme(R.style.theme_sun);

            // Set the theme settings check.
            theme_settings_check = 2;

        }
    }

}
