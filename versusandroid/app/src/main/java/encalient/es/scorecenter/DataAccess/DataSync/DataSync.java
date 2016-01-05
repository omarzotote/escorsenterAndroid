package encalient.es.scorecenter.DataAccess.DataSync;

import android.content.Context;
import android.util.Log;

//import com.activeandroid.query.Select;
//import com.tiendapago.app.Core.CoreManager;
//import com.tiendapago.app.Core.ICustomerManager;
//import com.tiendapago.app.Helpers.CommonEventHelper.CommonEventHelper;
//import com.tiendapago.app.Helpers.ConnectionHelper;
//import com.tiendapago.app.Helpers.CustomerUploader.CustomerUploader;
//import com.tiendapago.app.Helpers.CustomerUploader.CustomerUploaderResponse;
//import com.tiendapago.app.ORMModels.CustomerORM;
//import com.tiendapago.app.StaticClasses.SyncItems;

import java.util.List;

//import OuterProto.CustomerDTOOuterClass;


/**
 * Created by joseag on 1/8/2015.
 */
/*public class DataSync
{
    private static DataSync dataSync;
    private boolean isSyncInProgress = false;
    private boolean reSync = false;
    private ConnectionHelper connectionHelper;
    public Context context;
    private DataSyncListener listener = null;

    private String messageError = "";

    //This needs to be a Singleton
    private DataSync(Context ctx)
    {
        context = ctx;
        connectionHelper = new ConnectionHelper();
        Log.d("Sync", "Created");
    }

    public static DataSync getDataSync(Context ctx)
    {
        if (null == dataSync)
        {
            dataSync = new DataSync(ctx);
        }
        return dataSync;
    }

    public boolean startSync(DataSyncListener l) {
        listener = l;
        return startSync();
    }

    public boolean getSyncStatus() {
        return isSyncInProgress;
    }

    public void setSyncStatus(boolean s) {
        isSyncInProgress = s;
    }

    public boolean getReSync() {
        return reSync;
    }

    public void setReSync(boolean s) {
        reSync = s;
    }

    public boolean canSync() {
        if (isSyncInProgress) {
            Log.d("Sync", "isSyncInProgress");
            messageError = "Ya se esta efectuando la sincronización.";
            return false;
        }

        //There is no connection
        if (!connectionHelper.getConnectionStatus(context)) {
            Log.d("Sync", "!connectionHelper.getConnectionStatus()");
            messageError = "No se puede efectuar la sincronización sin estar conectado a internet.";
            setSyncStatus(false);
            return false;
        }

        return true;
    }

    public boolean startSync() {
        Log.d("Sync", "Start");
        //It is already doing the sync
        if(canSync()) {
            Log.d("Sync", "SyncThread().run()");
            new SyncThread().run();
        } else {
            if(listener != null) {
                listener.OnError(messageError);
            }
        }
        return getSyncStatus();
    }

    private class SyncThread extends Thread {
        int position = 1;
        int total = 0;
        public void run() {
            isSyncInProgress = true;
            ICustomerManager cm = CoreManager.GetInstance(context).GetService("CustomerManager");
            final List<CustomerORM> customersToSyncList = new Select().all().from(CustomerORM.class).where("SendToReview = ?", true).execute();
            SyncItems.pending = customersToSyncList.size();
            if(customersToSyncList.size() > 0) {
                for(CustomerORM cust : customersToSyncList) {
                    final CustomerORM auxCust = cust;
                    CustomerDTOOuterClass.CustomerDTO c = cm.getCustomer(auxCust.cid);
                    new CustomerUploader(context, c, position).execute(new CustomerUploaderResponse() {
                        @Override
                        public void OnResponse(boolean s, int p) {
                            Log.d("Sync", "CustomerUploadedResponse");
                            if(s) {
                                Log.d("EnvioCustomerR", "Ya chingue");
                            }
                            auxCust.sendToReview = false;
                            auxCust.stackindex = 0;
                            auxCust.save();
                            SyncItems.pending--;
                            CommonEventHelper.TriggerEventsNamespace("Sync", null);
                            if(SyncItems.pending == 0) {
                                setSyncStatus(false);
                                if(!getReSync()) {
                                    if(listener != null) {
                                        listener.OnFinish();
                                    }
                                } else {
                                    new SyncThread().run();
                                }
                            }
                        }
                    });
                    position++;
                }
            } else {
                setSyncStatus(false);
                if(listener != null) {
                    listener.OnFinish();
                }
            }
        }
    }
}*/
