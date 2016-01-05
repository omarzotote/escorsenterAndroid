package encalient.es.scorecenter.PlaceHolderFragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import encalient.es.scorecenter.Adapters.LeagueCellAdapter;
import encalient.es.scorecenter.Adapters.TeamCellAdapter;
import encalient.es.scorecenter.Core.LastUpdateManager;
import encalient.es.scorecenter.Core.LeagueManager;
import encalient.es.scorecenter.Core.TeamManager;
import encalient.es.scorecenter.DataAccess.DataSources.DbHelper;
import encalient.es.scorecenter.DataAccess.DataSources.FavoriteTeamDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.LeagueDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.TeamContentProvider;
import encalient.es.scorecenter.DataAccess.DataSources.TeamDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.UpdatesDataSource;
import encalient.es.scorecenter.R;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoLeagueListDTO;
import es.encalient.ProtoTeamDTO;
import es.encalient.ProtoTeamListDTO;

/**
 * Created by EnCalientes on 10/13/2015.
 */
public class
        MainHolderFragment extends Fragment{
    private Context context;
    private Activity activity;
    private View rootView;
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
    public static MainHolderFragment newInstance(int sectionNumber) {
        MainHolderFragment fragment = new MainHolderFragment();
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
                displayTemporaryListViewLeagues();
                break;
            case 2:
                displayTemporaryListViewTeams();
                break;
        }
        return rootView;
    }

    private void displayTemporaryListViewLeagues() {
        LeagueDataSource leagueDataSource = new LeagueDataSource(dbHelper.getReadableDatabase());
        List<ProtoLeagueDTO.LeagueDTO> leagues = leagueDataSource.read();

        //Sort Favorite Leagues alphabetically
        Collections.sort(leagues, new Comparator<ProtoLeagueDTO.LeagueDTO>() {
            @Override
            public int compare(final ProtoLeagueDTO.LeagueDTO object1, final ProtoLeagueDTO.LeagueDTO object2) {
                return object1.getName().compareTo(object2.getName());
            }
        });

            if(leagues!=null)
            {
                displayLeagues(leagues);
            }
        }

    private void displayLeagues(List<ProtoLeagueDTO.LeagueDTO> leagues) {
        ArrayList<ProtoLeagueDTO.LeagueDTO> list = new ArrayList<>();
        for (ProtoLeagueDTO.LeagueDTO league : leagues) {
            list.add(league);
        }

        LeagueCellAdapter mLeagueAdapter = new LeagueCellAdapter(context, activity, list);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(mLeagueAdapter);
    }

    private void displayTemporaryListViewTeams() {
        TeamDataSource teamDataSource = new TeamDataSource(dbHelper.getReadableDatabase());
        List<ProtoTeamDTO.TeamDTO> teams = teamDataSource.read();

        //Sort Favorites Teams alphabetically
        Collections.sort(teams, new Comparator<ProtoTeamDTO.TeamDTO>() {
            @Override
            public int compare(final ProtoTeamDTO.TeamDTO object1, final ProtoTeamDTO.TeamDTO object2) {
                return object1.getName().compareTo(object2.getName());
            }
        });

        if(teams != null) {
            displayTeams(teams);
        }
    }

    private void displayTeams(List<ProtoTeamDTO.TeamDTO> teams) {
        ArrayList<ProtoTeamDTO.TeamDTO> list = new ArrayList<>();
        for (ProtoTeamDTO.TeamDTO team : teams) {
            list.add(team);
        }
        TeamCellAdapter mTeamAdapter = new TeamCellAdapter(context, activity, list);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(mTeamAdapter);
    }
}