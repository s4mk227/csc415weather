package androidclassproject.weatherapplication;

/**
 * Created by mkhul on 11/4/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidclassproject.weatherapplication.Common.Common;
import androidclassproject.weatherapplication.Model.HourlyWeatherMap;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    private HourlyWeatherMap hour;
    private Context c;
    SharedPreferences prefs;
    int temp_setting;
    public HourAdapter(Context context, HourlyWeatherMap data)
    {

        inflater = LayoutInflater.from(context);
        hour = data;
        c = context;
        int x = 0;
    }
    public void setHour(HourlyWeatherMap data)
    {
        hour = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView hourTime;
        public TextView degreeTime;
        public ImageView imageView;
        public MyViewHolder(View view)
        {
            super(view);
            hourTime = view.findViewById(R.id.textviewWeatherTimeRow);
            degreeTime = view.findViewById(R.id.textviewWeatherDegree);
            imageView = view.findViewById(R.id.weatherImageRow);
        }
    }


    public HourAdapter(HourlyWeatherMap hour) {
        this.hour = hour;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hour_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i)
    {
        if(hour != null)
        {
            temp_setting = Integer.parseInt(prefs.getString("pref_temperature", "0"));
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
            display_celsius = Double.toString(temp_celsius);
            display_celsius = String.format("%s °C", display_celsius);
            if(temp_setting == 0)
                holder.degreeTime.setText(display_fahrenheit);
            else
                holder.degreeTime.setText(display_celsius);
            Picasso.with(c)
                    .load(Common.getImage(hour.getList().get(i).getWeather().get(0).getIcon()))
                    .into(holder.imageView);

        }
    }

    @Override
    public int getItemCount() {
        return hour.getCnt();
    }
    public void setPrefs(SharedPreferences prefs)
    {
        this.prefs = prefs;
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
