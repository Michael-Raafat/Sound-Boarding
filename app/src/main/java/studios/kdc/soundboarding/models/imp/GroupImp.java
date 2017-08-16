package studios.kdc.soundboarding.models.imp;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.models.Group;
import studios.kdc.soundboarding.models.Track;

/**
 * Created by Michael on 8/2/2017.
 */
public class GroupImp implements Group {

    private String imagePath;
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


    public GroupImp(List<String> data) {
        tracks = new ArrayList<Track>();
        this.name = data.get(0);
        this.color = Color.parseColor(data.get(1));
        this.imagePath = data.get(2);
    }
    public GroupImp(int color, String name, String imagePath){
        tracks = new ArrayList<Track>();
        this.color = color;
        this.name = name;
        this.imagePath = imagePath;
    }

    public GroupImp(String name, String color){
        tracks = new ArrayList<Track>();
        this.color = Color.parseColor(color);
        this.imagePath = "";
        this.name = name;
    }

    public GroupImp(String name, int color){
        tracks = new ArrayList<Track>();
        this.color = color;
        this.imagePath = "";
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
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
    }

    @Override
    public void removeTrackByName(String name) {
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getName().equals(name)) {
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

    /*@Override
    public void sortTracksByName() {
        Collections.sort(this.tracks);
    }*/

    @Override
    public int getNumberOfTracks() {
        return tracks.size();
    }

    @Override
    public void clearAndAddTracks(List<Track> tracks) {
        this.tracks.clear();
        this.tracks.addAll(tracks);
    }
}
