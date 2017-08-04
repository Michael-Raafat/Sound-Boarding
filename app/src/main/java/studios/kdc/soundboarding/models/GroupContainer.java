package studios.kdc.soundboarding.models;

import java.util.List;

/**
 * Created by Michael on 8/3/2017.
 */

public interface GroupContainer {

    void addGroup(Group group);

    Group getGroupByName(String name);

    void removeGroupByName(String name);

    void clearAndAddGroups(List<Group> groups);

    int getNumberOfGroups();

    List<Group> getGrps();

    Track popTrack(int position, String trackName);

}
