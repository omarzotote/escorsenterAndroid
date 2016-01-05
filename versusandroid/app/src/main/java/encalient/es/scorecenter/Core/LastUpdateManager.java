package encalient.es.scorecenter.Core;

import android.content.Context;
import android.support.v4.util.Pair;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import encalient.es.scorecenter.DataAccess.DataSources.DbHelper;
import encalient.es.scorecenter.DataAccess.DataSources.UpdatesDataSource;

/**
 * Created by EnCalientes on 10/22/2015.
 */
//Esta clase es para saber la fecha de la ultima actualización
public class LastUpdateManager {

    Context context;
    UpdatesDataSource updatesDataSource;

    public LastUpdateManager(Context _context) {
        context = _context;
        updatesDataSource = new UpdatesDataSource(new DbHelper(context).getWritableDatabase());
    }

    public void refreshLastUpdateDate() {
        String date = getLastUpdateDate();
        if(date != null) {
            updatesDataSource.delete(date);
        }
        DateFormat dateFormat = DateFormat.getDateInstance();
        updatesDataSource.insert(dateFormat.format(GregorianCalendar.getInstance().getTime()));
    }

    public void displayLastUpdateDate() {
        String date = getLastUpdateDate();
        if(date != null) {
            Toast.makeText(context, "Actualizado el: " + date, Toast.LENGTH_SHORT).show();
        }
    }

    private String getLastUpdateDate() {
        List<String> dates = updatesDataSource.read();
        String date = null;
        if(dates.size()!=0)
            date = dates.get(0);
        return date;
    }
}
