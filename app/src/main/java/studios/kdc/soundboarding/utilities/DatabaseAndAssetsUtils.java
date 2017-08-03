package studios.kdc.soundboarding.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import studios.kdc.soundboarding.MainActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Michael on 8/2/2017.
 */

public class DatabaseAndAssetsUtils {

    public static void loadDefaultDatabase(Activity activity, SQLiteDatabase database) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("studios.kdc.soundboarding", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("Creation", true).apply();

        database.execSQL("CREATE TABLE all (name VARCHAR, duration INTEGER(4), path VARCHAR)");
        database.execSQL("INSERT INTO all (name, age) VALUES ('hurricane', 4)");
        database.execSQL("INSERT INTO all (name, age) VALUES ('wind01', 9)");
        database.execSQL("INSERT INTO all (name, age) VALUES ('storm-thunder', 3)");

        database.execSQL("CREATE TABLE nature (name VARCHAR, duration INTEGER(4), path VARCHAR)");
        database.execSQL("INSERT INTO nature (name, age) VALUES ('hurricane', 4)");
        database.execSQL("INSERT INTO nature (name, age) VALUES ('wind01', 9)");
        database.execSQL("INSERT INTO nature (name, age) VALUES ('storm-thunder', 3)");

    }

    public static void addAssets(Activity activity){

    }





}
