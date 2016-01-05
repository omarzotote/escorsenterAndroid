package encalient.es.scorecenter.DataAccess.Loaders;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import encalient.es.scorecenter.Common.FlowType;
import encalient.es.scorecenter.Common.SharedSettings;
import encalient.es.scorecenter.DataAccess.DataSources.DbHelper;
import encalient.es.scorecenter.DataAccess.DataSources.LeagueDataSource;
import es.encalient.ProtoLeagueListDTO;
import es.encalient.ProtoSeasonDTO;
import es.encalient.ProtoWeekDTO;

/**
 * Created by nacho on 6/8/2015.
 */


public class LeagueLoader {

    public Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    DbHelper dbHelper = new DbHelper(context);

    public LeagueLoader(Context ctx) {
        context = ctx;
        settings = context.getSharedPreferences(SharedSettings.sharedProperties, 0);
        editor = settings.edit();
    }

    public ProtoLeagueListDTO.LeagueListDTO getLeagueList() {
        String offlinePath = "League";
        DataLoader<ProtoLeagueListDTO.LeagueListDTO> leagueLoader = new DataLoader<ProtoLeagueListDTO.LeagueListDTO>(context);

        DataLoader.DataRequest req = new DataLoader.DataRequest();
        req.method = "get";
        req.paths = new ArrayList<String>();
        req.paths.add("Leagues");

        ProtoLeagueListDTO.LeagueListDTO leagueList = leagueLoader.getObject(req, offlinePath, ProtoLeagueListDTO.LeagueListDTO.PARSER, FlowType.FORCEREMOTE);

        return leagueList;
    }


    public ProtoSeasonDTO.SeasonDTO getSeasonByLeagueId(long id) {
        String offlinePath = "League";
        DataLoader<ProtoSeasonDTO.SeasonDTO> seasonLoader = new DataLoader<ProtoSeasonDTO.SeasonDTO>(context);

        DataLoader.DataRequest req = new DataLoader.DataRequest();
        req.method = "get";
        req.paths = new ArrayList<String>();
        req.paths.add("Leagues");
        req.paths.add(Long.toString(id));
        req.paths.add("Season");
        ProtoSeasonDTO.SeasonDTO season = seasonLoader.getObject(req, offlinePath, ProtoSeasonDTO.SeasonDTO.PARSER, FlowType.FORCEREMOTE);
        return season;
    }

    public ProtoWeekDTO.WeekDTO getWeekMatchesByLeagueId(long id){
        String offlinePath = "League";
        DataLoader<ProtoWeekDTO.WeekDTO> seasonLoader = new DataLoader<ProtoWeekDTO.WeekDTO>(context);

        DataLoader.DataRequest req = new DataLoader.DataRequest();
        req.method = "get";
        req.paths = new ArrayList<String>();
        req.paths.add("Leagues");
        req.paths.add(Long.toString(id));
        req.paths.add("Week");
        req.paths.add("Matches");
        ProtoWeekDTO.WeekDTO week = seasonLoader.getObject(req, offlinePath, ProtoWeekDTO.WeekDTO.PARSER , FlowType.FORCEREMOTE);
        return week;
    }

    public ProtoSeasonDTO.SeasonDTO getMatches(long id){
        String offlinePath = "League";
        DataLoader<ProtoSeasonDTO.SeasonDTO> seasonLoader = new DataLoader<ProtoSeasonDTO.SeasonDTO>(context);

        DataLoader.DataRequest req = new DataLoader.DataRequest();
        req.method = "get";
        req.paths = new ArrayList<String>();
        req.paths.add("Leagues");
        req.paths.add(Long.toString(id));
        req.paths.add("futureMatches");
        ProtoSeasonDTO.SeasonDTO season = seasonLoader.getObject(req, offlinePath, ProtoSeasonDTO.SeasonDTO.PARSER , FlowType.FORCEREMOTE);
        return season;
    }

    public ProtoSeasonDTO.SeasonDTO getPastMatches(long id){
        String offlinePath = "League";
        DataLoader<ProtoSeasonDTO.SeasonDTO> seasonLoader = new DataLoader<ProtoSeasonDTO.SeasonDTO>(context);

        DataLoader.DataRequest req = new DataLoader.DataRequest();
        req.method = "get";
        req.paths = new ArrayList<String>();
        req.paths.add("Leagues");
        req.paths.add(Long.toString(id));
        req.paths.add("pastMatches");
        ProtoSeasonDTO.SeasonDTO season = seasonLoader.getObject(req, offlinePath, ProtoSeasonDTO.SeasonDTO.PARSER , FlowType.FORCEREMOTE);
        return season;
    }

    public ProtoSeasonDTO.SeasonDTO getScoreTableResult(long id){
        String offlinePath = "League";
        DataLoader<ProtoSeasonDTO.SeasonDTO> seasonLoader = new DataLoader<ProtoSeasonDTO.SeasonDTO>(context);

        DataLoader.DataRequest req = new DataLoader.DataRequest();
        req.method = "get";
        req.paths = new ArrayList<String>();
        req.paths.add("Leagues");
        req.paths.add(Long.toString(id));
        req.paths.add("Table");
        ProtoSeasonDTO.SeasonDTO season = seasonLoader.getObject(req, offlinePath, ProtoSeasonDTO.SeasonDTO.PARSER , FlowType.FORCEREMOTE);
        return season;
    }
}
