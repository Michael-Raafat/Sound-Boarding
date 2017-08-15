package studios.kdc.soundboarding.media.playerStrategy;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;

import java.io.IOException;
import java.net.URI;

class SdCardPlayer implements MediaPlayerStrategy {
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
}
