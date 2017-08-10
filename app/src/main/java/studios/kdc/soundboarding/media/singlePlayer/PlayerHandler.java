package studios.kdc.soundboarding.media.singlePlayer;

import android.content.Context;
import android.media.MediaPlayer;

import studios.kdc.soundboarding.media.MediaPlayerHandler;

/**
 * Created by Michael on 8/10/2017.
 */

public class PlayerHandler extends MediaPlayerHandler {
    public PlayerHandler(Context context) {
        super(context);
    }

    @Override
    protected void setMediaPlayerListener() {
        mediaPlayer.setOnCompletionListener (new MediaPlayer.OnCompletionListener ( ) {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                MediaPlayerController.getInstance(context).onCompletion();
            }
        });

    }
}
