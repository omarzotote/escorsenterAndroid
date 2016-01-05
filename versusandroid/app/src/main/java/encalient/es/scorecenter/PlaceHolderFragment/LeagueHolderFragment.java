package encalient.es.scorecenter.PlaceHolderFragment;

/**
 * Created by EnCalientes on 10/13/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import encalient.es.scorecenter.Adapters.ScoreCellAdapter;
import encalient.es.scorecenter.Adapters.WeekCellAdapter;
import encalient.es.scorecenter.Common.Map.MapMatchHandler;
import encalient.es.scorecenter.Core.LeagueManager;
import encalient.es.scorecenter.DataAccess.DataSources.DbHelper;
import encalient.es.scorecenter.DataAccess.DataSources.FutureMatchesDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.LeagueScoreTableResultDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.PastMatchesDataSource;
import encalient.es.scorecenter.R;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoScoreTableResultDTO;
import es.encalient.ProtoSeasonDTO;
import es.encalient.ProtoWeekDTO;

/**
 * A placeholder fragment containing a simple view.
 */
public class LeagueHolderFragment extends Fragment {

    private WeekCellAdapter mWeekAdapter = null;
    private LeagueManager leagueManager = null;
    private View rootView;
    private Context context;
    private Activity activity;
    private ProtoLeagueDTO.LeagueDTO league;
    DbHelper dbHelper;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_LEAGUE = "league";
    private static final String ARG_SECTION_NUMBER = "section number";


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LeagueHolderFragment newInstance(Context ctx, Activity activity, ProtoLeagueDTO.LeagueDTO league, int sectionNumber) {
        LeagueHolderFragment fragment = new LeagueHolderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_LEAGUE, league);
        fragment.setArguments(args);
        return fragment;
    }

    //inflate fragments
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        league = (ProtoLeagueDTO.LeagueDTO) arguments.getSerializable(ARG_LEAGUE);
        context = getActivity();
        activity = getActivity();


        dbHelper = new DbHelper(context);
        switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
                rootView = inflater.inflate(R.layout.league_role, container, false);
                displayTemporaryFutureMatchesListView();
                break;
            case 2:
                rootView = inflater.inflate(R.layout.list_container_fragment, container, false);
                displayTemporaryPastMatchesListView();
                break;
            case 3:
                rootView = inflater.inflate(R.layout.league_table, container, false);
                displayTemporaryScore();
                break;

            default:
                rootView = inflater.inflate(R.layout.league_table, container, false);
        }

        return rootView;
    }

    // Display Future Matches
    private void displayTemporaryFutureMatchesListView() {
        ProtoSeasonDTO.SeasonDTO season = null;
        FutureMatchesDataSource futureMatchesDataSource = new FutureMatchesDataSource(dbHelper.getReadableDatabase());
        List<ProtoSeasonDTO.SeasonDTO> seasons = futureMatchesDataSource.read();

        for (ProtoSeasonDTO.SeasonDTO s : seasons) {
            if (s.getLeague() == league.getId()) {
                season = s;
            }
        }
        if (season != null) {
            displayFutureMatches(season);
        }
    }

    private void displayFutureMatches(ProtoSeasonDTO.SeasonDTO season) {
        List<ProtoWeekDTO.WeekDTO> weeks = season.getWeeksList();
        ArrayList<ProtoWeekDTO.WeekDTO> list = new ArrayList<>();
        for (ProtoWeekDTO.WeekDTO week : weeks) {
            list.add(week);
        }
        mWeekAdapter = new WeekCellAdapter(context, list, (MapMatchHandler) activity, 1);
        //TextView titleView = (TextView) activity.findViewById(R.id.leagueTitle);
        //titleView.setText(league.getName());
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(mWeekAdapter);
    }

    // Display Past Matches

    private void displayTemporaryPastMatchesListView() {
        ProtoSeasonDTO.SeasonDTO season = null;
        PastMatchesDataSource pastMatchesDataSource = new PastMatchesDataSource(dbHelper.getReadableDatabase());
        List<ProtoSeasonDTO.SeasonDTO> seasons = pastMatchesDataSource.read();
        for (ProtoSeasonDTO.SeasonDTO s : seasons) {
            if (s.getLeague() == league.getId()) {
                season = s;
            }
        }
        if (season != null) {
            displayPastMatches(season);
        }
    }

    private void displayPastMatches(ProtoSeasonDTO.SeasonDTO season) {
        List<ProtoWeekDTO.WeekDTO> weeks = season.getWeeksList();
        ArrayList<ProtoWeekDTO.WeekDTO> list = new ArrayList<>();
        for (ProtoWeekDTO.WeekDTO week : weeks) {
            if(week.getMatchesCount() > 0){
                list.add(week);
            }
        }
        mWeekAdapter = new WeekCellAdapter(context, list, (MapMatchHandler) activity, 2);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(mWeekAdapter);
    }

    // Display Score Table Result

    private void displayTemporaryScore() {

        ProtoSeasonDTO.SeasonDTO season = null;

        LeagueScoreTableResultDataSource leagueScoreTableResultDataSource = new LeagueScoreTableResultDataSource(dbHelper.getReadableDatabase());
        List<ProtoSeasonDTO.SeasonDTO> seasons = leagueScoreTableResultDataSource.read();
        for (ProtoSeasonDTO.SeasonDTO s : seasons) {
            if (s.getLeague() == league.getId()) {
                season = s;
            }
        }
        if (season != null) {
            displayScore(season);
        }
    }

    private void displayScore(ProtoSeasonDTO.SeasonDTO season) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<ProtoScoreTableResultDTO.ScoreTableResultDTO> list = new ArrayList<>();
        List<ProtoScoreTableResultDTO.ScoreTableResultDTO> scores = season.getScoreTableResultList();
        ListView linearLayout = (ListView) rootView.findViewById(R.id.teamContainer);
        for (ProtoScoreTableResultDTO.ScoreTableResultDTO score : scores) {
            list.add(score);
        }

        ScoreCellAdapter scoreCellAdapter = new ScoreCellAdapter(context, activity, list);
        ListView listView = (ListView) rootView.findViewById(R.id.teamContainer);
        listView.setAdapter(scoreCellAdapter);
    }
}