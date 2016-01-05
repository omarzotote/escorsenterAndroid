package encalient.es.scorecenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;
import java.util.Locale;

import encalient.es.scorecenter.Common.ActivityStack;
import encalient.es.scorecenter.Common.Map.MapHandler;
import encalient.es.scorecenter.Common.Map.MapMatchHandler;
import encalient.es.scorecenter.Common.Map.MatchMapFragment;
import encalient.es.scorecenter.Core.LeagueManager;
import encalient.es.scorecenter.DataAccess.DataSources.DbHelper;
import encalient.es.scorecenter.DataAccess.DataSources.FavoriteLeagueDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.FutureMatchesDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.LeagueScoreTableResultDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.PastMatchesDataSource;
import encalient.es.scorecenter.PlaceHolderFragment.LeagueHolderFragment;
import encalient.es.scorecenter.tabs.SlidingTabLayout;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoMatchDTO;
import es.encalient.ProtoSeasonDTO;
import es.encalient.ProtoWeekDTO;


public class LeagueActivity extends AppCompatActivity implements MapMatchHandler, SwipeRefreshLayout.OnRefreshListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    Activity activity = this;
    Context context = activity;
    LeaguePagerAdapter mLeaguePagerAdapter;
    LeagueManager leagueManager;
    ProtoLeagueDTO.LeagueDTO league;
    DbHelper dbHelper = new DbHelper(activity);
    FavoriteLeagueDataSource favoriteLeagueDataSource;
    Toolbar toolbar;
    SlidingTabLayout tabs;
    ViewPager pager;
    Menu menu;
    MenuItem favoritesMenuItem;
    SupportMapFragment mMapFragment;
    SwipeRefreshLayout swipeRefreshLayout;
    Integer refreshed;

    boolean isFavorite;
    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */

    private void addFavorites() {
        favoriteLeagueDataSource = new FavoriteLeagueDataSource(dbHelper.getWritableDatabase());
        favoriteLeagueDataSource.insert(league);
        Toast.makeText(context, "Liga añadida a favoritos." , Toast.LENGTH_SHORT).show();

        isFavorite = true;
    }

    private void removeFavorites() {
        favoriteLeagueDataSource = new FavoriteLeagueDataSource(dbHelper.getWritableDatabase());
        favoriteLeagueDataSource.delete(league);
        isFavorite = false;
    }
    //Checar si está en favoritos
    private void isInFavoritesDb(){
        isFavorite = false;
        favoriteLeagueDataSource = new FavoriteLeagueDataSource(dbHelper.getReadableDatabase());
        List<ProtoLeagueDTO.LeagueDTO> leagueList = favoriteLeagueDataSource.read();
        for(ProtoLeagueDTO.LeagueDTO l : leagueList) {
            if(l.getId() == league.getId())
                isFavorite = true;
        }
    }

    public void toggleFavorites() {

        if(!isFavorite) {
            addFavorites();
            favoritesMenuItem.setTitle("Quitar de Favoritos");
            favoritesMenuItem.setIcon(R.drawable.icon3);
        }
        else {
            removeFavorites();
            favoritesMenuItem.setTitle("Añadir a Favoritos");
            favoritesMenuItem.setIcon(R.drawable.icon3white);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);

        Intent intent = getIntent();
        league = (ProtoLeagueDTO.LeagueDTO) intent.getSerializableExtra("League");

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(league.getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //swipe down to refresh
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.primaryColor));

        mLeaguePagerAdapter = new LeaguePagerAdapter(context, activity, league, getSupportFragmentManager());
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(mLeaguePagerAdapter);
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);
        tabs.setSelectedIndicatorColors(getResources().getColor(R.color.accentColor));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                swipeRefreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        pager.setCurrentItem(ActivityStack.selectedTabIndexLeaguesActivity);

        if(ActivityStack.stack.size() == 0)
            return;

        if(getIntent().equals(ActivityStack.stack.peek()))
            ActivityStack.stack.pop();
    }

    @Override
    public void onPause() {
        super.onPause();

        ActivityStack.stack.push(getIntent());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        ActivityStack.stack.pop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.league_activity_actions, _menu);
        menu = _menu;
        favoritesMenuItem = menu.findItem(R.id.addFavorites);
        isInFavoritesDb();
        if(isFavorite) {
            favoritesMenuItem.setTitle("Quitar de Favoritos");
            favoritesMenuItem.setIcon(R.drawable.icon3);
        }
        else {
            favoritesMenuItem.setTitle("Añadir a Favoritos");
            favoritesMenuItem.setIcon(R.drawable.icon3white);
        }


        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addFavorites:
                toggleFavorites();
                return true;
            case R.id.home:
                if(ActivityStack.stack.size() > 0) {
                    Intent intent = ActivityStack.stack.peek();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setMap(ProtoMatchDTO.MatchDTO match, ProtoWeekDTO.WeekDTO week) {
        final View weekListResults = activity.findViewById(R.id.swipe_container);
        weekListResults.setVisibility(View.GONE);

        final View mapContainer = activity.findViewById(R.id.mapContainer);
        mapContainer.setVisibility(View.VISIBLE);

        TextView weekTextView = (TextView) mapContainer.findViewById(R.id.JornadaTextView);
        TextView team1TextView = (TextView) mapContainer.findViewById(R.id.Team1TextView);
        TextView team2TextView = (TextView) mapContainer.findViewById(R.id.Team2TextView);
        TextView timeTextView = (TextView) mapContainer.findViewById(R.id.TimeTextView);
        TextView locationTextView = (TextView) mapContainer.findViewById(R.id.LocationTextView);
        TextView fieldTextView = (TextView) mapContainer.findViewById(R.id.FieldTextView);
        View mapDescription = mapContainer.findViewById(R.id.mapDecription);

        weekTextView.setText("Jornada: " + week.getTitle());
        team1TextView.setText(match.getTeam1().getName());
        team2TextView.setText(match.getTeam2().getName());
        timeTextView.setText(match.getDate());
        locationTextView.setText(match.getField().getAddress());
        fieldTextView.setText(match.getField().getName());
        mapDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(mMapFragment);
                fragmentTransaction.commit();

                weekListResults.setVisibility(View.VISIBLE);
                mapContainer.setVisibility(View.GONE);
            }
        });

        mMapFragment = MatchMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapContainer, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(new MapHandler(activity, match, week));
    }
    //Partidos futuros
    private void loadRoleData() {
        leagueManager = new LeagueManager(context);
        leagueManager.SetActionListener(new LeagueManager.OnActionListener() {
            @Override
            public void OnAction(ProtoSeasonDTO.SeasonDTO season) {
                if(season != null) {
                    updateFutureMatchesListView(season);
                }

                refreshed++;
                if(refreshed == 3) {
                    swipeRefreshLayout.setRefreshing(false);
                    pager.setCurrentItem(pager.getCurrentItem());
                }
            }
        });

        leagueManager.getFutureMatches(league.getId());
    }

    private void updateFutureMatchesListView(ProtoSeasonDTO.SeasonDTO season) {
        FutureMatchesDataSource futureMatchesDataSource = new FutureMatchesDataSource(dbHelper.getWritableDatabase());
        List<ProtoSeasonDTO.SeasonDTO> seasons = futureMatchesDataSource.read();
        for (ProtoSeasonDTO.SeasonDTO s : seasons) {
            if (s.getLeague() == league.getId()) {
                futureMatchesDataSource.delete(s);
            }
        }
        futureMatchesDataSource.insert(season);
    }
    //partidos pasados con resultados
    private void loadResultData() {
        leagueManager = new LeagueManager(context);
        leagueManager.SetActionListener(new LeagueManager.OnActionListener() {
            @Override
            public void OnAction(ProtoSeasonDTO.SeasonDTO season) {
                if (season != null) {
                    updatePastMatchesListView(season);
                }

                refreshed++;
                if(refreshed == 3) {
                    swipeRefreshLayout.setRefreshing(false);
                    pager.setCurrentItem(pager.getCurrentItem());
                }
            }
        });

        leagueManager.getPastMatches(league.getId());
    }

    private void updatePastMatchesListView(ProtoSeasonDTO.SeasonDTO season) {
        PastMatchesDataSource pastMatchesDataSource = new PastMatchesDataSource(dbHelper.getWritableDatabase());
        List<ProtoSeasonDTO.SeasonDTO> seasons = pastMatchesDataSource.read();

        for (ProtoSeasonDTO.SeasonDTO s : seasons) {
            if (s.getLeague() == league.getId()) {
                pastMatchesDataSource.delete(s);
            }
        }
        pastMatchesDataSource.insert(season);
    }

    private void loadScoreData() {
        leagueManager = new LeagueManager(context);
        leagueManager.SetActionListener(new LeagueManager.OnActionListener() {
            @Override
            public void OnAction(ProtoSeasonDTO.SeasonDTO season) {
                if (season != null) {
                    updateScore(season);
                }

                refreshed++;
                if(refreshed == 3) {
                    swipeRefreshLayout.setRefreshing(false);
                    pager.setCurrentItem(pager.getCurrentItem());
                }
            }
        });
        leagueManager.getScoreTableResult(league.getId());
    }

    private void updateScore(ProtoSeasonDTO.SeasonDTO season) {
        LeagueScoreTableResultDataSource leagueScoreTableResultDataSource = new LeagueScoreTableResultDataSource(dbHelper.getWritableDatabase());
        List<ProtoSeasonDTO.SeasonDTO> seasons = leagueScoreTableResultDataSource.read();

        for (ProtoSeasonDTO.SeasonDTO s : seasons) {
            if (s.getLeague() == league.getId()) {
                leagueScoreTableResultDataSource.delete(s);
            }
        }
        leagueScoreTableResultDataSource.insert(season);
    }

    @Override
    public void onRefresh() {
        refreshed = 0;

        loadRoleData();
        loadResultData();
        loadScoreData();
    }

    public class LeaguePagerAdapter extends FragmentPagerAdapter {
        private final Activity activity;
        private final Context context;
        private final ProtoLeagueDTO.LeagueDTO league;

        public LeaguePagerAdapter(Context ctx, Activity act, ProtoLeagueDTO.LeagueDTO leag, FragmentManager fm) {
            super(fm);
            activity = act;
            context = ctx;
            league = leag;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return LeagueHolderFragment.newInstance(context, activity, league, position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_league_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_league_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_league_section3).toUpperCase(l);
            }
            return null;
        }
    }

}
