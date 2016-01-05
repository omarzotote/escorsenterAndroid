package encalient.es.scorecenter.Core;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import encalient.es.scorecenter.DataAccess.Loaders.LeagueLoader;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoLeagueListDTO;
import es.encalient.ProtoSeasonDTO;

//import com.tiendapago.app.ORMModels.CustomerORM;
//import com.tiendapago.app.DataAccess.CustomerLoader;
//import OuterProto.CustomerDTOOuterClass;

/**
 * Created by nacho on 6/8/2015.
 */
public class LeagueManager {
    private LeagueLoader leagueLoader;
    public Context context;
    private ActionListener listener = null;

    public LeagueManager(Context ctx) {
        context = ctx;
        leagueLoader = new LeagueLoader(context);

    }

    public void SetActionListener(ActionListener l) {
        listener = l;
    }

    public void getLeagues() {
        new GetLeagues().execute();
    }

    public void getFutureMatches(long leagueId) {
        new GetFutureMatches(leagueId).execute();
    }

    public void getPastMatches(long leagueId) {
        new GetPastMatches(leagueId).execute();
    }

    public void getScoreTableResult(long leagueId) {
        new GetScoreTableResult(leagueId).execute();
    }

    private class GetLeagues extends AsyncTask<Void, Void, ProtoLeagueListDTO.LeagueListDTO> {

        @Override
        protected ProtoLeagueListDTO.LeagueListDTO doInBackground(Void... params) {
            return leagueLoader.getLeagueList();// 0 = online, 1 = offline
        }

        @Override
        protected void onPostExecute(ProtoLeagueListDTO.LeagueListDTO leagueList) {
            if (listener != null) {
                listener.OnAction(leagueList);
            }
        }
    }

    private class GetFutureMatches extends AsyncTask<Void, Void, ProtoSeasonDTO.SeasonDTO> {
        long leagueId;

        public GetFutureMatches(long _leagueId) {
            leagueId = _leagueId;
        }

        @Override
        protected ProtoSeasonDTO.SeasonDTO doInBackground(Void... params) {
            return leagueLoader.getMatches(leagueId);
        }

        @Override
        protected void onPostExecute(ProtoSeasonDTO.SeasonDTO season) {
            if (listener != null) {
                Log.d("LeagueManager", "Not null");
                listener.OnAction(season);
            }
        }
    }

    private class GetScoreTableResult extends AsyncTask<Void, Void, ProtoSeasonDTO.SeasonDTO> {

        private long leagueId;

        public GetScoreTableResult(long _leagueId) {
            leagueId = _leagueId;
        }

        @Override
        protected ProtoSeasonDTO.SeasonDTO doInBackground(Void... params) {
            return leagueLoader.getScoreTableResult(leagueId);// 0 = online, 1 = offline
        }

        @Override
        protected void onPostExecute(ProtoSeasonDTO.SeasonDTO season) {
            if (listener != null) {
                Log.d("PruebaManager", "Not null");
                listener.OnAction(season);
            }
        }
    }

    private class GetPastMatches extends AsyncTask<Void, Void, ProtoSeasonDTO.SeasonDTO> {
        long leagueId;

        public GetPastMatches(long _leagueId) {
            leagueId = _leagueId;
        }

        @Override
        protected ProtoSeasonDTO.SeasonDTO doInBackground(Void... params) {
            return leagueLoader.getPastMatches(leagueId);
        }

        @Override
        protected void onPostExecute(ProtoSeasonDTO.SeasonDTO season) {
            if (listener != null) {
                Log.d("LeagueManager", "Not null");
                listener.OnAction(season);
            }
        }
    }


    public static class OnActionListener implements ActionListener {

        @Override
        public void OnAction() {

        }

        @Override
        public void OnAction(ProtoLeagueListDTO.LeagueListDTO notificationList) {

        }

        public void OnAction(ProtoSeasonDTO.SeasonDTO season) {

        }

    }


    public interface ActionListener {
        void OnAction();

        void OnAction(ProtoLeagueListDTO.LeagueListDTO notificationList);

        void OnAction(ProtoSeasonDTO.SeasonDTO season);
    }
}
