package studios.kdc.soundboarding.models;

import java.util.List;

/**
 * Created by Michael on 8/4/2017.
 */

public interface SelectedTracksContainer {
    void addTrack(SelectedTrack track);

    Track getTrackByName(String name);

    Track removeTrackByName(String name);

    void clearAndaddGroups(List<SelectedTrack> tracks);

    int getNumberOfTracks();

    List<SelectedTrack> getTracks();

}
