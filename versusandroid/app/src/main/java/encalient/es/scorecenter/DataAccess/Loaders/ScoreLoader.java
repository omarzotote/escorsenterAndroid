package encalient.es.scorecenter.DataAccess.Loaders;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import encalient.es.scorecenter.Common.FlowType;
import encalient.es.scorecenter.Common.SharedSettings;
import es.encalient.ProtoScoreTableResultDTO;
import es.encalient.ProtoSeasonDTO;

/**
 * Created by EnCalientes PC 3 on 9/4/2015.
 */
public class ScoreLoader {

    public Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    public ScoreLoader(Context ctx) {
        context = ctx;
        settings = context.getSharedPreferences(SharedSettings.sharedProperties, 0);
        editor = settings.edit();
    }

    public ProtoScoreTableResultDTO.ScoreTableResultDTO getScoreTableResult(long teamId) {
        String offlinePath = "Score" + "_" + teamId;
        DataLoader<ProtoScoreTableResultDTO.ScoreTableResultDTO> scoreTableResultDTODataLoader = new DataLoader<>(context);

        DataLoader.DataRequest req = new DataLoader.DataRequest();
        req.method = "get";
        req.paths = new ArrayList<>();
        req.paths.add("teams");
        req.paths.add(Long.toString(teamId));
        req.paths.add("score");

        ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResultDTOList = scoreTableResultDTODataLoader.getObject(req, offlinePath, ProtoScoreTableResultDTO.ScoreTableResultDTO.PARSER, FlowType.FORCEREMOTE);
        return scoreTableResultDTOList;
    }


}
