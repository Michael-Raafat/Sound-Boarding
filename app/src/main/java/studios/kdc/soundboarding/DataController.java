package studios.kdc.soundboarding;

import android.app.Activity;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.models.Group;
import studios.kdc.soundboarding.models.GroupContainer;
import studios.kdc.soundboarding.models.Track;
import studios.kdc.soundboarding.models.TracksContainer;
import studios.kdc.soundboarding.models.imp.GroupContainerImp;
import studios.kdc.soundboarding.models.imp.GroupImp;
import studios.kdc.soundboarding.models.imp.TrackImp;

/**
 * Created by Michael on 8/4/2017.
 */

public class DataController {
    private GroupContainer groupContainer;
    private TracksContainer tracksContainer;

    public DataController() {
        groupContainer = new GroupContainerImp();

    }

    public void importDatabase() {
        List<Pair<String, Integer>> groups = DataServiceSingleton.getInstance().getGroupsInDatabase();
        for (int i = 0; i < groups.size(); i++) {
            String groupName = groups.get(i).first;
            int groupColor = groups.get(i).second;
            Group group = new GroupImp(groupColor, groupName);
            groupContainer.addGroup(group);
            List<String[]> tracks = DataServiceSingleton.getInstance().getTracksInTable(groupName);
            for(int j = 0; j < tracks.size(); j++) {
                String trackName = tracks.get(j)[0];
                int trackDuration = Integer.valueOf(tracks.get(j)[1]);
                String trackPath = tracks.get(j)[2];
                Track track = new TrackImp(trackName, trackPath, group, trackDuration);
                group.addTrack(track);
            }
        }
    }

    public void searchTracksInGroups(String search) {
        List<List<String>> data = DataServiceSingleton.getInstance().getDataMatches(search);
        List<Group> newGroups = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            List<Track> tracksMatched = new ArrayList<>();
            Group group = null;
            for (int j = 0; j < data.get(i).size(); j++) {
                if ( j == 0) {
                    group = groupContainer.getGroupByName(data.get(i).get(j));
                } else {
                    tracksMatched.add(group.getTrackByName(data.get(i).get(j)));
                }
            }
            group.clearAndAddTracks(tracksMatched);
            newGroups.add(group);
        }
        groupContainer.clearAndAddGroups(newGroups);
    }




}
