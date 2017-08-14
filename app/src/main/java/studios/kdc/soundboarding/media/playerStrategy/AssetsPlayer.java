package studios.kdc.soundboarding.media.playerStrategy;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class AssetsPlayer implements MediaPlayerStrategy {
    private Context context;

    public AssetsPlayer(Context context){
        this.context = context;
    }

    @Override
    public void playMedia(MediaPlayer mediaPlayer, String path) { //path here is grpname + trackname + extention
        ;
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(path);
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
