package studios.kdc.soundboarding.media.playerStrategy;


import android.media.MediaPlayer;

import java.io.IOException;

class SdCardPlayer implements MediaPlayerStrategy {
    @Override
    public void playMedia(MediaPlayer mediaPlayer, String path) {
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
