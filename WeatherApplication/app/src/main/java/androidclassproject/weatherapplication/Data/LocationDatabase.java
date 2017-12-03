package androidclassproject.weatherapplication.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {SavedLocation.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    private static LocationDatabase instance;

    public abstract LocationDao locationDao();

    //TODO: Run database query in a separate thread
    public static LocationDatabase getLocationDatabase(Context context) {
        if (instance == null)
            instance = Room
                    .databaseBuilder(context, LocationDatabase.class, "location-database")
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}
