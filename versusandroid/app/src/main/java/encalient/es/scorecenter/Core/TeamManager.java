package encalient.es.scorecenter.Core;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import encalient.es.scorecenter.DataAccess.Loaders.ScoreLoader;
import encalient.es.scorecenter.DataAccess.Loaders.TeamLoader;
import es.encalient.ProtoScoreTableResultDTO;
import es.encalient.ProtoTeamListDTO;

/**
 * Created by EnCalientes PC 3 on 8/27/2015.
 */
public class TeamManager {
    private TeamLoader teamLoader;
    private ScoreLoader scoreLoader;
    public Context context;
    private ActionListener listener = null;

    public TeamManager(Context ctx) {
        context = ctx;
        teamLoader = new TeamLoader(context);
        scoreLoader = new ScoreLoader(context);
    }

    public void SetActionListener(ActionListener l) {
        listener = l;
    }

    public void getTeams() {
        new GetTeams().execute();
    }

    public void getScoreTableResult(long teamId){
        new GetScoreTableResult(teamId).execute();
    }

    private class GetScoreTableResult extends AsyncTask<Void, Void, ProtoScoreTableResultDTO.ScoreTableResultDTO>{
        private long teamId;

        public GetScoreTableResult(long _teamId) {
            super();
            teamId = _teamId;
        }

        @Override
        protected ProtoScoreTableResultDTO.ScoreTableResultDTO doInBackground(Void... params) {
            return scoreLoader.getScoreTableResult(teamId);
        }

        @Override
        protected void  onPostExecute(ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResult) {
            if(listener != null) {
                listener.OnAction(scoreTableResult);
            }
        }
    }

    private class GetTeams extends AsyncTask<Void, Void, ProtoTeamListDTO.TeamListDTO> {

        @Override
        protected ProtoTeamListDTO.TeamListDTO doInBackground(Void... params) {
            return teamLoader.getTeamsList(1);// 0 = online, 1 = offline
        }
        @Override
        protected void onPostExecute(ProtoTeamListDTO.TeamListDTO teamList) {
            if(listener != null) {

                Log.d("PruebaManager", "Not null");
                listener.OnAction(teamList);
            }
        }
    }


    public static class OnActionListener implements ActionListener {

        @Override
        public void OnAction() {

        }

        @Override
        public void OnAction(ProtoTeamListDTO.TeamListDTO teamList) {

        }

        @Override
        public void OnAction(ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResult) {

        }
    }

    public interface ActionListener{
        void OnAction();
        void OnAction(ProtoTeamListDTO.TeamListDTO teamList);
        void OnAction(ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResult);
    }

}
