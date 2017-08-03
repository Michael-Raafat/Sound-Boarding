package studios.kdc.soundboarding.models.imp;

import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.models.Group;
import studios.kdc.soundboarding.models.GroupContainer;

/**
 * Created by Michael on 8/3/2017.
 */

public class GroupContainerImp implements GroupContainer {

    private List<Group> groups;

    public GroupContainerImp() {
        groups = new ArrayList<Group>();
    }

    @Override
    public void addGroup(Group group) {
        groups.add(group);
    }

    @Override
    public Group getGroupByName(String name) {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getName().equals(name)) {
                return groups.get(i);
            }
        }
        return null;
    }

    @Override
    public void removeGroupByName(String name) {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getName().equals(name)) {
                groups.remove(i);
            }
        }
    }
}
