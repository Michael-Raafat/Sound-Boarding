package studios.kdc.soundboarding;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import studios.kdc.soundboarding.models.Group;
import studios.kdc.soundboarding.models.Track;
import studios.kdc.soundboarding.models.SelectedTracksContainer;
import studios.kdc.soundboarding.models.imp.GroupContainerImp;
import studios.kdc.soundboarding.models.imp.GroupImp;
import studios.kdc.soundboarding.models.imp.SelectedTrackContainerImp;
import studios.kdc.soundboarding.models.imp.TrackImp;
import studios.kdc.soundboarding.view.adapters.MainAdapter;

/**
 * Created by Michael on 8/4/2017.
 */

public class DataController {
    private GroupContainerImp groupContainer;
    private SelectedTracksContainer selectedTracksContainer;

    public DataController() {
        groupContainer = GroupContainerImp.getInstance();
        selectedTracksContainer = SelectedTrackContainerImp.getInstance();

    }
    public void deleteReferences() {
        GroupContainerImp.deleteInstance();
        SelectedTrackContainerImp.deleteInstance();
    }


    public void setMainAdapterList(MainAdapter adapter) {
        adapter.setGroups(this.groupContainer.getGrps());
    }

    public void importDatabase() {
        List<List<String>> groups = DataServiceSingleton.getInstance().getGroupsInDatabase();
        for (int i = 0; i < groups.size(); i++) {
            Group group = new GroupImp(groups.get(i));
            groupContainer.addGroup(group);
            Log.i("hna" , groupContainer.getNumberOfGroups()+"");
            List<List<String>> tracks = DataServiceSingleton.getInstance().getTracksInTable(group.getName());
            for(int j = 0; j < tracks.size(); j++) {
                Track track = new TrackImp(tracks.get(j));
                group.addTrack(track);
            }
        }
    }

    public void searchTracksInGroups(String search) {
        if (search.equals("")) {
            groupContainer.getGrps().clear();
            importDatabase();
        } else {
            List<List<String>> data = DataServiceSingleton.getInstance().getDataMatches(search);
            List<Group> newGroups = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                List<Track> tracksMatched = new ArrayList<>();
                Group group = null;
                for (int j = 0; j < data.get(i).size(); j++) {
                    if (j == 0) {
                        List<String> groupData = DataServiceSingleton.getInstance().getGroupData(data.get(i).get(0));
                        group = new GroupImp(groupData);

                    } else {
                        List<String> trackData = DataServiceSingleton.getInstance().getTrackData(
                                data.get(i).get(j),
                                data.get(i).get(0));
                        Track track = new TrackImp(trackData);
                        tracksMatched.add(track);
                    }
                }
                group.clearAndAddTracks(tracksMatched);
                newGroups.add(group);
            }
            groupContainer.clearAndAddGroups(newGroups);
        }
        for (int i = 0; i < selectedTracksContainer.getNumberOfTracks(); i++) {
            groupContainer.removeTrackByName(
                    selectedTracksContainer.getTracks().get(i).getName());
        }
        for(int i = 0; i < groupContainer.getNumberOfGroups(); i++) {
            if (groupContainer.getGrps().get(i).getTracks().size() == 0) {
                groupContainer.removeGroupByName(groupContainer.getGrps().get(i).getName());
            }
        }
    }

    public Map<String , String> selectTrackToMix(String trackName , int groupPosition) {
        Group group = groupContainer.getGrps().get(groupPosition);
        List<String> trackData = DataServiceSingleton.getInstance().getTrackData(
                trackName,
                groupContainer.getGrps().get(groupPosition).getName());
       // groupContainer.getGrps().get(groupPosition).removeTrackByName(trackName);
        selectedTracksContainer.addTrack(new TrackImp(trackData));
        if (groupContainer.getGrps().get(groupPosition).getTracks().size() == 0) {
            groupContainer.removeGroupByName(group.getName());
        }
        Map<String , String> trackInfo =  new HashMap<>();
        trackInfo.put("name" , trackData.get(0));
        trackInfo.put("path" , trackData.get(2));
        trackInfo.put("duration" , String.valueOf(trackData.get(1)));
        trackInfo.put("grpName" , group.getName());

        return trackInfo;
    }

    public void removeTrack(int position) {
        selectedTracksContainer.getTracks().remove(position);
    }

}
