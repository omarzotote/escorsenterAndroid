package encalient.es.scorecenter.DataAccess.DataSources;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.encalient.ProtoScoreTableResultDTO;
import es.encalient.ProtoSeasonDTO;

/**
 * Created by EnCalientes on 10/12/2015.
 */
public class TeamScoreTableResultDataSource extends DataSource<ProtoScoreTableResultDTO.ScoreTableResultDTO> {


    public static final String TABLE_NAME = "teamScoreTableResult";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OBJECT = "object";
    public static final String COLUMN_NAME = "name";

    // Database creation sql statement
    public static final String CREATE_COMMAND = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_OBJECT + " blob, "
            + COLUMN_NAME + " text not null);";



    public TeamScoreTableResultDataSource(SQLiteDatabase database) {
        super(database);
    }

    @Override
    public boolean insert(ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResult) {
        if (scoreTableResult == null) {
            return false;
        }

        long result = mDatabase.insert(TABLE_NAME, null, generateContentValuesFromObject(scoreTableResult));
        return result != -1;
    }

    @Override
    public boolean delete(ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResult) {
        if (scoreTableResult == null) {
            return false;
        }

        int result = mDatabase.delete(TABLE_NAME,
                COLUMN_ID + " = " + scoreTableResult.getTeam().getId(), null);
        return result != 0;
    }

    @Override
    public boolean update(ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResult) {
        if (scoreTableResult == null) {
            return false;
        }

        int result = mDatabase.update(TABLE_NAME,
                generateContentValuesFromObject(scoreTableResult), COLUMN_ID + " = "
                        + scoreTableResult.getTeam().getId(), null);
        return result != 0;
    }

    @Override
    public List read() {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), null,
                null, null, null, null);
        List scoreTableResults = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                scoreTableResults.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return scoreTableResults;
    }

    public String[] getAllColumns() {
        return new String[] { COLUMN_ID, COLUMN_OBJECT, COLUMN_NAME };
    }

    @Override
    public List read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), selection,
                selectionArgs, groupBy, having, orderBy);
        List scoreTableResults = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                scoreTableResults.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return scoreTableResults;
    }

    public ProtoScoreTableResultDTO.ScoreTableResultDTO generateObjectFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResultDTO = null;

        try
        {
            scoreTableResultDTO = ProtoScoreTableResultDTO.ScoreTableResultDTO.PARSER.parseFrom(cursor.getBlob(cursor.getColumnIndex(COLUMN_OBJECT)));
        }
        catch (Exception ex)
        {

        }

        return scoreTableResultDTO;
    }

    public ContentValues generateContentValuesFromObject(ProtoScoreTableResultDTO.ScoreTableResultDTO scoreTableResult) {
        if (scoreTableResult == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, scoreTableResult.getTeam().getId());
        values.put(COLUMN_NAME, scoreTableResult.getTeam().getName());
        values.put(COLUMN_OBJECT, scoreTableResult.toByteArray());
        return values;
    }
}
