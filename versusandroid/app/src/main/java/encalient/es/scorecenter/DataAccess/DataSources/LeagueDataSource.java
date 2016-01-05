package encalient.es.scorecenter.DataAccess.DataSources;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.encalient.ProtoLeagueDTO;

/**
 * Created by EnCalientes PC 3 on 3/24/2015.
 */
public class LeagueDataSource extends  DataSource<ProtoLeagueDTO.LeagueDTO>{

    public static final String TABLE_NAME = "league";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OBJECT = "object";
    public static final String COLUMN_NAME = "name";

    // Database creation sql statement
    public static final String CREATE_COMMAND = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_OBJECT + " blob, "
            + COLUMN_NAME + " text not null);";

    public LeagueDataSource(SQLiteDatabase database){
        super(database);
    }

    @Override
    public boolean insert(ProtoLeagueDTO.LeagueDTO entity) {
        if (entity == null) {
            return false;
        }

        long result = mDatabase.insert(TABLE_NAME, null, generateContentValuesFromObject(entity));
        return result != -1;
    }

    @Override
    public boolean delete(ProtoLeagueDTO.LeagueDTO league) {
        if (league == null) {
            return false;
        }

        int result = mDatabase.delete(TABLE_NAME,
                COLUMN_ID + " = " + league.getId(), null);
        return result != 0;
    }

    @Override
    public boolean update(ProtoLeagueDTO.LeagueDTO league) {
        if (league == null) {
            return false;
        }

        int result = mDatabase.update(TABLE_NAME,
                generateContentValuesFromObject(league), COLUMN_ID + " = "
                        + league.getId(), null);
        return result != 0;
    }

    @Override
    public List<ProtoLeagueDTO.LeagueDTO> read() {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), null,
                null, null, null, null);
        List leagues = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                leagues.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return leagues;
    }

    @Override
    public List<ProtoLeagueDTO.LeagueDTO> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), selection,
                selectionArgs, groupBy, having, orderBy);
        List leagues = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                leagues.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return leagues;
    }

    public String[] getAllColumns() {
        return new String[] { COLUMN_ID, COLUMN_OBJECT, COLUMN_NAME };
    }

    public ProtoLeagueDTO.LeagueDTO generateObjectFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        ProtoLeagueDTO.LeagueDTO league = null;

        try
        {
            league = ProtoLeagueDTO.LeagueDTO.PARSER.parseFrom(cursor.getBlob(cursor.getColumnIndex(COLUMN_OBJECT)));
        }
        catch (Exception ex)
        {

        }

        return league;
    }

    public ContentValues generateContentValuesFromObject(ProtoLeagueDTO.LeagueDTO league) {
        if (league == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, league.getId());
        values.put(COLUMN_NAME, league.getName());
        values.put(COLUMN_OBJECT, league.toByteArray());
        return values;
    }
}
