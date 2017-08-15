package studios.kdc.soundboarding.media.playerStrategy;

import android.content.Context;

/**
 * Created by Michael on 8/15/2017.
 */

public class PlayerStrategyFactory {


    private MediaPlayerStrategy assetsPlayer;
    private MediaPlayerStrategy sdCardPlayer;

    public PlayerStrategyFactory(Context context){

        this.assetsPlayer = new AssetsPlayer(context);
        this.sdCardPlayer = new SdCardPlayer(context);
    }

    public MediaPlayerStrategy createPlayerStrategy(String path) {
        switch (path) {
            case "assets":
                return assetsPlayer;
            case "mobile":
                return sdCardPlayer;
            default:
                break;
        }
        return null;
    }

}
