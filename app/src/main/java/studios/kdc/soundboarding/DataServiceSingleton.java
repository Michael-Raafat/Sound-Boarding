package studios.kdc.soundboarding;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        database.execSQL("INSERT INTO groups (name, color) VALUES ('Car', '#106198')");
        database.execSQL("INSERT INTO groups (name, color) VALUES ('Nature', '#C9A1A1')");
        database.execSQL("INSERT INTO groups (name, color) VALUES ('Animal', '#46bde4')");
        database.execSQL("INSERT INTO groups (name, color) VALUES ('War', '#672543')");
        database.execSQL("INSERT INTO groups (name, color) VALUES ('Cartoon', '#c8498b')");

        database.execSQL("CREATE TABLE IF NOT EXISTS War" +
                " (name VARCHAR, duration INTEGER, path VARCHAR, extension VARCHAR, type VARCHAR)");
        database.execSQL("INSERT INTO War (name, duration, path, extension, type) VALUES" +
                " ('explode', 1, 'War/explode.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO War (name, duration, path, extension, type) VALUES" +
                " ('bazooka', 1, 'War/bazooka.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO War (name, duration, path, extension, type) VALUES" +
                " ('battle', 5, 'War/battle.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO War (name, duration, path, extension, type) VALUES" +
                " ('artillery', 1, 'War/artillery.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO War (name, duration, path, extension, type) VALUES" +
                " ('chainsaw', 4, 'War/chainsaw.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO War (name, duration, path, extension, type) VALUES" +
                " ('bomb', 2, 'War/bomb.mp3', 'mp3', 'assets')");


        database.execSQL("CREATE TABLE IF NOT EXISTS Nature" +
                " (name VARCHAR, duration INTEGER, path VARCHAR, extension VARCHAR, type VARCHAR)");
        database.execSQL("INSERT INTO Nature (name, duration, path, extension, type) VALUES" +
                " ('hurricane', 4, 'Nature/hurricane.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO Nature (name, duration, path, extension, type) VALUES" +
                " ('wind01', 9, 'Nature/wind01.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO Nature (name, duration, path, extension, type) VALUES" +
                " ('storm-thunder', 3, 'Nature/storm-thunder.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO Nature (name, duration, path, extension, type) VALUES" +
                " ('earthquake', 2, 'Nature/earthquake.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO Nature (name, duration, path, extension, type) VALUES" +
                " ('rain', 4, 'Nature/rain.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO Nature (name, duration, path, extension, type) VALUES" +
                " ('water', 50, 'Nature/water.mp3', 'mp3', 'assets')");

        database.execSQL("CREATE TABLE IF NOT EXISTS Animal" +
                " (name VARCHAR, duration INTEGER, path VARCHAR, extension VARCHAR, type VARCHAR)");
        database.execSQL("INSERT INTO Animal (name, duration, path, extension, type) VALUES" +
                " ('Dog', 47, 'Animal/Dog.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO Animal (name, duration, path, extension, type) VALUES" +
                " ('Kitty-noises', 3, 'Animal/Kitty-noises.mp3', 'mp3', 'assets')");

        database.execSQL("CREATE TABLE IF NOT EXISTS Car" +
                " (name VARCHAR, duration INTEGER, path VARCHAR, extension VARCHAR, type VARCHAR)");
        database.execSQL("INSERT INTO Car (name, duration, path, extension, type) VALUES" +
                " ('vehicle162', 6, 'Car/vehicle162.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO Car (name, duration, path, extension, type) VALUES" +
                " ('vehicle165', 6, 'Car/vehicle165.mp3', 'mp3', 'assets')");

        database.execSQL("CREATE TABLE IF NOT EXISTS Cartoon" +
                " (name VARCHAR, duration INTEGER, path VARCHAR, extension VARCHAR, type VARCHAR)");
        database.execSQL("INSERT INTO Cartoon (name, duration, path, extension, type) VALUES" +
                " ('bush_baby', 99, 'Cartoon/bush_baby.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO Cartoon (name, duration, path, extension, type) VALUES" +
                " ('popcorn', 75, 'Cartoon/popcorn.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO Cartoon (name, duration, path, extension, type) VALUES" +
                " ('Tom and Jerry', 24, 'Cartoon/Tom and Jerry.mp3', 'mp3', 'assets')");


    }

    public void addGroup(Group group) {
        database.execSQL("CREATE TABLE IF NOT EXISTS " +
                group.getName() + " (name VARCHAR, duration INTEGER, path VARCHAR, extension VARCHAR, type VARCHAR)");
        database.execSQL("INSERT INTO groups (name, color, imagePath) VALUES ('"+ group.getName() +
                "', '"+ Integer.toHexString(group.getColor()) +"', '"+ group.getImagePath()
                +"')");
    }

    public void addTrack(Track track, String groupName) {
        database.execSQL("INSERT INTO "+ groupName +" (name, duration, path, extension, type) VALUES ('"+
                track.getName() + "', " + String.valueOf(track.getTrackDuration()) + ", '"+
                track.getPath() +"', '"+ track.getExtension() +"', '"+
                track.getType() + "')");
    }

    public void removeTrack(Track track, String groupName) {
        database.execSQL("DELETE FROM "+ groupName +" WHERE name = '"+ track.getName() +"'");
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


    public List<String> getGroupNamesInDatabase() {
        Cursor cursor = database.rawQuery("SELECT * FROM groups", null);
        int nameIndex = cursor.getColumnIndex("name");
        cursor.moveToFirst();
        List<String> groups = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            groups.add(cursor.getString(nameIndex));
            cursor.moveToNext();
        }
        return groups;
    }

    public List<List<String>> getTracksInTable(String tableName) {
        Cursor cursor = database.rawQuery("SELECT * FROM "+ tableName, null);
        int nameIndex = cursor.getColumnIndex("name");
        int durationIndex = cursor.getColumnIndex("duration");
        int pathIndex = cursor.getColumnIndex("path");
        int extensionIndex = cursor.getColumnIndex("extension");
        int typeIndex = cursor.getColumnIndex("type");
        cursor.moveToFirst();
        List<List<String>> tracks = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            List<String> trackInfo = new ArrayList<>();
            trackInfo.add(cursor.getString(nameIndex));
            trackInfo.add(String.valueOf(cursor.getInt(durationIndex)));
            trackInfo.add(cursor.getString(pathIndex));
            trackInfo.add(cursor.getString(extensionIndex));
            trackInfo.add(cursor.getString(typeIndex));
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
        int extensionIndex = tracksCursor.getColumnIndex("extension");
        int typeIndex = tracksCursor.getColumnIndex("type");
        tracksCursor.moveToFirst();
        List<String> trackData = new ArrayList<>();
        trackData.add(tracksCursor.getString(nameIndex));
        trackData.add(String.valueOf(tracksCursor.getInt(durationIndex)));
        trackData.add(tracksCursor.getString(pathIndex));
        trackData.add(tracksCursor.getString(extensionIndex));
        trackData.add(tracksCursor.getString(typeIndex));
        return trackData;
    }


}
