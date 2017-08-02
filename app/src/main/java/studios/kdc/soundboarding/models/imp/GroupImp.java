package studios.kdc.soundboarding.models.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import studios.kdc.soundboarding.models.Group;
import studios.kdc.soundboarding.models.Track;

/**
 * Created by Michael on 8/2/2017.
 */
public class GroupImp implements Group {
    /**
     * list of tracks.
     */
    private List<Track> tracks;

    /**
     * color of the group.
     */
    private String color;

    /**
     * name of the group.
     */
    private String name;


    public GroupImp(String color, String name){
        tracks = new ArrayList<Track>();
        this.color = color;
        this.name = name;
    }

    public GroupImp(String name){
        tracks = new ArrayList<Track>();
        this.color = "#41494c";
        this.name = name;
    }


    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public List<Track> getTracks() {
        return tracks;
    }

    @Override
    public void addTrack(Track track) {
        tracks.add(track);
    }

    @Override
    public void removeTrackByName(String name) {
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getName() == name) {
                tracks.remove(i);
            }
        }
    }

    @Override
    public void sortTracksByName() {
        Collections.sort(this.tracks);
    }

    @Override
    public int getNumberOfTracks() {
        return tracks.size();
    }
}
