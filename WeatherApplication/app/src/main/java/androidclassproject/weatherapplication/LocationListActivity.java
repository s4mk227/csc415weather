package androidclassproject.weatherapplication;


import android.app.Fragment;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.support.v7.app.AppCompatActivity;

public class LocationListActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


}
