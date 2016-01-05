package encalient.es.scorecenter.PlaceHolderFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

import encalient.es.scorecenter.Adapters.LeagueCellAdapter;
import encalient.es.scorecenter.Adapters.TeamCellAdapter;
import encalient.es.scorecenter.DataAccess.DataSources.DbHelper;
import encalient.es.scorecenter.DataAccess.DataSources.FavoriteLeagueDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.FavoriteTeamDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.TeamContentProvider;
import encalient.es.scorecenter.R;
import encalient.es.scorecenter.TeamActivity;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoTeamDTO;

/**
 * Created by EnCalientes on 10/13/2015.
 */
public class FavoritesHolderFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    private Activity activity;
    private View rootView;
    private SimpleCursorAdapter dataAdapter;
    private DbHelper dbHelper;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FavoritesHolderFragment newInstance(int sectionNumber) {
        FavoritesHolderFragment fragment = new FavoritesHolderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    //inflate fragments
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();
        activity = getActivity();
        dbHelper = new DbHelper(context);
        rootView = inflater.inflate(R.layout.list_container_fragment, container, false);
        switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
                loadLeagues();
                break;
            case 2:
                loadTeams();
                break;
        }
        return rootView;

    }
    //Read data from database
    private void loadLeagues(){
        List<ProtoLeagueDTO.LeagueDTO> leagues;
        FavoriteLeagueDataSource favoriteLeagueDataSource = new FavoriteLeagueDataSource(dbHelper.getReadableDatabase());
        leagues = favoriteLeagueDataSource.read();
        displayListViewLeagues(leagues);
    }

    private void loadTeams(){
        List<ProtoTeamDTO.TeamDTO> teamList;
        FavoriteTeamDataSource favoriteTeamDataSource = new FavoriteTeamDataSource(dbHelper.getReadableDatabase());
        teamList = favoriteTeamDataSource.read();
        displayListViewTeams(teamList);
    }
    //Display Data
    private void displayListViewLeagues(List<ProtoLeagueDTO.LeagueDTO> leagues) {
        ArrayList<ProtoLeagueDTO.LeagueDTO> list = new ArrayList<>();
        for (ProtoLeagueDTO.LeagueDTO league : leagues) {
            list.add(league);
        }

        LeagueCellAdapter mLeagueAdapter = new LeagueCellAdapter(context, activity, list);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(mLeagueAdapter);

    }



    private void displayListViewTeams(List<ProtoTeamDTO.TeamDTO> teamList) {
        ArrayList<ProtoTeamDTO.TeamDTO> list = new ArrayList<>();
        for (ProtoTeamDTO.TeamDTO team : teamList)
        {
            list.add(team);
        }

        TeamCellAdapter mTeamAdapter = new TeamCellAdapter(context, activity, list);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(mTeamAdapter);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                FavoriteTeamDataSource.COLUMN_ID,
                FavoriteTeamDataSource.COLUMN_OBJECT,
                FavoriteTeamDataSource.COLUMN_NAME
        };
        android.support.v4.content.CursorLoader cl = new android.support.v4.content.CursorLoader(context, TeamContentProvider.CONTENT_URI, projection, null, null, null);
        return cl;
    }


    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        dataAdapter.swapCursor(data);

        ListView listView = (ListView) activity.findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, TeamActivity.class);
                // intent.putExtra("Team", team);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        dataAdapter.swapCursor(null);
    }

}


