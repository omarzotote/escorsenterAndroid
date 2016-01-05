package encalient.es.scorecenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import encalient.es.scorecenter.Adapters.NotificationCellAdapter;
import encalient.es.scorecenter.Common.ActivityStack;
import encalient.es.scorecenter.Core.LastUpdateManager;
import encalient.es.scorecenter.Core.NotificationManager;
import encalient.es.scorecenter.DataAccess.DataSources.DbHelper;
import encalient.es.scorecenter.DataAccess.DataSources.FavoriteLeagueDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.NotificationsDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.ReadNotificationDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.FavoriteTeamDataSource;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoNotificationDTO;
import es.encalient.ProtoNotificationListDTO;
import es.encalient.ProtoTeamDTO;


public class NotificationsActivity extends AppCompatActivity {


    private NotificationCellAdapter mAdapter = null;
    private NotificationManager notificationManager = null;
    private ArrayList<Long> Favorites = new ArrayList<Long>();
    private HashSet<ProtoNotificationDTO.NotificationDTO> NotificationsHashSet = new HashSet<>();
    private SwipeRefreshLayout notificationsSwipe;
    Toolbar toolbar;
    DbHelper dbHelper;
    LastUpdateManager lastUpdateManager;
    boolean connected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        dbHelper = new DbHelper(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notificaciones");

        lastUpdateManager = new LastUpdateManager(this);

        notificationsSwipe = (SwipeRefreshLayout) findViewById(R.id.notifactionSwipeContainer);
        notificationsSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isConnected()) {
                    notificationsSwipe.setRefreshing(false);
                }
                else {
                    loadData();
                }
            }
        });
        notificationsSwipe.setColorSchemeColors(getResources().getColor(R.color.primaryColor));
        displayTemporaryListView();

        if(!isConnected()){
            lastUpdateManager.displayLastUpdateDate();
        }
    }
    //checar conexion a internet
    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
           return true;
        }
        else
           return false;

    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_notifications, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.explorar:
                startExploring();
                return true;
            case R.id.favoritos:
                startFavorites();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resources getResources() {
        return new ResourceFix(super.getResources());
    }

    private class ResourceFix extends Resources {
        private int targetId = 0;

        ResourceFix(Resources resources) {
            super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
            targetId = Resources.getSystem().getIdentifier("split_action_bar_is_narrow", "bool", "android");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean getBoolean(int id) throws Resources.NotFoundException {
            return targetId == id || super.getBoolean(id);
        }
    }

    private void startFavorites() {
        ActivityStack.selectedTabIndexMainActivity = 0;
        Intent intentGoToFavorites = new Intent(this, FavoritesActivity.class);
        startActivity(intentGoToFavorites);
    }

    private void startExploring() {
        ActivityStack.selectedTabIndexMainActivity = 0;
        Intent intentGoToExploring = new Intent(this, MainActivity.class);
        startActivity(intentGoToExploring);
    }

    private void loadData() {
        ReadNotificationDataSource readNotificationsDataSource = new ReadNotificationDataSource(dbHelper.getReadableDatabase());
        List<ProtoNotificationDTO.NotificationDTO> readNotificationListDb = readNotificationsDataSource.read();
        for (ProtoNotificationDTO.NotificationDTO notification : readNotificationListDb) {
            NotificationsHashSet.add(notification);
        }

        List<ProtoLeagueDTO.LeagueDTO> leagueList;
        FavoriteLeagueDataSource favoriteLeagueDataSource = new FavoriteLeagueDataSource(dbHelper.getReadableDatabase());
        leagueList = favoriteLeagueDataSource.read();

        List<ProtoTeamDTO.TeamDTO> teamList;
        FavoriteTeamDataSource favoriteTeamDataSource = new FavoriteTeamDataSource(dbHelper.getReadableDatabase());
        teamList = favoriteTeamDataSource.read();

        notificationManager = new NotificationManager(getApplicationContext());
        notificationManager.SetActionListener(new NotificationManager.OnActionListener() {
            @Override
            public void OnAction(ProtoNotificationListDTO.NotificationListDTO notificationList) {
                if(notificationList != null) {
                    updateListView(notificationList);
                    notificationsSwipe.setRefreshing(false);
                }
            }
        });

        notificationManager.getNotifications(teamList, leagueList);
    }
    //Mostrar contenido de base de datos local
    private void displayTemporaryListView() {
        ReadNotificationDataSource readNotificationsDataSource = new ReadNotificationDataSource(dbHelper.getReadableDatabase());
        List<ProtoNotificationDTO.NotificationDTO> readNotificationListDb = readNotificationsDataSource.read();
        for (ProtoNotificationDTO.NotificationDTO notification : readNotificationListDb) {
            NotificationsHashSet.add(notification);
        }

        NotificationsDataSource notificationsDataSource = new NotificationsDataSource(dbHelper.getReadableDatabase());
        List<ProtoNotificationDTO.NotificationDTO> notificationListDb = notificationsDataSource.read();

        if(notificationListDb != null) {
            display(notificationListDb);
        }
    }

    private void updateListView(ProtoNotificationListDTO.NotificationListDTO notificationList) {

        NotificationsDataSource notificationsDataSource = new NotificationsDataSource(dbHelper.getWritableDatabase());
        List<ProtoNotificationDTO.NotificationDTO> notificationsList = notificationsDataSource.read();
        List<ProtoNotificationDTO.NotificationDTO> notifications = notificationList.getItemsList();

        // Forget old notifications
        for (ProtoNotificationDTO.NotificationDTO n : notificationsList) {
            notificationsDataSource.delete(n);
        }

        // Forget old read notifications
        ReadNotificationDataSource readNotificationDataSource = new ReadNotificationDataSource(dbHelper.getWritableDatabase());
        for (ProtoNotificationDTO.NotificationDTO n : NotificationsHashSet) {
            if (!notifications.contains(n)) {
                readNotificationDataSource.delete(n);
                NotificationsHashSet.remove(n);
            }
        }

        // Save non-read notifications
        for (ProtoNotificationDTO.NotificationDTO n : notifications) {
            if (!NotificationsHashSet.contains(n)) {
                notificationsDataSource.insert(n);
            }
        }

        lastUpdateManager.refreshLastUpdateDate();
        display(notifications);
    }

    private void display(List<ProtoNotificationDTO.NotificationDTO> notifications) {
        // Only display non-read notifications
        ArrayList<ProtoNotificationDTO.NotificationDTO> Notifications = new ArrayList<>();

        for (ProtoNotificationDTO.NotificationDTO n : notifications) {
            if (!NotificationsHashSet.contains(n)) {
                Notifications.add(n);
            }
        }

        mAdapter = new NotificationCellAdapter(getApplicationContext(), Notifications);
        ListView listView = (ListView) findViewById(R.id.notificationList);
        listView.setAdapter(mAdapter);
    }
}