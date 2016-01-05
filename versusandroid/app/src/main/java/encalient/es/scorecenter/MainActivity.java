package encalient.es.scorecenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;

import encalient.es.scorecenter.Adapters.LeagueCellAdapter;
import encalient.es.scorecenter.Adapters.TeamCellAdapter;
import encalient.es.scorecenter.Common.ActivityStack;
import encalient.es.scorecenter.Core.LeagueManager;
import encalient.es.scorecenter.Core.TeamManager;
import encalient.es.scorecenter.DataAccess.DataSources.DbHelper;
import encalient.es.scorecenter.DataAccess.DataSources.LeagueDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.TeamContentProvider;
import encalient.es.scorecenter.DataAccess.DataSources.FavoriteTeamDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.TeamDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.UpdatesDataSource;
import encalient.es.scorecenter.PlaceHolderFragment.MainHolderFragment;
import encalient.es.scorecenter.tabs.SlidingTabLayout;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoLeagueListDTO;
import es.encalient.ProtoTeamDTO;
import es.encalient.ProtoTeamListDTO;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    Activity activity = this;
    Context context = activity;
    DbHelper dbHelper = new DbHelper(context);
    MainPagerAdapter mMainPagerAdapter;
    Toolbar toolbar;
    ViewPager pager;
    SlidingTabLayout tabs;
    SwipeRefreshLayout swipeRefreshLayout;
    Integer refreshed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //swipe down to refresh
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.primaryColor));

        onRefresh();
        mMainPagerAdapter = new MainPagerAdapter(context, activity, getSupportFragmentManager());
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(mMainPagerAdapter);
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

        pager.setCurrentItem(ActivityStack.selectedTabIndexMainActivity);
        //Esto es para regresar a la última activity sin problemas
        if (ActivityStack.stack.size() == 0)
            return;

        if (getIntent().equals(ActivityStack.stack.peek()))
            ActivityStack.stack.pop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Esto es para regresar a la última activity sin problemas
        ActivityStack.stack.push(getIntent());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Esto es para regresar a la última activity sin problemas
        ActivityStack.stack.pop();
    }

    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Esto es para regresar a la última activity sin problemas
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                if (ActivityStack.stack.size() > 0) {
                    Intent intent = ActivityStack.stack.peek();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadLeaguesData() {
        LeagueManager leagueManager = new LeagueManager(context);
        leagueManager.SetActionListener(new LeagueManager.OnActionListener() {
            @Override
            public void OnAction(ProtoLeagueListDTO.LeagueListDTO leagueList) {
                if (leagueList != null) {
                    updateListViewLeagues(leagueList);
                }

                refreshed++;
                if(refreshed == 2) {
                    swipeRefreshLayout.setRefreshing(false);
                    pager.setCurrentItem(pager.getCurrentItem());
                }
            }
        });

        leagueManager.getLeagues();
    }

    private void updateListViewLeagues(ProtoLeagueListDTO.LeagueListDTO leagueList) {
        LeagueDataSource leagueDataSource = new LeagueDataSource(dbHelper.getWritableDatabase());
        List<ProtoLeagueDTO.LeagueDTO> leagues = leagueDataSource.read();
        for (ProtoLeagueDTO.LeagueDTO leagueDTO : leagues) {
            leagueDataSource.delete(leagueDTO);
        }
        leagues = leagueList.getItemsList();
        for (ProtoLeagueDTO.LeagueDTO leagueDTO : leagues) {
            leagueDataSource.insert(leagueDTO);
        }
    }

    private void loadTeamsData() {
        TeamManager teamManager = new TeamManager(context);
        teamManager.SetActionListener(new TeamManager.OnActionListener() {
            @Override
            public void OnAction(ProtoTeamListDTO.TeamListDTO teamList) {
                if (teamList != null) {
                    updateListViewTeams(teamList);
                }

                refreshed++;
                if(refreshed == 2) {
                    swipeRefreshLayout.setRefreshing(false);
                    pager.setCurrentItem(pager.getCurrentItem());
                }
            }
        });

        teamManager.getTeams();
    }

    private void updateListViewTeams(ProtoTeamListDTO.TeamListDTO teamList) {
        TeamDataSource teamDataSource = new TeamDataSource(dbHelper.getWritableDatabase());
        List<ProtoTeamDTO.TeamDTO> teams = teamDataSource.read();
        for (ProtoTeamDTO.TeamDTO teamDTO : teams) {
            teamDataSource.delete(teamDTO);
        }
        teams = teamList.getItemsList();
        for (ProtoTeamDTO.TeamDTO teamDTO : teams) {
            teamDataSource.insert(teamDTO);
        }
    }

    @Override
    public void onRefresh() {
        refreshed = 0;
        loadLeaguesData();
        loadTeamsData();
    }

    public class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(Context ctx, Activity act, FragmentManager fm) {
            super(fm);
            context = ctx;
            activity = act;
        }
        private final Context context;
        private final Activity activity;


        @Override
        public Fragment getItem(int position) {

            return MainHolderFragment.newInstance(position+1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);

            }
            return null;
        }
    }
}
