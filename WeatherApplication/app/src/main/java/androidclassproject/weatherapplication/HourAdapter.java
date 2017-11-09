package androidclassproject.weatherapplication;

/**
 * Created by mkhul on 11/4/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import androidclassproject.weatherapplication.Model.HourlyWeatherMap;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    private HourlyWeatherMap hour;
    TextView t;
    int c = 0;
    public HourAdapter(Context context, HourlyWeatherMap data){
        inflater = LayoutInflater.from(context);
        hour = data;
    }
    public void setHour(HourlyWeatherMap data)
    {
        hour = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView hourTime;
        public TextView degreeTime;
        public MyViewHolder(View view) {
            super(view);
            hourTime = view.findViewById(R.id.textviewWeatherTimeRow);
            degreeTime = view.findViewById(R.id.textviewWeatherDegree);
        }
    }


    public HourAdapter(HourlyWeatherMap hour) {
        this.hour = hour;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hour_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i)
    {
        if(hour != null)
        {
            String display_fahrenheit = "";
            String display_celsius = "";
            String text = hour.getList().get(i).getDt_txt();
            text = text.substring(hour.getList().get(i).getDt_txt().length() - 8, hour.getList().get(i).getDt_txt().length() - 3);
            text = HourParseHelper(text);
            holder.hourTime.setText(text);
            double check = 1.8;
            double temp_celsius = (int) hour.getList().get(i).getMain().getTemp();
            double temp_fahrenheit = temp_celsius * check + 32;
            temp_fahrenheit = Math.round(temp_fahrenheit);
            display_fahrenheit = Double.toString(temp_fahrenheit);
            display_fahrenheit = String.format("%s°F", display_fahrenheit);
            holder.degreeTime.setText(display_fahrenheit);
        }
    }

    @Override
    public int getItemCount() {
        //return skillsList.size();
        return 0;
    }
    String HourParseHelper(String s)
    {
        switch (s)
        {
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
