package encalient.es.scorecenter.DataAccess.DataSources;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.encalient.ProtoTeamDTO;

/**
 * Created by EnCalientes PC 3 on 3/24/2015.
 */
public class FavoriteTeamDataSource extends  DataSource<ProtoTeamDTO.TeamDTO>{

    public static final String TABLE_NAME = "favoriteTeam";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OBJECT = "object";
    public static final String COLUMN_NAME = "name";

    // Database creation sql statement
    public static final String CREATE_COMMAND = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_OBJECT + " blob, "
            + COLUMN_NAME + " text not null);";

    public FavoriteTeamDataSource(SQLiteDatabase database){
        super(database);
    }

    @Override
    public boolean insert(ProtoTeamDTO.TeamDTO entity) {
        if (entity == null) {
            return false;
        }

        long result = mDatabase.insert(TABLE_NAME, null, generateContentValuesFromObject(entity));
        return result != -1;
    }

    @Override
    public boolean delete(ProtoTeamDTO.TeamDTO  team) {
        if (team == null) {
            return false;
        }

        int result = mDatabase.delete(TABLE_NAME,
                COLUMN_ID + " = " + team.getId(), null);
        return result != 0;
    }

    @Override
    public boolean update(ProtoTeamDTO.TeamDTO team) {
        if (team == null) {
            return false;
        }

        int result = mDatabase.update(TABLE_NAME,
                generateContentValuesFromObject(team), COLUMN_ID + " = "
                        + team.getId(), null);
        return result != 0;
    }

    @Override
    public List<ProtoTeamDTO.TeamDTO> read() {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), null,
                null, null, null, null);
        List teams = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                teams.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return teams;
    }

    @Override
    public List<ProtoTeamDTO.TeamDTO> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), selection,
                selectionArgs, groupBy, having, orderBy);
        List teams = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                teams.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return teams;
    }

    public String[] getAllColumns() {
        return new String[] { COLUMN_ID, COLUMN_OBJECT, COLUMN_NAME };
    }

    public ProtoTeamDTO.TeamDTO generateObjectFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        ProtoTeamDTO.TeamDTO team = null;

        try
        {
            team = ProtoTeamDTO.TeamDTO.PARSER.parseFrom(cursor.getBlob(cursor.getColumnIndex(COLUMN_OBJECT)));
        }
        catch (Exception ex)
        {

        }

        return team;
    }

    public ContentValues generateContentValuesFromObject(ProtoTeamDTO.TeamDTO team) {
        if (team == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, team.getId());
        values.put(COLUMN_NAME, team.getName());
        values.put(COLUMN_OBJECT, team.toByteArray());
        return values;
    }
}
