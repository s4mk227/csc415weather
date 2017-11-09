package androidclassproject.weatherapplication.Model;

import java.util.List;

/**
 * Created by mkhul on 11/1/2017.
 */

public class HourlyWeatherMap {
    private String country;
    private int cod;
    private int cnt;
    private double message;
    private List<MyList> list;
    public HourlyWeatherMap()
    {

    }
    public HourlyWeatherMap(int cod, double message,int cnt,List<MyList> list)
    {
        this.cod = cod;
        this.cnt = cnt;
        this.message = message;
        this.list = list;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public List<MyList> getList() {
        return list;
    }

    public void setList(List<MyList> list) {
        this.list = list;
    }
}
