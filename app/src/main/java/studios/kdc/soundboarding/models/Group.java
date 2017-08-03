package studios.kdc.soundboarding.models;

import java.util.List;

/**
 * Created by Michael on 8/2/2017.
 */

public interface Group {
    /**
     * set name of group.
     * @param name of the group
     */
    void setName(String name);

    /**
     * gets name of the group.
     * @return name of the group.
     */
    String getName();

    /**
     * set color of the group in the form of string.
     * @param color of the group.
     */
    void setColor(int color);

    /**
     * gets color in form of string.
     * @return color in the form of string of the group
     */
    int getColor();

    /**
     * gets list of tracks in this group
     * @return list of tracks.
     */
    List<Track> getTracks();

    /**
     * adds new track to this group.
     * @param track to be added to this group.
     */
    void addTrack(Track track);
    /**
     * remove track in the group using name.
     * @param name of the track to be removed.
     */
    void removeTrackByName(String name);

    /**
     * get a track in the group using name.
     * @param name of the track needed.
     */
    Track getTrackByName(String name);
    /**
     * sort tracks in group by name.
     */
    void sortTracksByName();

    /**
     * gets number of tracks inside the group.
     * @return ineteger value of number of tracks.
     */
    int getNumberOfTracks();
}
