package studios.kdc.soundboarding;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.models.Group;
import studios.kdc.soundboarding.models.SelectedTrack;
import studios.kdc.soundboarding.models.Track;


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

    /**
     * application context.
     */
    private Context context;


    private DataServiceSingleton(SQLiteDatabase database, Context context){

        this.database = database;
        this.context = context;
    }

    public static DataServiceSingleton getInstance(SQLiteDatabase database, Context context){
        if (instance == null) {
            instance = new DataServiceSingleton(database, context);
        }
        return instance;
    }

    public static DataServiceSingleton getInstance(){
        return instance;
    }

    public void loadDefaultDatabase() {
        database.execSQL("CREATE TABLE IF NOT EXISTS groups (name VARCHAR, color VARCHAR, imagePath VARCHAR)");
        DataController.getInstance().createDataGroup("Animal_Bird", Color.parseColor("#C9A1A1"));
        DataController.getInstance().createDataGroup("Cartoon", Color.parseColor("#c8498b"));
        DataController.getInstance().createDataGroup("Nature", Color.parseColor("#46bde4"));
        DataController.getInstance().createDataGroup("Sound_Effects", Color.parseColor("#106198"));
        DataController.getInstance().createDataGroup("War", Color.parseColor("#672543"));

        //Animal_Bird
        DataController.getInstance().createDataTrack(context, "Bat","Animal_Bird/Bat.m4a","assets", "Animal_Bird");
        DataController.getInstance().createDataTrack(context, "Bird Chirping","Animal_Bird/Bird Chirping.m4a","assets", "Animal_Bird");
        DataController.getInstance().createDataTrack(context, "Bullfrog","Animal_Bird/Bullfrog.m4a","assets", "Animal_Bird");
        DataController.getInstance().createDataTrack(context, "Cat","Animal_Bird/Cat.mp3","assets", "Animal_Bird");
        DataController.getInstance().createDataTrack(context, "Chicken","Animal_Bird/Chicken.m4a","assets", "Animal_Bird");
        DataController.getInstance().createDataTrack(context, "Dog","Animal_Bird/Dog.mp3","assets", "Animal_Bird");
        DataController.getInstance().createDataTrack(context, "Dolphin","Animal_Bird/Dolphin.m4a","assets", "Animal_Bird");
        DataController.getInstance().createDataTrack(context, "Gremlin Growling","Animal_Bird/Gremlin Growling.m4a","assets", "Animal_Bird");
        DataController.getInstance().createDataTrack(context, "Raven","Animal_Bird/Raven.m4a","assets", "Animal_Bird");
        DataController.getInstance().createDataTrack(context, "Seagull","Animal_Bird/Seagull.m4a","assets", "Animal_Bird");
        DataController.getInstance().createDataTrack(context, "Wolf","Animal_Bird/Wolf.m4a","assets", "Animal_Bird");
        //WAR
        DataController.getInstance().createDataTrack(context, "Alarm","War/Alarm.m4a","assets", "War");
        DataController.getInstance().createDataTrack(context, "Artillery","War/Artillery.mp3","assets", "War");
        DataController.getInstance().createDataTrack(context, "Battle","War/Battle.mp3","assets", "War");
        DataController.getInstance().createDataTrack(context, "Bazooka","War/Bazooka.mp3","assets", "War");
        DataController.getInstance().createDataTrack(context, "Bomb","War/Bomb.mp3","assets", "War");
        DataController.getInstance().createDataTrack(context, "Chainsaw","War/Chainsaw.mp3","assets", "War");
        DataController.getInstance().createDataTrack(context, "Explode","War/Explode.mp3","assets", "War");
        DataController.getInstance().createDataTrack(context, "GunShots","War/GunShots.m4a","assets", "War");
        //Nature
        DataController.getInstance().createDataTrack(context, "Avalanche","Nature/Avalanche.m4a","assets", "Nature");
        DataController.getInstance().createDataTrack(context, "Earthquake","Nature/Earthquake.mp3","assets", "Nature");
        DataController.getInstance().createDataTrack(context, "Hurricane","Nature/Hurricane.mp3","assets", "Nature");
        DataController.getInstance().createDataTrack(context, "Rain","Nature/Rain.mp3","assets", "Nature");
        DataController.getInstance().createDataTrack(context, "Thunder","Nature/Thunder.mp3","assets", "Nature");
        DataController.getInstance().createDataTrack(context, "Water","Nature/Water.mp3","assets", "Nature");
        DataController.getInstance().createDataTrack(context, "Waterfall","Nature/Waterfall.m4a","assets", "Nature");
        DataController.getInstance().createDataTrack(context, "Wind","Nature/Wind.mp3","assets", "Nature");
        //Cartoon
        DataController.getInstance().createDataTrack(context, "Bomb Drop","Cartoon/Bomb Drop.m4a","assets", "Cartoon");
        DataController.getInstance().createDataTrack(context, "Bush Baby","Cartoon/Bush Baby.mp3","assets", "Cartoon");
        DataController.getInstance().createDataTrack(context, "Fight","Cartoon/Fight.m4a","assets", "Cartoon");
        DataController.getInstance().createDataTrack(context, "Pop corn","Cartoon/Pop corn.mp3","assets", "Cartoon");
        DataController.getInstance().createDataTrack(context, "Pop Gun","Cartoon/Pop Gun.m4a","assets", "Cartoon");
        DataController.getInstance().createDataTrack(context, "Scream","Cartoon/Scream.m4a","assets", "Cartoon");
        DataController.getInstance().createDataTrack(context, "Splat and Spin","Cartoon/Splat and Spin.m4a","assets", "Cartoon");
        DataController.getInstance().createDataTrack(context, "Sport Whistle","Cartoon/Sport Whistle.m4a","assets", "Cartoon");
        //Sound Effects
        DataController.getInstance().createDataTrack(context, "Aaaw","Sound_Effects/Aaaw.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Bruh","Sound_Effects/Bruh.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Camera Shutter","Sound_Effects/Camera Shutter.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Clapping people","Sound_Effects/Clapping people.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Count Down","Sound_Effects/Count Down.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Dj Stop","Sound_Effects/Dj Stop.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "DrumRoll","Sound_Effects/DrumRoll.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Drums","Sound_Effects/Drums.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "FlashBack","Sound_Effects/FlashBack.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Illuminati","Sound_Effects/Illuminati.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Kung Fu","Sound_Effects/Kung Fu.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Ping","Sound_Effects/Ping.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Punch","Sound_Effects/Punch.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Pup","Sound_Effects/Pup.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Sad Trombone","Sound_Effects/Sad Trombone.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Tape Rewind","Sound_Effects/Tape Rewind.mp3","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Well sound","Sound_Effects/Well sound.m4a","assets", "Sound_Effects");
        DataController.getInstance().createDataTrack(context, "Wha wha","Sound_Effects/Wha wha.m4a","assets", "Sound_Effects");



        database.execSQL("CREATE TABLE IF NOT EXISTS Car" +
                " (name VARCHAR, duration INTEGER, path VARCHAR, extension VARCHAR, type VARCHAR)");
        database.execSQL("INSERT INTO Car (name, duration, path, extension, type) VALUES" +
                " ('vehicle162', 6, 'Car/vehicle162.mp3', 'mp3', 'assets')");
        database.execSQL("INSERT INTO Car (name, duration, path, extension, type) VALUES" +
                " ('vehicle165', 6, 'Car/vehicle165.mp3', 'mp3', 'assets')");

    }

    public void addGroup(Group group) {
        database.execSQL("CREATE TABLE IF NOT EXISTS " +
                group.getName() + " (name VARCHAR, duration INTEGER, path VARCHAR, extension VARCHAR, type VARCHAR)");
        database.execSQL("INSERT INTO groups (name, color, imagePath) VALUES ('"+ group.getName() +
                "', '"+ "#" + Integer.toHexString(group.getColor()) +"', '"+ group.getImagePath()
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

    public void deleteSavedTrack(String trackName) {
        database.execSQL("DELETE FROM SavedTracks WHERE name = '"+ trackName +"'");
        database.execSQL("DROP TABLE " + trackName);
    }

    public void removeGroup(String groupName) {
        database.execSQL("DROP TABLE " + groupName);
        database.execSQL("DELETE FROM groups WHERE name = '"+ groupName +"'");
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
