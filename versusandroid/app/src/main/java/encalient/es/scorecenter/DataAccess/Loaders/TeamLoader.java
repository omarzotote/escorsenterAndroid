package encalient.es.scorecenter.DataAccess.Loaders;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import encalient.es.scorecenter.Common.FlowType;
import encalient.es.scorecenter.Common.SharedSettings;
import es.encalient.ProtoTeamListDTO;

/**
 * Created by nacho on 6/8/2015.
 */


public class TeamLoader {

    public Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    public TeamLoader(Context ctx) {
        context = ctx;
        settings = context.getSharedPreferences(SharedSettings.sharedProperties, 0);
        editor = settings.edit();
    }

    public ProtoTeamListDTO.TeamListDTO getTeamsList(long id) {
        String offlinePath = "Team" + "_" + id;
        DataLoader<ProtoTeamListDTO.TeamListDTO> teamLoader = new DataLoader<ProtoTeamListDTO.TeamListDTO>(context);

        DataLoader.DataRequest req = new DataLoader.DataRequest();
        req.method = "get";
        req.paths = new ArrayList<String>();
        req.paths.add("teams");

        ProtoTeamListDTO.TeamListDTO teamList = teamLoader.getObject(req, offlinePath, ProtoTeamListDTO.TeamListDTO.PARSER, FlowType.FORCEREMOTE);
        return teamList;
    }

}
