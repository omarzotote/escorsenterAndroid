package encalient.es.scorecenter.DataAccess.DataSources;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.Pair;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.encalient.ProtoLeagueDTO;

/**
 * Created by EnCalientes PC 3 on 3/24/2015.
 */
public class UpdatesDataSource extends  DataSource<String>{

    public static final String TABLE_NAME = "updates";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";

    // Database creation sql statement
    public static final String CREATE_COMMAND = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DATE + " text not null);";

    public UpdatesDataSource(SQLiteDatabase database){
        super(database);
    }

    @Override
    public boolean insert(String entity) {
        if (entity == null) {
            return false;
        }

        long result = mDatabase.insert(TABLE_NAME, null, generateContentValuesFromObject(entity));
        return result != -1;
    }

    @Override
    public boolean delete(String entity) {
        if (entity == null) {
            return false;
        }

        int result = mDatabase.delete(TABLE_NAME,
                COLUMN_ID + " = " + "1" , null);
        return result != 0;
    }

    @Override
    public boolean update(String entity) {
        if (entity == null) {
            return false;
        }

        int result = mDatabase.update(TABLE_NAME,
                generateContentValuesFromObject(entity), COLUMN_ID + " = "
                        + "1" , null);
        return result != 0;
    }

    @Override
    public List<String> read() {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), null,
                null, null, null, null);
        List<String> dates = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                dates.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return dates;
    }

    @Override
    public List<String> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), selection,
                selectionArgs, groupBy, having, orderBy);
        List<String> dates = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                dates.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return dates;
    }

    public String[] getAllColumns() {
        return new String[] { COLUMN_ID, COLUMN_DATE };
    }

    public String generateObjectFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        String date = null;

        try
        {
             date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        }
        catch (Exception ex)
        {

        }

        return date;
    }

    public ContentValues generateContentValuesFromObject(String date) {
        if (date == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        return values;
    }
}
