package studios.kdc.soundboarding.models.imp;

import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.DataServiceSingleton;
import studios.kdc.soundboarding.models.Group;
import studios.kdc.soundboarding.models.GroupContainer;
import studios.kdc.soundboarding.models.Track;

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

    @Override
    public void clearAndAddGroups(List<Group> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
    }


    @Override
    public int getNumberOfGroups() {
        return groups.size();
    }

    @Override
    public List<Group> getGrps() {
        return this.groups;
    }

    @Override
    public Track popTrack(int position, String trackName) {
        Track temp = groups.get(position).getTrackByName(trackName);
        groups.get(position).removeTrackByName(trackName);
        if(groups.get(position).getTracks().isEmpty())
            groups.remove(position);
        return temp;
    }

    @Override
    public void removeTrackByName(String trackName) {
        for(int i = 0; i < this.groups.size(); i++) {
            for(int j = 0; j < this.groups.get(i).getTracks().size(); j++) {
                if (this.groups.get(i).getTracks().get(j).getName().equals(trackName)) {
                    this.groups.get(i).removeTrackByName(trackName);
                    break;
                }
            }
        }
    }
}
