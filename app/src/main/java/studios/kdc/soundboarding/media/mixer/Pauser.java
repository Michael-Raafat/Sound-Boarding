package studios.kdc.soundboarding.media.mixer;

import android.util.Log;

/**
 * Created by Michael on 8/10/2017.
 */

public class Pauser{

    private boolean isPaused=false;

    public synchronized void pause(){
        isPaused=true;
    }

    public synchronized void resume(){
        isPaused=false;
        notifyAll();
    }

    public synchronized void look(){
        while(isPaused){
            try {
                wait();
            } catch (InterruptedException e) {
                Log.i("loong", "zew");
            }
        }
    }

}