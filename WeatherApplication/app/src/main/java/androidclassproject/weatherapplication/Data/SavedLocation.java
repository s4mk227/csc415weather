package androidclassproject.weatherapplication.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class SavedLocation {
    @PrimaryKey
    private int id;

    //Table columns
    private String location;
    private double latitude;
    private double longitude;

    /* Getters and setters are required for Room Persistence library */
    //Getters
    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return location + " " + latitude + " " + longitude;
    }
}
