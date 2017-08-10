package studios.kdc.soundboarding.media.mixer;

/**
 * Created by Michael on 8/10/2017.
 */

public class MixerRunnable implements Runnable {

    private  Pauser pauser;

    public MixerRunnable(Pauser pauser) {
        this.pauser = pauser;
    }

    @Override
    public void run() {
        while (true) {
            pauser.look();
        }
    }
}
