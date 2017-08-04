package studios.kdc.soundboarding.models.imp;

import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.models.Track;
import studios.kdc.soundboarding.models.TracksContainer;

/**
 * Created by Michael on 8/4/2017.
 */

public class TrackContainerImp implements TracksContainer {

    private List<Track> selectedTracks;

    public TrackContainerImp(){
        selectedTracks = new ArrayList<>();
    }

    @Override
    public void addTrack(Track track) {
        selectedTracks.add(track);
    }

    @Override
    public Track getTrackByName(String name) {
        for (int i = 0; i < selectedTracks.size(); i++) {
            if (selectedTracks.get(i).getName().equals(name)){
                return selectedTracks.get(i);
            }
        }
        return null;
    }

    @Override
    public Track removeTrackByName(String name) {
        for (int i = 0; i < selectedTracks.size(); i++) {
            if (selectedTracks.get(i).getName().equals(name)){
                Track temp = selectedTracks.get(i);
                selectedTracks.remove(i);
                return temp;
            }
        }
        return null;
    }

    @Override
    public void clearAndaddGroups(List<Track> tracks) {
        selectedTracks.clear();
        selectedTracks.addAll(tracks);
    }

    @Override
    public int getNumberOfTracks() {
        return selectedTracks.size();
    }
}
