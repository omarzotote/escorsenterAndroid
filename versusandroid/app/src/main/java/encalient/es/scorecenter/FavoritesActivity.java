package encalient.es.scorecenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import encalient.es.scorecenter.Adapters.LeagueCellAdapter;
import encalient.es.scorecenter.Adapters.TeamCellAdapter;
import encalient.es.scorecenter.Common.ActivityStack;
import encalient.es.scorecenter.Core.LeagueManager;
import encalient.es.scorecenter.DataAccess.DataSources.DbHelper;
import encalient.es.scorecenter.DataAccess.DataSources.FavoriteLeagueDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.TeamContentProvider;
import encalient.es.scorecenter.DataAccess.DataSources.FavoriteTeamDataSource;
import encalient.es.scorecenter.PlaceHolderFragment.FavoritesHolderFragment;
import encalient.es.scorecenter.tabs.SlidingTabLayout;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoTeamDTO;


public class FavoritesActivity extends AppCompatActivity {

    private LeagueCellAdapter mLeagueAdapter = null;
    private LeagueManager leagueManager = null;
    Toolbar toolbar;
    SlidingTabLayout tabs;
    ViewPager pager;
    FavoritesPagerAdapter mFavoritesPagerAdapter;
    final int REQUEST_CODE_SECOND_ACTIVITY = 2;
    final int RESULT_CANCEL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFavoritesPagerAdapter = new FavoritesPagerAdapter(getApplicationContext(), this, getSupportFragmentManager());
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(mFavoritesPagerAdapter);
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);
        tabs.setSelectedIndicatorColors(getResources().getColor(R.color.accentColor));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        pager.setAdapter(null);
        pager.setAdapter(mFavoritesPagerAdapter);
        if (requestCode == REQUEST_CODE_SECOND_ACTIVITY && resultCode == RESULT_CANCEL) {
            pager.setCurrentItem(1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pager.setAdapter(null);
        pager.setAdapter(mFavoritesPagerAdapter);

        pager.setCurrentItem(ActivityStack.selectedTabIndexMainActivity);

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

    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public class FavoritesPagerAdapter extends FragmentPagerAdapter {

        public FavoritesPagerAdapter(Context ctx, Activity act, FragmentManager fm) {
            super(fm);
            context = ctx;
            activity = act;
        }

        private final Context context;
        private final Activity activity;


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return FavoritesHolderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
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
