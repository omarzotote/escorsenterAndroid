package encalient.es.scorecenter.DataAccess.Loaders;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import encalient.es.scorecenter.Common.FlowType;
import encalient.es.scorecenter.Common.SharedSettings;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoNotificationListDTO;
import es.encalient.ProtoTeamDTO;

/**
 * Created by nacho on 6/8/2015.
 */
public class NotificationLoader {

    public Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    public NotificationLoader(Context ctx) {
        context = ctx;
        settings = context.getSharedPreferences(SharedSettings.sharedProperties, 0);
        editor = settings.edit();
    }

    public ProtoNotificationListDTO.NotificationListDTO getNotificationList(int offline, List<ProtoTeamDTO.TeamDTO> _teamList, List<ProtoLeagueDTO.LeagueDTO> _leagueList) {
        String offlinePath = "Notifications" + "_" + offline;
        DataLoader<ProtoNotificationListDTO.NotificationListDTO> notificationLoader = new DataLoader<ProtoNotificationListDTO.NotificationListDTO>(context);

        DataLoader.DataRequest req = new DataLoader.DataRequest();
        req.method = "get";
        req.paths = new ArrayList<String>();
        req.paths.add("notifications");

        req.paths.add("leagues");
        for(ProtoLeagueDTO.LeagueDTO l : _leagueList){
            req.paths.add(Long.toString(l.getId()));
        }

        req.paths.add("teams");
        for(ProtoTeamDTO.TeamDTO t : _teamList){
            req.paths.add(Long.toString(t.getId()));
        }

        ProtoNotificationListDTO.NotificationListDTO notificationList = notificationLoader.getObject(req, offlinePath, ProtoNotificationListDTO.NotificationListDTO.PARSER, FlowType.FORCEREMOTE);
        return notificationList;
    }


}
