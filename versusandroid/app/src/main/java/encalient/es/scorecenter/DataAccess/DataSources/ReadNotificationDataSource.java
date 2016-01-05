package encalient.es.scorecenter.DataAccess.DataSources;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.encalient.ProtoNotificationDTO;

/**
 * Created by EnCalientes PC 3 on 3/24/2015.
 */
public class ReadNotificationDataSource extends  DataSource<ProtoNotificationDTO.NotificationDTO>{

    public static final String TABLE_NAME = "readNotifications";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OBJECT = "object";
    public static final String COLUMN_NAME = "name";

    // Database creation sql statement
    public static final String CREATE_COMMAND = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_OBJECT + " blob, "
            + COLUMN_NAME + " text not null);";

    public ReadNotificationDataSource(SQLiteDatabase database){
        super(database);
    }

    @Override
    public boolean insert(ProtoNotificationDTO.NotificationDTO entity) {
        if (entity == null) {
            return false;
        }

        long result = mDatabase.insert(TABLE_NAME, null, generateContentValuesFromObject(entity));
        return result != -1;
    }

    @Override
    public boolean delete(ProtoNotificationDTO.NotificationDTO notification) {
        if (notification == null) {
            return false;
        }

        int result = mDatabase.delete(TABLE_NAME,
                COLUMN_ID + " = " + notification.getId(), null);
        return result != 0;
    }

    @Override
    public boolean update(ProtoNotificationDTO.NotificationDTO notification) {
        if (notification == null) {
            return false;
        }

        int result = mDatabase.update(TABLE_NAME,
                generateContentValuesFromObject(notification), COLUMN_ID + " = "
                        + notification.getId(), null);
        return result != 0;
    }

    @Override
    public List<ProtoNotificationDTO.NotificationDTO> read() {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), null,
                null, null, null, null);
        List notifications = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                notifications.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return notifications;
    }

    @Override
    public List<ProtoNotificationDTO.NotificationDTO> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), selection,
                selectionArgs, groupBy, having, orderBy);
        List notifications = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                notifications.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return notifications;
    }

    public String[] getAllColumns() {
        return new String[] { COLUMN_ID, COLUMN_OBJECT, COLUMN_NAME };
    }

    public ProtoNotificationDTO.NotificationDTO generateObjectFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        ProtoNotificationDTO.NotificationDTO notification = null;

        try
        {
            notification = ProtoNotificationDTO.NotificationDTO.PARSER.parseFrom(cursor.getBlob(cursor.getColumnIndex(COLUMN_OBJECT)));
        }
        catch (Exception ex)
        {

        }

        return notification;
    }

    public ContentValues generateContentValuesFromObject(ProtoNotificationDTO.NotificationDTO notification) {
        if (notification == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, notification.getId());
        values.put(COLUMN_NAME, notification.getTitle());
        values.put(COLUMN_OBJECT, notification.toByteArray());
        return values;
    }
}
