package studios.kdc.soundboarding.models;

/**
 * Created by Michael on 8/3/2017.
 */

public interface GroupContainer {

    void addGroup(Group group);

    Group getGroupByName(String name);

    void removeGroupByName(String name);

}
