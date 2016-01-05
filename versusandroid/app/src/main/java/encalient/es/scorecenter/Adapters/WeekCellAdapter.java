package encalient.es.scorecenter.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import encalient.es.scorecenter.Common.ActivityStack;
import encalient.es.scorecenter.Common.CircleTransform;
import encalient.es.scorecenter.Common.Map.MapMatchHandler;
import encalient.es.scorecenter.R;
import encalient.es.scorecenter.TeamActivity;
import es.encalient.ProtoMatchDTO;
import es.encalient.ProtoTeamDTO;
import es.encalient.ProtoWeekDTO;

/**
 * Created by nacho on 6/8/2015.
 */
public class WeekCellAdapter extends ArrayAdapter<ProtoWeekDTO.WeekDTO> {

    MapMatchHandler handler;
    Context context;
    ArrayList<ProtoWeekDTO.WeekDTO> values;
    int nTab;

    public WeekCellAdapter(Context ctx, ArrayList<ProtoWeekDTO.WeekDTO> vals, MapMatchHandler mapMatchHandler, int _nTab) {
        super(ctx, R.layout.week_cell, vals);
        context = ctx;
        values = vals;
        handler = mapMatchHandler;
        nTab = _nTab;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.week_cell, parent, false);

        TextView title = (TextView) rowView.findViewById(R.id.weekTitle);

        ProtoWeekDTO.WeekDTO week = values.get(position);
        title.setText("Jornada: " + week.getTitle());
        for (ProtoMatchDTO.MatchDTO match : week.getMatchesList()) {
            addMatch((LinearLayout) rowView, match, week);
        }
        return rowView;
    }

    private void addMatch(ViewGroup parent, final ProtoMatchDTO.MatchDTO match, final ProtoWeekDTO.WeekDTO weekDTO) {

        final ProtoMatchDTO.MatchDTO m = match;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.game_cell, parent, false);
        final ProtoTeamDTO.TeamDTO team1 = match.getTeam1();
        final ProtoTeamDTO.TeamDTO team2 = match.getTeam2();

        LinearLayout team1LL = (LinearLayout) rowView.findViewById(R.id.Team1Layout);
        TextView team1View = (TextView) rowView.findViewById(R.id.team1);
        ImageView team1img = (ImageView) rowView.findViewById(R.id.team1img);
        LinearLayout team2LL = (LinearLayout) rowView.findViewById(R.id.Team2Layout);
        TextView team2View = (TextView) rowView.findViewById(R.id.team2);
        ImageView team2img = (ImageView) rowView.findViewById(R.id.team2img);
        TextView vsTextView = (TextView) rowView.findViewById(R.id.vs_text);
        TextView dateView = (TextView) rowView.findViewById(R.id.date);
        TextView timeView = (TextView) rowView.findViewById(R.id.date_time);
        TextView mapTextView = (TextView) rowView.findViewById(R.id.mapText);
        LinearLayout infoLayout = (LinearLayout) rowView.findViewById(R.id.infoLayout);

        team1View.setText(match.getTeam1().getName());
        if (match.getTeam1().hasLogo() && match.getTeam1().getLogo().trim().length() > 0) {

            Picasso.with(context)
                    .load(match.getTeam1().getLogo())
                    .placeholder(R.drawable.teamtransparent)
                    .transform(new CircleTransform())
                    .into(team1img);
        }
        team1LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(match.getFinished())
                    ActivityStack.selectedTabIndexLeaguesActivity = 1;
                else
                    ActivityStack.selectedTabIndexLeaguesActivity = 0;
                Intent intent = new Intent(context, TeamActivity.class);
                intent.putExtra("Team", team1);
                context.startActivity(intent);
            }
        });
        team2View.setText(match.getTeam2().getName());
        if (match.getTeam2().hasLogo() && match.getTeam2().getLogo().trim().length() > 0) {
            Picasso.with(context)
                    .load(match.getTeam2().getLogo())
                    .placeholder(R.drawable.teamtransparent)
                    .transform(new CircleTransform())
                    .into(team2img);
        }
        team2LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(match.getFinished())
                    ActivityStack.selectedTabIndexLeaguesActivity = 1;
                else
                    ActivityStack.selectedTabIndexLeaguesActivity = 0;
                Intent intent = new Intent(context, TeamActivity.class);
                intent.putExtra("Team", team2);
                context.startActivity(intent);
            }
        });
        if(match.getFinished()) {
            vsTextView.setText(match.getScoreTeam1() + " - " + match.getScoreTeam2());
        }
        String[] dateSplitted = match.getDate().split(" ", 2);
        dateView.setText(dateSplitted[0]);
        timeView.setText(dateSplitted[1]);
        if(nTab == 1) {
            infoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.setMap(match, weekDTO);
                }
            });
        } else {
            mapTextView.setVisibility(View.GONE);
        }
        parent.addView(rowView);
    }
}
