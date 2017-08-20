package studios.kdc.soundboarding;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import studios.kdc.soundboarding.models.Group;
import studios.kdc.soundboarding.models.SelectedTrack;
import studios.kdc.soundboarding.models.Track;
import studios.kdc.soundboarding.models.SelectedTracksContainer;
import studios.kdc.soundboarding.models.imp.GroupContainerImp;
import studios.kdc.soundboarding.models.imp.GroupImp;
import studios.kdc.soundboarding.models.imp.SelectedTrackContainerImp;
import studios.kdc.soundboarding.models.imp.SelectedTrackImp;
import studios.kdc.soundboarding.models.imp.TrackImp;
import studios.kdc.soundboarding.view.ViewContract;
import studios.kdc.soundboarding.view.adapters.MainAdapter;

/**
 * Created by Michael on 8/4/2017.
 */
public class DataController {

    private GroupContainerImp groupContainer;
    private SelectedTracksContainer selectedTracksContainer;
    private static DataController instance;
    private ViewContract.dataChangedNotifier notifierListener;

    public static DataController getInstance(){
        if(instance == null)
            instance = new DataController();
        return instance;
    }
    public static void deleteInstance(){
        instance = null;
    }
    public void setNotifierListener(ViewContract.dataChangedNotifier notifierListener){
        this.notifierListener = notifierListener;
    }
    private DataController() {
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
            List<List<String>> tracks = DataServiceSingleton.getInstance().getTracksInTable(group.getName());
            for(int j = 0; j < tracks.size(); j++) {
                Track track = new TrackImp(tracks.get(j));
                group.addTrack(track);
            }
            if (group.getTracks().size() == 0) {
                groupContainer.removeGroupByName(group.getName());
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
        selectedTracksContainer.addTrack(new SelectedTrackImp(trackData,
                this.groupContainer.getGrps().get(groupPosition).getName()));
        if (groupContainer.getGrps().get(groupPosition).getTracks().size() == 0) {
            groupContainer.removeGroupByName(group.getName());
        }
        Map<String , String> trackInfo =  new HashMap<>();
        trackInfo.put("name" , trackData.get(0));
        trackInfo.put("path" , trackData.get(2));
        trackInfo.put("duration" , String.valueOf(trackData.get(1)));
        trackInfo.put("extension", trackData.get(3));
        trackInfo.put("type", trackData.get(4));
        trackInfo.put("grpName" , group.getName());
        return trackInfo;
    }

    public void removeTrack(int position) {
        selectedTracksContainer.getTracks().remove(position);
    }

    public boolean deleteTrack(int trackPosition, int groupPosition) {
        Track track = groupContainer.getGrps().get(groupPosition).getTracks().get(trackPosition);
        if (track.getType().equals("assets"))
            return false;
        DataServiceSingleton.getInstance().removeTrack(track,
                groupContainer.getGrps().get(groupPosition).getName());
        this.notifierListener.notifySelectedWavesRemoved(selectedTracksContainer.getTrackPositionsByName(track.getName()));
        selectedTracksContainer.removeTrackByName(track.getName());
        groupContainer.getGrps().get(groupPosition).getTracks().remove(trackPosition);
        return true;
    }

    public boolean deleteGroup(int groupPosition) {
        int size = groupContainer.getGrps().get(groupPosition).getTracks().size();
        for (int i = 0; i < size; i++) {
            if (groupContainer.getGrps().get(groupPosition).getTracks().get(i).getType().equals("assets"))
                return false;
        }
        for (int i = 0; i < size; i++) {
            selectedTracksContainer.removeTrackByName(
                    groupContainer.getGrps().get(groupPosition).getTracks().get(i).getName());
        }
        DataServiceSingleton.getInstance().removeGroup(
                groupContainer.getGrps().get(groupPosition).getName());
        groupContainer.getGrps().get(groupPosition).getTracks().clear();
        groupContainer.getGrps().remove(groupPosition);
        return true;
    }
    public void setStartPointTrack(int position, int interval) {
        selectedTracksContainer.getTracks().get(position).setStartPoint(interval);
    }

    public List<String> getGroupNames() {
        return DataServiceSingleton.getInstance().getGroupNamesInDatabase();
    }

    public void createGroup(String name, int color) {
        Group group = new GroupImp(name, color);
        groupContainer.addGroup(group);
        DataServiceSingleton.getInstance().addGroup(group);
        this.notifierListener.notifyDataChanged();
    }

    public void createTrack(Context context ,String trackName, String path, String type, String groupName) {
        List<String> trackData = new ArrayList<>();
        trackData.add(trackName);
        trackData.add(String.valueOf(Utils.getTrackDuration(context ,path)));
        trackData.add(path);
        trackData.add(".mp3");
        trackData.add(type);
        Track newTrack = new TrackImp(trackData);
        this.groupContainer.getGroupByName(groupName).addTrack(newTrack);
        this.notifierListener.notifyDataChanged();
        DataServiceSingleton.getInstance().addTrack(newTrack, groupName);
    }

    public void saveMixedTrack(String trackName) {
        List<SelectedTrack> selectedTrackList = selectedTracksContainer.getTracks();
        List<String> savedTracksNames = DataServiceSingleton.getInstance().getSavedTracksNamesInDatabase();
        if (savedTracksNames.contains(trackName))
            DataServiceSingleton.getInstance().deleteSavedTrack(trackName);
        SavedDataServiceSingleton.getInstance().saveNewTrack(trackName, selectedTrackList);
    }

    public void loadSavedTrack(String trackName) {
        List<List<String>> tracks = SavedDataServiceSingleton.getInstance().getSelectedTracksInSavedTracksTable(trackName);
        List<SelectedTrack> selectedTrackList = new ArrayList<>();
        for(int j = 0; j < tracks.size(); j++) {
            SelectedTrack selectedTrack = new SelectedTrackImp(tracks.get(j));
            selectedTrackList.add(selectedTrack);
        }
        selectedTracksContainer.clearAndAddTracks(selectedTrackList);
    }

    public List<String> getSavedTracks() {
        return DataServiceSingleton.getInstance().getSavedTracksNamesInDatabase();
    }
    public int getNoOfSelectedTracks(){
        return this.selectedTracksContainer.getNumberOfTracks();
    }
    public Map<String, String> getTrackInfo(int position){
        SelectedTrack selectedTrack = this.selectedTracksContainer.getTrackByIndex(position);
        Map<String, String> trackInfo = new HashMap<>();
        trackInfo.put("name" , selectedTrack.getName());
        trackInfo.put("path" , selectedTrack.getPath());
        trackInfo.put("duration" , String.valueOf(selectedTrack.getTrackDuration()));
        trackInfo.put("extension", selectedTrack.getExtension());
        trackInfo.put("type", selectedTrack.getType());
        trackInfo.put("grpName" ,selectedTrack.getGroupName());
        trackInfo.put("startPoint" ,  String.valueOf(selectedTrack.getStartPoint()));
        return trackInfo;
    }

}
