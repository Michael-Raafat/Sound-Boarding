package studios.kdc.soundboarding;


import android.app.Activity;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Utils {

    public  static final int SECOND_PIXEL_RATIO = 10;  //timeline 1 second = 10px
    public  static final int TIMELINE_LENGTH_LIMIT = (SECOND_PIXEL_RATIO * 600) ;

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1)
                out.write(buffer,0,read);
        }
        out.close();
        return out.toByteArray();
    }
/*
    public static int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);
        percentage =(((double)currentSeconds)/totalSeconds)*100;
        return percentage.intValue();
    }
/*
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration;
        totalDuration = (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        return currentDuration * 1000;
    }
*/
    public static int getScreenWidth(Activity activity) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
