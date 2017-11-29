package androidclassproject.weatherapplication.Common;

/**
 * Created by TommyArnzen on 10/7/17.
 */

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    // http://openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b1b15e88fa797225412429c1c50c122a1


    public static String API_KEY = "6ba8de16bfaf48f0ba036856b1da58b0";
    public static String API_LINK = "http://api.openweathermap.org/data/2.5/weather";
    public static String API_LINK1 = "http://api.openweathermap.org/data/2.5/";
    @NonNull
    public static String apiRequest(String lat, String lng){

        StringBuilder sb = new StringBuilder(API_LINK);
        sb.append(String.format("?lat=%s&lon=%s&appid=%s&units=metric",lat,lng, API_KEY));
        return sb.toString();

    }
    @NonNull
    public static String hourlyRequest(String lat, String lng){

        StringBuilder sb = new StringBuilder(API_LINK1);
        sb.append(String.format("forecast?lat=%s&lon=%s&appid=%s&units=metric&cnt=16",lat,lng, API_KEY));
        return sb.toString();

    }
    public static String unixTimeStampToDateTime(double unixTimeStamp){

        DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
        Date date = new Date();
        date.setTime((long)unixTimeStamp * 1000);
        return dateFormat.format(date);

    }

    public static String getImage(String icon){

        return String.format("http://openweathermap.org/img/w/%s.png", icon);

    }

    public static String getDateNow(){

        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);

    }

}
