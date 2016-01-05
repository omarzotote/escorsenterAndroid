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
import encalient.es.scorecenter.Common.CircleTransform;
import encalient.es.scorecenter.LeagueActivity;
import encalient.es.scorecenter.MainActivity;
import encalient.es.scorecenter.R;
import encalient.es.scorecenter.TeamActivity;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoNotificationDTO;
import es.encalient.ProtoTeamDTO;

/**
 * Created by nacho on 6/8/2015.
 */


public class TeamCellAdapter extends ArrayAdapter<ProtoTeamDTO.TeamDTO> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<ProtoTeamDTO.TeamDTO> values;
    public TeamCellAdapter(Context ctx, Activity act, ArrayList<ProtoTeamDTO.TeamDTO> vals) {
        super(ctx, R.layout.item_cell, vals);
        context = ctx;
        values = vals;
        activity = act;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_cell, parent, false);
        TextView title = (TextView) rowView.findViewById(R.id.itemTitle);
        ImageView itemImage = (ImageView) rowView.findViewById(R.id.itemImage);
        final ProtoTeamDTO.TeamDTO team = values.get(position);


        if(team != null) {
            title.setText(team.getName());
        }

        Log.d("Team", "" + team.getName());

        //Put rounded image
        if(team.hasLogo() == true){
            Picasso.with(context)
                    .load(team.getLogo())
                    .placeholder(R.drawable.new_team)
                    .transform(new CircleTransform())
                    .into(itemImage);
        }else{
            Picasso.with(context)
                    .load(R.drawable.team)
                    .placeholder(R.drawable.new_team)
                    .transform(new CircleTransform())
                    .into(itemImage);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityStack.selectedTabIndexMainActivity = 1;
                Log.d("Teams", "Clicked" + position);
                Intent intent = new Intent(activity, TeamActivity.class);
                intent.putExtra("Team", team);
                activity.startActivityForResult(intent,2);
            }
        });

        return rowView;
    }
}

