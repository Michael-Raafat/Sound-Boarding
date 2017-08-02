package studios.kdc.soundboarding.models.imp;

import android.support.annotation.NonNull;

import studios.kdc.soundboarding.models.Group;
import studios.kdc.soundboarding.models.Track;

/**
 * Created by Michael on 8/2/2017.
 */

public class TrackImp implements Track{
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
    }

    public TrackImp(String name, String path, Group group) {
        this.name = name;
        this.path = path;
        this.group = group;
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
    public int compareTo(Track track) {
        return this.name.compareTo(track.getName());
    }
}
