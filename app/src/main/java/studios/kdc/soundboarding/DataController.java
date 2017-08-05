package studios.kdc.soundboarding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import studios.kdc.soundboarding.models.Group;
import studios.kdc.soundboarding.models.GroupContainer;
import studios.kdc.soundboarding.models.Track;
import studios.kdc.soundboarding.models.TracksContainer;
import studios.kdc.soundboarding.models.imp.GroupContainerImp;
import studios.kdc.soundboarding.models.imp.GroupImp;
import studios.kdc.soundboarding.models.imp.TrackContainerImp;
import studios.kdc.soundboarding.models.imp.TrackImp;
import studios.kdc.soundboarding.view.adapters.MainAdapter;

/**
 * Created by Michael on 8/4/2017.
 */

public class DataController {
    private GroupContainer groupContainer;
    private TracksContainer tracksContainer;

    public DataController() {
        groupContainer = new GroupContainerImp();
        tracksContainer = new TrackContainerImp();

    }
    public void setMainAdapterList(MainAdapter adapter) {
        adapter.setGroups(this.groupContainer.getGrps());
    }

    public void importDatabase() {
        List<List<String>> groups = DataServiceSingleton.getInstance().getGroupsInDatabase();
        for (int i = 0; i < groups.size(); i++) {
            Group group = new GroupImp(groups.get(i));
            groupContainer.addGroup(group);
            List<List<String>> tracks = DataServiceSingleton.getInstance().getTracksInTable(group.getName());
            for(int j = 0; j < tracks.size(); j++) {
                Track track = new TrackImp(tracks.get(j), group);
                group.addTrack(track);
            }
        }
    }

    public void searchTracksInGroups(String search) {
        if (search.equals("")) {
            groupContainer.getGrps().clear();
            importDatabase();
            for (int i = 0; i < tracksContainer.getNumberOfTracks(); i++) {
                groupContainer.getGroupByName(
                        tracksContainer.getTracks().get(i).getGroup().getName()).removeTrackByName(
                                tracksContainer.getTracks().get(i).getName());
            }
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
                        Track track = new TrackImp(trackData, group);
                        tracksMatched.add(track);
                    }
                }
                group.clearAndAddTracks(tracksMatched);
                newGroups.add(group);
            }
            groupContainer.clearAndAddGroups(newGroups);
        }
    }

    public Map<String , String> selectTrackToMix(String trackName , int groupPosition) {
        List<String> trackData = DataServiceSingleton.getInstance().getTrackData(
                trackName,
                groupContainer.getGrps().get(groupPosition).getName());
        groupContainer.getGrps().get(groupPosition).removeTrackByName(trackName);
        tracksContainer.addTrack(new TrackImp(trackData,groupContainer.getGrps().get(groupPosition)));
        Map<String , String> trackInfo =  new HashMap<>();
        trackInfo.put("name" , trackData.get(0));
        trackInfo.put("path" , trackData.get(2));
        trackInfo.put("duration" , String.valueOf(trackData.get(1)));
        trackInfo.put("grpName" , groupContainer.getGrps().get(groupPosition).getName());
        return trackInfo;
    }


}
