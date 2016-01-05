package encalient.es.scorecenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import encalient.es.scorecenter.Common.ActivityStack;
import encalient.es.scorecenter.Common.CircleTransform;
import encalient.es.scorecenter.Core.LeagueManager;
import encalient.es.scorecenter.Core.TeamManager;
import encalient.es.scorecenter.DataAccess.DataSources.DbHelper;
import encalient.es.scorecenter.DataAccess.DataSources.FavoriteTeamDataSource;
import encalient.es.scorecenter.DataAccess.DataSources.TeamScoreTableResultDataSource;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoLeagueListDTO;
import es.encalient.ProtoScoreTableResultDTO;
import es.encalient.ProtoSeasonDTO;
import es.encalient.ProtoTeamDTO;


public class TeamActivity extends AppCompatActivity {

    private ProtoTeamDTO.TeamDTO teamDTO;
    private DbHelper dbHelper = new DbHelper(this);
    private FavoriteTeamDataSource favoriteTeamDataSource;
    private SwipeRefreshLayout swiper;
    private ProtoSeasonDTO.SeasonDTO season;
    private ProtoLeagueListDTO.LeagueListDTO leagueListDTO;
    private ProtoLeagueDTO.LeagueDTO teamLeague;
    private int position;
    private List<ProtoLeagueDTO.LeagueDTO> leagues;
    private List<ProtoScoreTableResultDTO.ScoreTableResultDTO> scores;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = this.getIntent();

        teamDTO = (ProtoTeamDTO.TeamDTO) intent.getSerializableExtra("Team");

