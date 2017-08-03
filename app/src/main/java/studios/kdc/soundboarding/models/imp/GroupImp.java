package studios.kdc.soundboarding.models.imp;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import studios.kdc.soundboarding.DataServiceSingleton;
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
    private int color;

    /**
     * name of the group.
     */
    private String name;


    public GroupImp(int color, String name){
        tracks = new ArrayList<Track>();
        this.color = color;
        this.name = name;
    }

    public GroupImp(String name){
        tracks = new ArrayList<Track>();
        this.color = Color.parseColor("#41494c");
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
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public List<Track> getTracks() {
        return tracks;
    }

    @Override
    public void addTrack(Track track) {
        tracks.add(track);
        DataServiceSingleton.getInstance().addTrack(track);
    }

    @Override
    public void removeTrackByName(String name) {
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getName().equals(name)) {
                DataServiceSingleton.getInstance().removeTrack(tracks.get(i));
                tracks.remove(i);
            }
        }

    }

    @Override
    public Track getTrackByName(String name) {
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getName().equals(name)) {
                return tracks.get(i);
            }
        }
        return null;
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
