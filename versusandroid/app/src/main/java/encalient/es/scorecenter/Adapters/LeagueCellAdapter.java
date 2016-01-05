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
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoNotificationDTO;

/**
 * Created by nacho on 6/8/2015.
 */
public class LeagueCellAdapter extends ArrayAdapter<ProtoLeagueDTO.LeagueDTO> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<ProtoLeagueDTO.LeagueDTO> values;
    private final int REQUEST_CODE_FIRST_ACTIVITY = 1;

    public LeagueCellAdapter(Context ctx, Activity act, ArrayList<ProtoLeagueDTO.LeagueDTO> vals) {
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
        final ProtoLeagueDTO.LeagueDTO league = values.get(position);

        if(league != null) {
            title.setText(league.getName());
        }

        Log.d("League", "" + league.getName());

        //Verificar que exista un logotipo
        if(league.hasLogo() == true){
            Picasso.with(context)
                    .load(league.getLogo())
                    .placeholder(R.drawable.new_team)
                    .transform(new CircleTransform())
                    .into(itemImage);
        }else{
            Picasso.with(context)
                    .load(R.drawable.league)
                    .placeholder(R.drawable.new_team)
                    .transform(new CircleTransform())
                    .into(itemImage);

        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityStack.selectedTabIndexMainActivity = 0;
                ActivityStack.selectedTabIndexLeaguesActivity = 0;
                Log.d("Leagues", "Clicked" + position);
                Intent intent = new Intent(activity, LeagueActivity.class);
                intent.putExtra("League", league);
                activity.startActivity(intent);
            }
        });

        return rowView;
    }
}
