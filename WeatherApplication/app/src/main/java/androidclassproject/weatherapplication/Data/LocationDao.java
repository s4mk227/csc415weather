package androidclassproject.weatherapplication.Data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM SavedLocation")
    List<SavedLocation> getall();

    @Delete
    void deleteLocation(SavedLocation... location);

    @Insert
    void insertLocation(SavedLocation... location);
}
