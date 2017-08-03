package studios.kdc.soundboarding;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import studios.kdc.soundboarding.models.Track;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Michael on 8/2/2017.
 * singleton class.
 */
public class DataServiceSingleton {
    /**
     * instance of DataServiceSingleton.
     */
    private static DataServiceSingleton instance;
    /**
     * database.
     */
    private static SQLiteDatabase database;

    private DataServiceSingleton(SQLiteDatabase database){
        this.database = database;
    }

    public static DataServiceSingleton getInstance(SQLiteDatabase database){
        if (instance == null) {
            instance = new DataServiceSingleton(database);
        }
        return instance;
    }


    public void loadDefaultDatabase(Activity activity) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("studios.kdc.soundboarding", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("Creation", true).apply();

        database.execSQL("CREATE TABLE tables (name VARCHAR, color INTEGER)");
        database.execSQL("INSERT INTO tables (name, duration) VALUES ('all', 0)");
        database.execSQL("INSERT INTO tables (name, duration) VALUES ('nature', 0)");


        database.execSQL("CREATE TABLE all (name VARCHAR, duration INTEGER, path VARCHAR)");
        database.execSQL("INSERT INTO all (name, duration) VALUES ('hurricane', 4)");
        database.execSQL("INSERT INTO all (name, duration) VALUES ('wind01', 9)");
        database.execSQL("INSERT INTO all (name, duration) VALUES ('storm-thunder', 3)");

        database.execSQL("CREATE TABLE nature (name VARCHAR, duration INTEGER, path VARCHAR)");
        database.execSQL("INSERT INTO nature (name, duration) VALUES ('hurricane', 4)");
        database.execSQL("INSERT INTO nature (name, duration) VALUES ('wind01', 9)");
        database.execSQL("INSERT INTO nature (name, duration) VALUES ('storm-thunder', 3)");

    }

    public void addGroup(String name) {
        database.execSQL("CREATE TABLE " + name + " (name VARCHAR, duration INTEGER(4), path VARCHAR)");
    }

    public void addTrack(String groupName, Track track) {
        database.execSQL("INSERT INTO "+ groupName +" (name, duration, path) VALUES ('"+
                track.getName() +"', "+ String.valueOf(track.getTrackDuration()) +", "+ track.getPath() +")");
    }

    public void removeTrack(String groupName, String trackname) {
        database.execSQL("DELETE FROM "+ groupName +" WHERE name = '"+ trackname +"'");
    }

    public void removeGroup(String groupName) {
        database.execSQL("DROP TABLE " + groupName);
    }


}
