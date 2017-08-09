package studios.kdc.soundboarding.models;

import java.util.List;

/**
 * Created by Michael on 8/4/2017.
 */

public interface SelectedTracksContainer {
    void addTrack(Track track);

    Track getTrackByName(String name);

    Track removeTrackByName(String name);

    void clearAndaddGroups(List<Track> tracks);

    int getNumberOfTracks();

    List<Track> getTracks();

}
