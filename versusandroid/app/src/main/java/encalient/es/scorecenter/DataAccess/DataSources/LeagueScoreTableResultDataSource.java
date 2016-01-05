package encalient.es.scorecenter.DataAccess.DataSources;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.encalient.ProtoNotificationDTO;
import es.encalient.ProtoSeasonDTO;

/**
 * Created by EnCalientes PC 3 on 3/24/2015.
 */
public class LeagueScoreTableResultDataSource extends  DataSource<ProtoSeasonDTO.SeasonDTO>{

    public static final String TABLE_NAME = "leagueScoreTableResult";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OBJECT = "object";
    public static final String COLUMN_NAME = "name";

    // Database creation sql statement
    public static final String CREATE_COMMAND = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_OBJECT + " blob, "
            + COLUMN_NAME + " text not null);";

    public LeagueScoreTableResultDataSource(SQLiteDatabase database){
        super(database);
    }

    @Override
    public boolean insert(ProtoSeasonDTO.SeasonDTO season) {
        if (season == null) {
            return false;
        }

        long result = mDatabase.insert(TABLE_NAME, null, generateContentValuesFromObject(season));
        return result != -1;
    }

    @Override
    public boolean delete(ProtoSeasonDTO.SeasonDTO season) {
        if (season == null) {
            return false;
        }

        int result = mDatabase.delete(TABLE_NAME,
                COLUMN_ID + " = " + season.getId(), null);
        return result != 0;
    }

    @Override
    public boolean update(ProtoSeasonDTO.SeasonDTO season) {
        if (season == null) {
            return false;
        }

        int result = mDatabase.update(TABLE_NAME,
                generateContentValuesFromObject(season), COLUMN_ID + " = "
                        + season.getId(), null);
        return result != 0;
    }



    @Override
    public List<ProtoSeasonDTO.SeasonDTO> read() {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), null,
                null, null, null, null);
        List seasons = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                seasons.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return seasons;
    }

    @Override
    public List<ProtoSeasonDTO.SeasonDTO> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), selection,
                selectionArgs, groupBy, having, orderBy);
        List seasons = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                seasons.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return seasons;
    }

    public String[] getAllColumns() {
        return new String[] { COLUMN_ID, COLUMN_OBJECT, COLUMN_NAME };
    }

    public ProtoSeasonDTO.SeasonDTO generateObjectFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        ProtoSeasonDTO.SeasonDTO season = null;

        try
        {
            season = ProtoSeasonDTO.SeasonDTO.PARSER.parseFrom(cursor.getBlob(cursor.getColumnIndex(COLUMN_OBJECT)));
        }
        catch (Exception ex)
        {

        }

        return season;
    }

    public ContentValues generateContentValuesFromObject(ProtoSeasonDTO.SeasonDTO season) {
        if (season == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, season.getId());
        values.put(COLUMN_NAME, season.getTitle());
        values.put(COLUMN_OBJECT, season.toByteArray());
        return values;
    }
}