        getSupportActionBar().setTitle(teamDTO.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView TeamImage = (ImageView) findViewById(R.id.teamimg);
        // Set image


        //Put rounded image
        if(teamDTO.hasLogo() == true){
            Picasso.with(this)
                    .load(teamDTO.getLogo())
                    .placeholder(R.drawable.new_team)
                    .transform(new CircleTransform())
                    .into(TeamImage);
        }else{
            Picasso.with(this)
                    .load(R.drawable.team)
                    .placeholder(R.drawable.new_team)
                    .transform(new CircleTransform())
                    .into(TeamImage);
        }
        // Get Score Table

        final Button favoritesButton = (Button) findViewById(R.id.favoritesbutton);
        // set button text
        if (isFavorite()) {
            ((View) favoritesButton).setBackgroundResource(R.drawable.quit_favorites_button);
            favoritesButton.setText(getString(R.string.quit_favorites));
        } else {
            ((View) favoritesButton).setBackgroundResource(R.drawable.favorites_button);
            favoritesButton.setText(getString(R.string.add_favorites));
        }

        // toggle Favorites
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (favoritesButton.getText().equals(getString(R.string.add_favorites))) {
                    addFavorites();
                    // favoritesButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.quit_favorites_button));
                    v.setBackgroundResource(R.drawable.quit_favorites_button);
                    favoritesButton.setText(getString(R.string.quit_favorites));

                } else {
                    removeFavorites();
                    // favoritesButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorites_button));
                    v.setBackgroundResource(R.drawable.favorites_button);
                    favoritesButton.setText(getString(R.string.add_favorites));
                }
            }
        });

        swiper = (SwipeRefreshLayout) findViewById(R.id.Scroll);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        swiper.setColorSchemeColors(getResources().getColor(R.color.primaryColor));
        showTemporaryScoreTableResult();
        swiper.setRefreshing(true);
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();

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
        getMenuInflater().inflate(R.menu.team_activity_actions, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id) {
        case android.R.id.home:
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

    private void loadData() {
        TeamManager teamManager = new TeamManager(this);
        teamManager.SetActionListener(new TeamManager.OnActionListener() {
            @Override
            public void OnAction(ProtoScoreTableResultDTO.ScoreTableResultDTO _scoreTableResult) {
                if(_scoreTableResult != null) {
                    updateScoreTableResult(_scoreTableResult);
                    swiper.setRefreshing(false);
                }
            }
        });
        teamManager.getScoreTableResult(teamDTO.getId());
    }

    private boolean isFavorite() {
        List<ProtoTeamDTO.TeamDTO> teamList;
        FavoriteTeamDataSource favoriteTeamDataSource = new FavoriteTeamDataSource(dbHelper.getReadableDatabase());
        teamList = favoriteTeamDataSource.read();
        for (ProtoTeamDTO.TeamDTO team : teamList) {
            if (teamDTO.getId() == team.getId()) {
                return true;
            }
        }
        return false;
    }

    private void addFavorites() {

        favoriteTeamDataSource = new FavoriteTeamDataSource(dbHelper.getWritableDatabase());
        favoriteTeamDataSource.insert(teamDTO);
    }

    private void removeFavorites() {
        favoriteTeamDataSource = new FavoriteTeamDataSource(dbHelper.getWritableDatabase());
        favoriteTeamDataSource.delete(teamDTO);
    }

    //Mostrar datos almacenados en la base de datos local
    private void showTemporaryScoreTableResult() {
        TeamScoreTableResultDataSource teamScoreTableResultDataSource = new TeamScoreTableResultDataSource(dbHelper.getReadableDatabase());
        List<ProtoScoreTableResultDTO.ScoreTableResultDTO> teamScore = teamScoreTableResultDataSource.read();

        ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResult = null;
        for (ProtoScoreTableResultDTO.ScoreTableResultDTO t : teamScore) {
            if (t.getTeam().getId() == teamDTO.getId()) {
                scoreTableResult = t;
            }
        }

        if(scoreTableResult != null) {
            displayScore(scoreTableResult);
        }
    }

    private void updateScoreTableResult(ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResult) {
        TeamScoreTableResultDataSource teamScoreTableResultDataSource = new TeamScoreTableResultDataSource(dbHelper.getWritableDatabase());
        List<ProtoScoreTableResultDTO.ScoreTableResultDTO> teamScore = teamScoreTableResultDataSource.read();

        for (ProtoScoreTableResultDTO.ScoreTableResultDTO t : teamScore) {
            if (t.getTeam().getId() == teamDTO.getId()) {
                teamScoreTableResultDataSource.delete(t);
            }
        }
        teamScoreTableResultDataSource.insert(scoreTableResult);

        if(scoreTableResult != null) {
            displayScore(scoreTableResult);
        }
    }

    //Poner texto en los TextViews
    private void displayScore(ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResult) {
        TextView tname = (TextView) findViewById(R.id.tname);
        tname.setText(scoreTableResult.getTeam().getName());
        TextView tleague = (TextView) findViewById(R.id.tleague);
        tleague.setText(scoreTableResult.getLeague());
        TextView tgames = (TextView) findViewById(R.id.tgames);
        tgames.setText(Long.toString(scoreTableResult.getGamesPlayed()));
        TextView twins = (TextView) findViewById(R.id.twins);
        twins.setText(Long.toString(scoreTableResult.getGamesWined()));
        TextView tlost = (TextView) findViewById(R.id.tlost);
        tlost.setText(Long.toString(scoreTableResult.getGamesLost()));
        TextView tdraw = (TextView) findViewById(R.id.tdraw);
        tdraw.setText(Long.toString(scoreTableResult.getGamesDrawn()));
        TextView tgf = (TextView) findViewById(R.id.tgf);
        tgf.setText(Long.toString(scoreTableResult.getScoreFavor()));
        TextView tgc = (TextView) findViewById(R.id.tgc);
        tgc.setText(Long.toString(scoreTableResult.getScoreAgainst()));
        TextView tdif = (TextView) findViewById(R.id.tdif);
        tdif.setText(Long.toString(scoreTableResult.getScoreDifference()));
        TextView tp = (TextView) findViewById(R.id.tp);
        tp.setText(Long.toString(scoreTableResult.getPoints()));
        TextView tpoints = (TextView) findViewById(R.id.tpoints);
        tpoints.setText(Long.toString(scoreTableResult.getPoints()) + " Pts");
    }
}