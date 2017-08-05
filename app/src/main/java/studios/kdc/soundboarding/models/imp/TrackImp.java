package studios.kdc.soundboarding.models.imp;

import android.support.annotation.NonNull;

import java.util.List;

import studios.kdc.soundboarding.models.Group;
import studios.kdc.soundboarding.models.Track;

/**
 * Created by Michael on 8/2/2017.
 */

public class TrackImp implements Track{
    /**
     * time of the track
     */
    private int trackDuration;
    /**
     * name of track.
     */
    private String name;
    /**
     * path of the track on phone.
     */
    private String path;

    public TrackImp(List<String> data) {
        this.name = data.get(0);
        this.trackDuration = Integer.valueOf(data.get(1));
        this.path = data.get(2);
    }

    public TrackImp(String name, Group group) {
        this.name = name;
        this.path = "";
        this.trackDuration = 0;
    }

    public TrackImp(String name, Group group, int trackDuration) {
        this.name = name;
        this.path = "";
        this.trackDuration = trackDuration;
    }

    public TrackImp(String name, String path, Group group, int trackDuration) {
        this.name = name;
        this.path = path;
        this.trackDuration = trackDuration;
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
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setTrackDuration(int duration) {
        this.trackDuration = duration;
    }

    @Override
    public int getTrackDuration() {
        return trackDuration;
    }

    @Override
    public int compareTo(Track track) {
        return this.name.compareTo(track.getName());
    }
}
