package studios.kdc.soundboarding.media.playerStrategy;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Michael on 8/15/2017.
 */

public class PlayerStrategyFactory {

    private Context context;
    public PlayerStrategyFactory(Context context){
        this.context = context;
    }

    public MediaPlayerStrategy createPlayerStrategy(String path) {
        switch (path) {
            case "assets":
                return new AssetsPlayer(context);
            case "mobile":
                return new SdCardPlayer();
            default:
                break;
        }
        return null;
    }

}
