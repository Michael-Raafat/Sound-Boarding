package studios.kdc.soundboarding;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Utils {

    public  static final int SECOND_PIXEL_RATIO = 10;  //timeline 1 second = 10px
    public  static final int TIMELINE_LENGTH_LIMIT = (SECOND_PIXEL_RATIO * 600) ;

    public static byte[] toByteArray(InputStream in) throws IOException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read = 0;
            byte[] buffer = new byte[1024];
            while (read != -1) {
                read = in.read(buffer);
                if (read != -1)
                    out.write(buffer, 0, read);
            }
            out.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
      return null;
    }

    public static int getScreenWidth(Activity activity) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getTrackDuration(Context context, String path){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context ,Uri.parse(path));
            mediaPlayer.prepare();
            int duration = (int) Math.ceil((mediaPlayer.getDuration() / 1000));
            mediaPlayer.release();
            return duration;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
