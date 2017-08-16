package studios.kdc.soundboarding.playerStrategy;

import android.content.Context;

import studios.kdc.soundboarding.media.singlePlayer.MediaPlayerStrategy;

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

    public MediaPlayerStrategy createPlayerStrategy(String type) {
        switch (type) {
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
