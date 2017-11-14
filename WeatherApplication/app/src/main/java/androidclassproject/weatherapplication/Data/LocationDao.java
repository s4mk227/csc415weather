package androidclassproject.weatherapplication.Data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM SavedLocation")
    Cursor getLocationCursor();

    @Query("SELECT * FROM SavedLocation")
    List<SavedLocation> getAll();

    //@Query("SELECT location FROM SavedLocation")
    //List<SavedLocation> getAllLocations();

    @Delete
    void deleteLocation(SavedLocation... location);

    @Insert
    void insertLocation(SavedLocation... location);
}