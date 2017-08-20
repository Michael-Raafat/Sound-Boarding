package studios.kdc.soundboarding.models.imp;


import java.util.List;
import studios.kdc.soundboarding.models.Track;

/**
 * Created by Michael on 8/2/2017.
 */

public class TrackImp implements Track{
    private String extension;
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

    private String type;

    private int volume;


    public TrackImp(List<String> data) {
        this.name = data.get(0);
        this.trackDuration = Integer.valueOf(data.get(1));
        this.path = data.get(2);
        this.extension = data.get(3);
        this.type = data.get(4);
        this.volume = 100;
    }

    public TrackImp(String name, int trackDuration, String path, String extension, String type) {
        this.name = name;
        this.path = path;
        this.trackDuration = trackDuration;
        this.extension = extension;
        this.type = type;
    }

    public TrackImp(String name, String path, int trackDuration) {
        this.name = name;
        this.path = path;
        this.trackDuration = trackDuration;
        this.extension = "";
        this.type = "";
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
    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String getExtension() {
        return this.extension;
    }

    @Override
    public String getType() {
        return type;
    }
    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setVolume(int vol) {
        this.volume = vol;
    }

    @Override
    public int getVolume() {
        return this.volume;
    }


}
