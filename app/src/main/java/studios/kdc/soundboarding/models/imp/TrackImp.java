package studios.kdc.soundboarding.models.imp;

import android.support.annotation.NonNull;

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
    /**
     * group which contains this track.
     */
    private Group group;

    public TrackImp(String name, Group group) {
        this.name = name;
        this.group = group;
        this.path = "";
        this.trackDuration = 0;
    }

    public TrackImp(String name, Group group, int trackDuration) {
        this.name = name;
        this.group = group;
        this.path = "";
        this.trackDuration = trackDuration;
    }

    public TrackImp(String name, String path, Group group, int trackDuration) {
        this.name = name;
        this.path = path;
        this.group = group;
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
    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public Group getGroup() {
        return this.group;
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
