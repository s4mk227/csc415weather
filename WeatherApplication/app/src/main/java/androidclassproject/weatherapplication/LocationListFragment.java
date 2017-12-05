package androidclassproject.weatherapplication;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.LinkedList;

import androidclassproject.weatherapplication.Data.LocationDao;
import androidclassproject.weatherapplication.Data.LocationDatabase;
import androidclassproject.weatherapplication.Data.SavedLocation;

public class LocationListFragment extends ListFragment {

    private LinkedList<SavedLocation> savedLocations = new LinkedList<>();
    private ArrayAdapter adapter;

    private double longitude;
    private double latitude;
    private String city;

    private LocationDatabase locationDatabase;
    private LocationDao locationDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        locationDatabase = LocationDatabase.getLocationDatabase(getContext());
        locationDao = locationDatabase.locationDao();

        Bundle extras = getActivity().getIntent().getExtras();
        longitude = extras.getDouble("current_longitude");
        latitude = extras.getDouble("current_latitude");
        city = extras.getString("current_city");

        savedLocations.addAll(locationDao.getall());
        savedLocations.addFirst(getCurrentLocation());

        adapter = new ArrayAdapter<SavedLocation>(getActivity(), android.R.layout.simple_list_item_1, savedLocations);
        setListAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.location_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_location:
                SavedLocation currentLocation = getCurrentLocation();
                locationDao.insertLocation(currentLocation);
                savedLocations.add(currentLocation);
                adapter.notifyDataSetChanged();
                return true;
        }
        return true;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent returnToDisplayWeather = new Intent(getActivity().getApplicationContext(), DisplayWeather.class);
        returnToDisplayWeather.putExtra("saved_longitude", longitude);
        returnToDisplayWeather.putExtra("saved latitude", latitude);
        if (position != 0)
            returnToDisplayWeather.putExtra("saved_city", city);
        else
            returnToDisplayWeather.putExtra("saved_city", "use_current_city");

        getActivity().setResult(1, returnToDisplayWeather);
        getActivity().finish();
    }

    private SavedLocation getCurrentLocation() {
        SavedLocation currentLocation = new SavedLocation();
        currentLocation.setLocation(city);
        currentLocation.setLongitude(longitude);
        currentLocation.setLatitude(latitude);
        return currentLocation;
    }
}
