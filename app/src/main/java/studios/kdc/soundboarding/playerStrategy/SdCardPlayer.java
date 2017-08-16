package studios.kdc.soundboarding.playerStrategy;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import studios.kdc.soundboarding.media.singlePlayer.MediaPlayerStrategy;


public class SdCardPlayer implements MediaPlayerStrategy {
    private Context context;

    SdCardPlayer(Context context){
        this.context = context;
    }



    @Override
    public void playMedia(MediaPlayer mediaPlayer, String path) {
        try {
            mediaPlayer.setDataSource(context , Uri.parse(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream getInputStream(String path) {
        try {
          return context.getContentResolver().openInputStream(Uri.parse(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
