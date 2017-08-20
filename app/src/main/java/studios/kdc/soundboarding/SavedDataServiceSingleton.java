package studios.kdc.soundboarding;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.models.SelectedTrack;

/**
 * Created by Michael on 8/20/2017.
 */

public class SavedDataServiceSingleton {

    /**
     * instance of DataServiceSingleton.
     */
    private static SavedDataServiceSingleton instance;
    /**
     * database.
     */
    private static SQLiteDatabase database;

    private SavedDataServiceSingleton(SQLiteDatabase database){
        this.database = database;
    }

    public static SavedDataServiceSingleton getInstance(SQLiteDatabase database){
        if (instance == null) {
            instance = new SavedDataServiceSingleton(database);
        }
        return instance;
    }

    public static SavedDataServiceSingleton getInstance(){
        return instance;
    }


    private void addSavedGroup() {
        database.execSQL("CREATE TABLE IF NOT EXISTS SavedTracks" +
                " (name VARCHAR)");
    }


    public List<String> getSavedTracksNamesInDatabase() {
        Cursor cursor = database.rawQuery("SELECT * FROM SavedTracks", null);
        int nameIndex = cursor.getColumnIndex("name");
        cursor.moveToFirst();
        List<String> savedTracks = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            savedTracks.add(cursor.getString(nameIndex));
            cursor.moveToNext();
        }
        return savedTracks;
    }

    public void saveNewTrack(String trackName, List<SelectedTrack> tracks) {
        database.execSQL("INSERT INTO SavedTracks (name) VALUES ('"+ trackName +
                "')");
        database.execSQL("CREATE TABLE IF NOT EXISTS "+ trackName
                +" (name VARCHAR, startPoint INTEGER," +
                " endPoint INTEGER, path VARCHAR, type VARCHAR, groupName VARCHAR, duration INTEGER, extension VARCHAR, volume INTEGER)");
        for ( SelectedTrack selectedTrack : tracks) {
            database.execSQL("INSERT INTO "+ trackName
                    +" (name, startPoint, endPoint, path, type, groupName, duration, extension, volume) VALUES ('"+ selectedTrack.getName() +
                    "', " + selectedTrack.getStartPoint() + ", " + selectedTrack.getEndPoint() + ", '"+
                    selectedTrack.getPath() + "', '" + selectedTrack.getType() + "', '"+ selectedTrack.getGroupName()
                    +"', " + selectedTrack.getTrackDuration() + ", '"+ selectedTrack.getExtension() +"', " + selectedTrack.getVolume() +")");
        }
    }

    public void deleteSavedTrack(String trackName) {
        database.execSQL("DELETE FROM SavedTracks WHERE name = '"+ trackName +"'");
        database.execSQL("DROP TABLE " + trackName);
    }


    public List<List<String>> getSelectedTracksInSavedTracksTable(String tableName) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);
        int nameIndex = cursor.getColumnIndex("name");
        int trackDurationIndex = cursor.getColumnIndex("duration");
        int extensionIndex = cursor.getColumnIndex("extension");
        int startPointIndex = cursor.getColumnIndex("startPoint");
        int endPointIndex = cursor.getColumnIndex("endPoint");
        int pathIndex = cursor.getColumnIndex("path");
        int typeIndex = cursor.getColumnIndex("type");
        int volumeIndex = cursor.getColumnIndex("volume");
        int groupNameIndex = cursor.getColumnIndex("groupName");
        cursor.moveToFirst();
        List<List<String>> selectedTracks = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            List<String> trackInfo = new ArrayList<>();
            trackInfo.add(cursor.getString(nameIndex));
            trackInfo.add(String.valueOf(cursor.getInt(trackDurationIndex)));
            trackInfo.add(cursor.getString(pathIndex));
            trackInfo.add(cursor.getString(extensionIndex));
            trackInfo.add(cursor.getString(typeIndex));
            trackInfo.add(String.valueOf(cursor.getInt(startPointIndex)));
            trackInfo.add(String.valueOf(cursor.getInt(endPointIndex)));
            trackInfo.add(String.valueOf(cursor.getInt(volumeIndex)));
            trackInfo.add(cursor.getString(groupNameIndex));
            selectedTracks.add(trackInfo);
            cursor.moveToNext();
        }
        return selectedTracks;
    }

    public void loadDefaultDatabase() {
        this.addSavedGroup();
    }
}
