package encalient.es.scorecenter.Adapters;


import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import encalient.es.scorecenter.DataAccess.DataSources.DbHelper;
import encalient.es.scorecenter.DataAccess.DataSources.ReadNotificationDataSource;
import es.encalient.ProtoNotificationDTO;
import encalient.es.scorecenter.*;

/**
 * Created by nacho on 6/8/2015.
 */
public class NotificationCellAdapter extends ArrayAdapter<ProtoNotificationDTO.NotificationDTO> {
    private final Context context;
    private final ArrayList<ProtoNotificationDTO.NotificationDTO> values;
    private DbHelper dbHelper;
    private ReadNotificationDataSource readNotificationDataSource;

    boolean hasRead;

    public NotificationCellAdapter(Context ctx, ArrayList<ProtoNotificationDTO.NotificationDTO> vals) {
        super(ctx, R.layout.notification_cell, vals);
        context = ctx;
        values = vals;
        dbHelper = new DbHelper(context);
    }

    public void hasRead(long id){
        readNotificationDataSource = new ReadNotificationDataSource(dbHelper.getReadableDatabase());
        List<ProtoNotificationDTO.NotificationDTO> notifications = readNotificationDataSource.read();
        hasRead = false;
        for(ProtoNotificationDTO.NotificationDTO notification : notifications) {
            if(notification.getId() == id) {
                hasRead = true;
            }
        }
    }

    public void addToReads(ProtoNotificationDTO.NotificationDTO notification){
        readNotificationDataSource = new ReadNotificationDataSource(dbHelper.getWritableDatabase());
        readNotificationDataSource.insert(notification);
    }

    public void removeReads(ProtoNotificationDTO.NotificationDTO notification){
        readNotificationDataSource = new ReadNotificationDataSource(dbHelper.getWritableDatabase());
        readNotificationDataSource.delete(notification);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.notification_cell, parent, false);
        TextView notificationTitle = (TextView) rowView.findViewById(R.id.notificationTitle);
        TextView notificationDescription = (TextView) rowView.findViewById(R.id.notificationDescription);
        final TextView newOrRead = (TextView) rowView.findViewById(R.id.newOrRead);
        final Button addToCalendar = (Button) rowView.findViewById(R.id.addToCalendar);
        final Button markAsRead = (Button) rowView.findViewById(R.id.markAsRead);
        final ProtoNotificationDTO.NotificationDTO notification = values.get(position);
        final String completeDate[] = notification.getDate().split(" ",2);
        final String MDY[] = completeDate[0].split("/",3);
        final String time[] = completeDate[1].split(":", 3);


        hasRead(notification.getId());
        if(hasRead) {
            rowView.setAlpha(0.4f);
            markAsRead.setText(context.getString(R.string.mark_as_not_read));
            newOrRead.setText(context.getString(R.string.read_notification_text));
        } else {
            rowView.setAlpha(1);
            markAsRead.setText(context.getString(R.string.mark_as_read));
            newOrRead.setText(context.getString(R.string.new_notification_text));
        }

        markAsRead.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!hasRead) {

                    rowView.setAlpha(0.4f);
                    markAsRead.setText(context.getString(R.string.mark_as_not_read));
                    newOrRead.setText(context.getString(R.string.read_notification_text));
                    addToReads(notification);
                } else {
                    rowView.setAlpha(1);
                    markAsRead.setText(context.getString(R.string.mark_as_read));
                    newOrRead.setText(context.getString(R.string.new_notification_text));
                    removeReads(notification);
                }
                hasRead = !hasRead;
            }
        });

        //Aquì empieza la parte de añadir un evento al calendario

        addToCalendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Esto es importante

                calIntent.setData(CalendarContract.Events.CONTENT_URI);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, notification.getTitle());
                calIntent.putExtra(CalendarContract.Events.DESCRIPTION, notification.getContent());

                GregorianCalendar calDate = new GregorianCalendar(Integer.parseInt(MDY[2]),
                        Integer.parseInt(MDY[0])-1, Integer.parseInt(MDY[1]),
                        Integer.parseInt(time[0]), Integer.parseInt(time[1]));//Año, mes, dia, hora, minuto


                calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis());

                calIntent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
                calIntent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                context.startActivity(calIntent);
            }
        });

        if(notification != null) {
            notificationTitle.setText(notification.getTitle());
            notificationDescription.setText(notification.getContent());
        }


        Log.d("Notification", "" + notification.getTitle());


        return rowView;
    }
}
