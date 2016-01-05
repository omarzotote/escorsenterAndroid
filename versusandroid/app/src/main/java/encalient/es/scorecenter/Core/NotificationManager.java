package encalient.es.scorecenter.Core;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

//import com.tiendapago.app.ORMModels.CustomerORM;

//import com.tiendapago.app.DataAccess.CustomerLoader;

//import OuterProto.CustomerDTOOuterClass;

import java.util.List;

import encalient.es.scorecenter.DataAccess.Loaders.NotificationLoader;
import es.encalient.ProtoLeagueDTO;
import es.encalient.ProtoNotificationListDTO;
import es.encalient.ProtoTeamDTO;

/**
 * Created by nacho on 6/8/2015.
 */
public class NotificationManager {
    private NotificationLoader notificationLoader;
    public Context context;
    private ActionListener listener = null;

    public NotificationManager(Context ctx) {
        context = ctx;
        notificationLoader = new NotificationLoader(context);
    }

    public void SetActionListener(ActionListener l) {
        listener = l;
    }

    public void getNotifications(List<ProtoTeamDTO.TeamDTO> teamList, List<ProtoLeagueDTO.LeagueDTO> leagueList) {
        new GetNotifications(teamList, leagueList).execute();
    }

    private class GetNotifications extends AsyncTask<Void, Void, ProtoNotificationListDTO.NotificationListDTO> {

        private List<ProtoTeamDTO.TeamDTO> teamList;
        private List<ProtoLeagueDTO.LeagueDTO> leagueList;

        protected GetNotifications(List<ProtoTeamDTO.TeamDTO> _teamList, List<ProtoLeagueDTO.LeagueDTO> _leagueList) {
            teamList = _teamList;
            leagueList = _leagueList;
        }

        @Override
        protected ProtoNotificationListDTO.NotificationListDTO doInBackground(Void... params) {
            return notificationLoader.getNotificationList(0, teamList, leagueList);// 0 = online, 1 = offline
        }

        @Override
        protected void onPostExecute(ProtoNotificationListDTO.NotificationListDTO notificationList) {
            //Log.d("PruebaManager", "Total: " + customers.size());
            if (listener != null) {
                Log.d("PruebaManager", "Not null");
                listener.OnAction(notificationList);
            }
        }
    }

    public static class OnActionListener implements ActionListener {

        @Override
        public void OnAction() {

        }

        @Override
        public void OnAction(ProtoNotificationListDTO.NotificationListDTO notificationList) {

        }
    }


    public interface ActionListener {
        void OnAction();

        void OnAction(ProtoNotificationListDTO.NotificationListDTO notificationList);
    }
}
