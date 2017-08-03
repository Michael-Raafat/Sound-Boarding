package studios.kdc.soundboarding.models;

/**
 * Created by Michael on 8/2/2017.
 */

public interface Track extends Comparable<Track> {
    /**
     * set name of track.
     * @param name of the track.
     */
    void setName(String name);

    /**
     * gets name of the track.
     * @return name of the track.
     */
    String getName();

    /**
     * set group that contains the track.
     * @param group that contains this track.
     */
    void setGroup(Group group);

    /**
     * gets group of the track.
     * @return group of the track.
     */
    Group getGroup();

    /**
     * set path of the track on phone.
     * @param path string contains path of the track.
     */
    void setPath(String path);

    /**
     * gets string of the path of the track.
     * @return path of the tracks on phone.
     */
    String getPath();

    /**
     * sets time of the track.
     * @param duration of the track.
     */
    void setTrackDuration(int duration);

    /**
     * gets duration of the track.
     * @return time which is taken by the track.
     */
    int getTrackDuration();

}
