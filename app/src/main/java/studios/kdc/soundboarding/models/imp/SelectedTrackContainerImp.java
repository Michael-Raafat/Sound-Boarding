package studios.kdc.soundboarding.models.imp;

import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.models.SelectedTrack;
import studios.kdc.soundboarding.models.Track;
import studios.kdc.soundboarding.models.SelectedTracksContainer;

/**
 * Created by Michael on 8/4/2017.
 */

public class SelectedTrackContainerImp implements SelectedTracksContainer {

    private List<SelectedTrack> selectedTracks;

    private static SelectedTrackContainerImp instance;
    private SelectedTrackContainerImp(){
        selectedTracks = new ArrayList<>();
    }

    public static SelectedTrackContainerImp getInstance() {
        if (instance == null) {
            instance = new SelectedTrackContainerImp();
        }
        return instance;
    }
    public static void deleteInstance() {
        instance = null;
    }

    @Override
    public void addTrack(SelectedTrack track) {
        this.selectedTracks.add(track);
    }

    @Override
    public Track getTrackByName(String name) {
        for (int i = 0; i < selectedTracks.size(); i++) {
            if (this.selectedTracks.get(i).getName().equals(name)){
                return this.selectedTracks.get(i);
            }
        }
        return null;
    }

    @Override
    public Track removeTrackByName(String name) {
        for (int i = 0; i < this.selectedTracks.size(); i++) {
            if (this.selectedTracks.get(i).getName().equals(name)){
                Track temp = this.selectedTracks.get(i);
                this.selectedTracks.remove(i);
                return temp;
            }
        }
        return null;
    }

    @Override
    public void clearAndAddTracks(List<SelectedTrack> tracks) {
        this.selectedTracks.clear();
        this.selectedTracks.addAll(tracks);
    }

    @Override
    public int getNumberOfTracks() {
        return this.selectedTracks.size();
    }

    @Override
    public List<SelectedTrack> getTracks() {
        return this.selectedTracks;
    }

    @Override
    public SelectedTrack getTrackByIndex(int position) {
        return this.selectedTracks.get(position);
    }

    @Override
    public List<Integer> getTrackPositionsByName(String name) {
        List<Integer> list = new ArrayList<>();
         for (int i =  this.selectedTracks.size() - 1; i >= 0; i--) {
            if (this.selectedTracks.get(i).getName().equals(name))
               list.add(i);
        }
        return list;
    }


}
