package studios.kdc.soundboarding;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.models.Group;
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

    public static DataServiceSingleton getInstance(){
        return instance;
    }

    public void loadDefaultDatabase() {
        database.execSQL("CREATE TABLE IF NOT EXISTS groups (name VARCHAR, color VARCHAR)");
        //TODO color
        database.execSQL("INSERT INTO groups (name, color) VALUES ('tracks', '#41494c')");
        database.execSQL("INSERT INTO groups (name, color) VALUES ('nature', '#41494c')");


        //TODO path of assets
        database.execSQL("CREATE TABLE IF NOT EXISTS tracks (name VARCHAR, duration INTEGER, path VARCHAR)");
        database.execSQL("INSERT INTO tracks (name, duration, path) VALUES ('hurricane', 4, '')");
        database.execSQL("INSERT INTO tracks (name, duration, path) VALUES ('wind01', 9, '')");
        database.execSQL("INSERT INTO tracks (name, duration, path) VALUES ('storm-thunder', 3, '')");

        database.execSQL("CREATE TABLE IF NOT EXISTS nature (name VARCHAR, duration INTEGER, path VARCHAR)");
        database.execSQL("INSERT INTO nature (name, duration, path) VALUES ('hurricane', 4, '')");
        database.execSQL("INSERT INTO nature (name, duration, path) VALUES ('wind01', 9, '')");
        database.execSQL("INSERT INTO nature (name, duration, path) VALUES ('storm-thunder', 3, '')");

    }

    public void addGroup(Group group) {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + group.getName() + " (name VARCHAR, duration INTEGER, path VARCHAR)");
        database.execSQL("INSERT INTO tables (name, duration) VALUES ('"+ group.getName() + "', "+group.getColor() +")");
    }

    public void addTrack(Track track) {
        database.execSQL("INSERT INTO "+ track.getGroup().getName() +" (name, duration, path) VALUES ('"+
                track.getName() +"', "+ String.valueOf(track.getTrackDuration()) +", "+ track.getPath() +")");
    }

    public void removeTrack(Track track) {
        database.execSQL("DELETE FROM "+ track.getGroup().getName() +" WHERE name = '"+ track.getName() +"'");
    }

    public void removeGroup(String groupName) {
        database.execSQL("DROP TABLE " + groupName);
    }

    public List<Pair<String, Integer>> getGroupsInDatabase() {
        Cursor cursor = database.rawQuery("SELECT * FROM groups", null);
        int nameIndex = cursor.getColumnIndex("name");
        int colorIndex = cursor.getColumnIndex("color");
        cursor.moveToFirst();
        List<Pair<String, Integer>> groups = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Pair<String, Integer> pair = new Pair<>(
                    cursor.getString(nameIndex),
                    cursor.getInt(colorIndex));
            groups.add(pair);
            cursor.moveToNext();
        }
        return groups;
    }


    public List<String[]> getTracksInTable(String tableName) {
        Cursor cursor = database.rawQuery("SELECT * FROM "+ tableName, null);
        int nameIndex = cursor.getColumnIndex("name");
        int durationIndex = cursor.getColumnIndex("duration");
        int pathIndex = cursor.getColumnIndex("duration");
        cursor.moveToFirst();
        List<String[]> tracks = new ArrayList<String[]>();
        while (!cursor.isAfterLast()) {
            String[] trackInfo = new String[3];
            trackInfo[0] = cursor.getString(nameIndex);
            trackInfo[1] = String.valueOf(cursor.getInt(durationIndex));
            trackInfo[2] = cursor.getString(pathIndex);
            tracks.add(trackInfo);
            cursor.moveToNext();
        }
        return tracks;
    }

    public List<List<String>> getDataMatches(String search) {
        Cursor groupsCursor = database.rawQuery("SELECT * FROM tables", null);
        int groupNameIndex = groupsCursor.getColumnIndex("name");
        groupsCursor.moveToFirst();
        List<List<String>> data = new ArrayList<>();
        while (!groupsCursor.isAfterLast()) {
            Cursor tracksCursor = database.rawQuery(
                    "SELECT * FROM " + groupsCursor.getString(groupNameIndex), null);
            int trackNameIndex = tracksCursor.getColumnIndex("name");
            tracksCursor.moveToFirst();
            List<String> infos = new ArrayList<String>();
            boolean flag = false;
            while (!tracksCursor.isAfterLast()) {
                if (tracksCursor.getString(trackNameIndex).contains(search)) {
                    if (!flag) {
                        infos.add(groupsCursor.getString(groupNameIndex));
                        flag = true;
                    }
                    infos.add(tracksCursor.getString(trackNameIndex));
                }
                tracksCursor.moveToNext();
            }
            if (!infos.isEmpty()) {
                data.add(infos);
            }
            groupsCursor.moveToNext();
        }
        return data;
    }




}
