package studios.kdc.soundboarding;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
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
        database.execSQL("CREATE TABLE IF NOT EXISTS groups (name VARCHAR, color VARCHAR, imagePath VARCHAR)");
        //TODO color
        database.execSQL("INSERT INTO groups (name, color) VALUES ('Cars', '#ff00ff')");
        database.execSQL("INSERT INTO groups (name, color) VALUES ('Nature', '#0000ff')");
        database.execSQL("INSERT INTO groups (name, color) VALUES ('Animal', '#46bde4')");
        database.execSQL("INSERT INTO groups (name, color) VALUES ('War', '#672543')");

        database.execSQL("CREATE TABLE IF NOT EXISTS War (name VARCHAR, duration INTEGER, path VARCHAR)");
        database.execSQL("INSERT INTO War (name, duration, path) VALUES ('explode', 1, 'file:///assets/War/explode.mp3')");
        database.execSQL("INSERT INTO War (name, duration, path) VALUES ('bazooka', 1, 'file:///assets/War/bazooka.mp3')");
        database.execSQL("INSERT INTO War (name, duration, path) VALUES ('battle', 5, 'file:///assets/War/battle.mp3')");
        database.execSQL("INSERT INTO War (name, duration, path) VALUES ('artillery', 1, 'file:///assets/War/artillery.mp3')");
        database.execSQL("INSERT INTO War (name, duration, path) VALUES ('chainsaw', 4, 'file:///assets/War/chainsaw.mp3')");
        database.execSQL("INSERT INTO War (name, duration, path) VALUES ('bomb', 2, 'file:///assets/War/bomb.mp3')");


        //TODO path of assets
        database.execSQL("CREATE TABLE IF NOT EXISTS Nature (name VARCHAR, duration INTEGER, path VARCHAR)");
        database.execSQL("INSERT INTO Nature (name, duration, path) VALUES ('hurricane', 4, '')");
        database.execSQL("INSERT INTO Nature (name, duration, path) VALUES ('wind01', 9, '')");
        database.execSQL("INSERT INTO Nature (name, duration, path) VALUES ('storm-thunder', 3, '')");
        database.execSQL("INSERT INTO Nature (name, duration, path) VALUES ('earthquake', 2, '')");
        database.execSQL("INSERT INTO Nature (name, duration, path) VALUES ('rain', 4, '')");
        database.execSQL("INSERT INTO Nature (name, duration, path) VALUES ('water', 50, '')");

        database.execSQL("CREATE TABLE IF NOT EXISTS Animal (name VARCHAR, duration INTEGER, path VARCHAR)");
        database.execSQL("INSERT INTO Animal (name, duration, path) VALUES ('Dog', 47, '')");
        database.execSQL("INSERT INTO Animal (name, duration, path) VALUES ('Kitty-noises', 3, '')");

        database.execSQL("CREATE TABLE IF NOT EXISTS Cars (name VARCHAR, duration INTEGER, path VARCHAR)");
        database.execSQL("INSERT INTO Cars (name, duration, path) VALUES ('vehicle162', 47, '')");
        database.execSQL("INSERT INTO Cars (name, duration, path) VALUES ('vehicle165', 3, '')");


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

    public List<List<String>> getGroupsInDatabase() {
        Cursor cursor = database.rawQuery("SELECT * FROM groups", null);
        int nameIndex = cursor.getColumnIndex("name");
        int colorIndex = cursor.getColumnIndex("color");
        int imageIndex = cursor.getColumnIndex("imagePath");
        cursor.moveToFirst();
        List<List<String>> groups = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            List<String> temp = new ArrayList<>();
            temp.add(cursor.getString(nameIndex));
            temp.add(cursor.getString(colorIndex));
            temp.add(cursor.getString(imageIndex));
            groups.add(temp);
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

        Cursor groupsCursor = database.rawQuery("SELECT * FROM groups", null);
        int groupNameIndex = groupsCursor.getColumnIndex("name");
        groupsCursor.moveToFirst();
        List<List<String>> data = new ArrayList<>();
        while (!groupsCursor.isAfterLast()) {
            Cursor tracksCursor = database.rawQuery(
                    "SELECT * FROM " + groupsCursor.getString(groupNameIndex) + " WHERE name LIKE '%" + search + "%'", null);
            int trackNameIndex = tracksCursor.getColumnIndex("name");
            tracksCursor.moveToFirst();
            List<String> infos = new ArrayList<String>();
            boolean flag = false;
            while (!tracksCursor.isAfterLast()) {
                if (!flag) {
                    infos.add(groupsCursor.getString(groupNameIndex));
                    flag = true;
                }
                infos.add(tracksCursor.getString(trackNameIndex));
                tracksCursor.moveToNext();
            }
            if (!infos.isEmpty()) {
                data.add(infos);
            }
            groupsCursor.moveToNext();
        }
        return data;
    }

    public List<String> getGroupData(String groupName) {
        Cursor groupsCursor = database.rawQuery(
                "SELECT * FROM groups WHERE name = '" + groupName + "'", null);
        int groupNameIndex = groupsCursor.getColumnIndex("name");
        int colorIndex = groupsCursor.getColumnIndex("color");
        int imagePathIndex = groupsCursor.getColumnIndex("imagePath");
        groupsCursor.moveToFirst();
        List<String> groupData = new ArrayList<>();
        groupData.add(groupsCursor.getString(groupNameIndex));
        groupData.add(groupsCursor.getString(colorIndex));
        groupData.add(groupsCursor.getString(imagePathIndex));
        return groupData;
    }

    public List<String> getTrackData(String trackName, String groupName) {
        Cursor tracksCursor = database.rawQuery(
                "SELECT * FROM "+ groupName +" WHERE name = '" + trackName + "'", null);
        int nameIndex = tracksCursor.getColumnIndex("name");
        int durationIndex = tracksCursor.getColumnIndex("duration");
        int pathIndex = tracksCursor.getColumnIndex("path");
        tracksCursor.moveToFirst();
        List<String> trackData = new ArrayList<>();
        trackData.add(tracksCursor.getString(nameIndex));
        trackData.add(String.valueOf(tracksCursor.getInt(durationIndex)));
        trackData.add(tracksCursor.getString(pathIndex));
        return trackData;
    }


}
