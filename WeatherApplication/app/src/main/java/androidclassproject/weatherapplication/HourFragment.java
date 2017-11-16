package androidclassproject.weatherapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidclassproject.weatherapplication.Model.HourlyWeatherMap;

/**
 * Created by mkhul on 11/15/2017.
 */

public class HourFragment extends Fragment
        implements TextView.OnEditorActionListener{

    RecyclerView recyclerView;
    HourAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    HourlyWeatherMap hourlyWeatherMap;
    int temp_setting = 0;
    public HourFragment()
    {

    }
    public HourFragment(HourlyWeatherMap hour)
    {
        hourlyWeatherMap = hour;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        // inflate the layout for this

        View view = inflater.inflate(
                R.layout.hour_recyler, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new HourAdapter(view.getContext(),hourlyWeatherMap);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        // return the View for the layout
        return view;
    }
    public void setHourlyWeatherMap(HourlyWeatherMap hour)
    {
        hourlyWeatherMap = hour;
    }
}