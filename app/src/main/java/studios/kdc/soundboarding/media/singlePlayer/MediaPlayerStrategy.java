package studios.kdc.soundboarding.media.singlePlayer;


import android.media.MediaPlayer;

import java.io.InputStream;

public interface MediaPlayerStrategy {

   void playMedia(MediaPlayer mediaPlayer , String path);
   InputStream getInputStream(String path);
}
