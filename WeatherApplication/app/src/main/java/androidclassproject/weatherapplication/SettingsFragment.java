package androidclassproject.weatherapplication;

/**
 * Created by TommyArnzen on 10/13/17.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    // Declare a sharedpreferences varaible.
    private SharedPreferences prefs;

    // Declare a sharedpreferences variable.
    private SharedPreferences preference_listener;

    // Declare a theme varaible.
    int theme_settings = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        preference_listener = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onResume(){
        super.onResume();
        preference_listener.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {

        preference_listener.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preference_listener, String s) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        theme_settings = Integer.parseInt(prefs.getString("pref_theme", "0"));
        Intent i = new Intent(getActivity(), DisplayWeather.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getActivity().startActivity(i);
    }

}
