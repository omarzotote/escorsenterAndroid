package encalient.es.scorecenter.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import encalient.es.scorecenter.Common.ActivityStack;
import encalient.es.scorecenter.LeagueActivity;
import encalient.es.scorecenter.R;
import encalient.es.scorecenter.TeamActivity;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoScoreTableResultDTO;

/**
 * Created by nacho on 6/8/2015.
 */
public class ScoreCellAdapter extends ArrayAdapter<ProtoScoreTableResultDTO.ScoreTableResultDTO> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<ProtoScoreTableResultDTO.ScoreTableResultDTO> values;

    public ScoreCellAdapter(Context ctx, Activity act, ArrayList<ProtoScoreTableResultDTO.ScoreTableResultDTO> vals) {
        super(ctx, R.layout.item_cell, vals);
        context = ctx;
        values = vals;
        activity = act;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.team_score_cell, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.teamScoreName);
        TextView PJView = (TextView) rowView.findViewById(R.id.teamScorePJ);
        TextView GView = (TextView) rowView.findViewById(R.id.teamScoreG);
        TextView EView = (TextView) rowView.findViewById(R.id.teamScoreE);
        TextView PView = (TextView) rowView.findViewById(R.id.teamScoreP);
        TextView GFView = (TextView) rowView.findViewById(R.id.teamScoreGF);
        TextView GCView = (TextView) rowView.findViewById(R.id.teamScoreGC);
        TextView DifView = (TextView) rowView.findViewById(R.id.teamScoreDif);
        TextView PtsView = (TextView) rowView.findViewById(R.id.teamScorePts);

        final ProtoScoreTableResultDTO.ScoreTableResultDTO score = values.get(position);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityStack.selectedTabIndexLeaguesActivity = 2;
                Log.d("Leagues", "Clicked" + position);
                Intent intent = new Intent(activity, TeamActivity.class);
                intent.putExtra("Team", score.getTeam());
                activity.startActivity(intent);
            }
        });


        nameView.setText("" + score.getTeam().getName());
        PJView.setText("" + score.getGamesPlayed());
        GView.setText("" + score.getGamesWined());
        EView.setText("" + score.getGamesDrawn());
        PView.setText("" + score.getGamesLost());
        GFView.setText("" + score.getScoreFavor());
        GCView.setText("" + score.getScoreAgainst());
        DifView.setText("" + score.getScoreDifference());
        PtsView.setText("" + score.getPoints());

        return rowView;
    }
}
