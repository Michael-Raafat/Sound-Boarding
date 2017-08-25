package studios.kdc.soundboarding.playerStrategy;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.InputStream;

import studios.kdc.soundboarding.media.singlePlayer.MediaPlayerStrategy;

public class AssetsPlayer implements MediaPlayerStrategy {
    private Context context;

    public AssetsPlayer(Context context){
        this.context = context;
    }

    @Override
    public void playMedia(MediaPlayer mediaPlayer, String path) {
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(path);
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public InputStream getInputStream(String path) {
        AssetManager am = context.getAssets();
        InputStream inputStream;
        try {
            inputStream = am.open(path);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
