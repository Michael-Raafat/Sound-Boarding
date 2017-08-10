package studios.kdc.soundboarding.models;

/**
 * Created by Michael on 8/10/2017.
 */

public interface SelectedTrack extends Track {

    void setStartPoint(int interval);

    int getStartPoint();

    void setEndPoint(int interval);

    int getEndPoint();

    void setGroupName(String groupName);

    String getGroupName();

}
