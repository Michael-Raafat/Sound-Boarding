package studios.kdc.soundboarding.models.imp;

import android.support.annotation.NonNull;

import java.util.List;

import studios.kdc.soundboarding.models.SelectedTrack;


/**
 * Created by Michael on 8/10/2017.
 */

public class SelectedTrackImp implements SelectedTrack {

    private String name;
    private int trackDuration;
    private String path;
    private String extension;
    private int startPoint;
    private int endPoint;



    public SelectedTrackImp(List<String> data) {
        this.name = data.get(0);
        this.trackDuration = Integer.valueOf(data.get(1));
        this.path = data.get(2);
        this.extension = data.get(3);
        this.startPoint = -1;
        this.endPoint = -1;
    }

    public SelectedTrackImp(String name, int trackDuration, String path, String extension) {
        this.name = name;
        this.trackDuration = trackDuration;
        this.path = path;
        this.extension = extension;
        this.startPoint = -1;
        this.endPoint = -1;
    }


    @Override
    public void setStartPoint(int interval) {
        this.startPoint = interval;
    }

    @Override
    public int getStratPoint() {
        return startPoint;
    }

    @Override
    public void setEndPoint(int interval) {
        this.endPoint = interval;
    }

    @Override
    public int getEndPoint() {
        return endPoint;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
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
        return extension;
    }


}