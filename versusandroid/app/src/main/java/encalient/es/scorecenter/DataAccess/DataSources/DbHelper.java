package encalient.es.scorecenter.DataAccess.DataSources;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.Future;

/**
 * Created by EnCalientes PC 3 on 3/24/2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "versusDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FavoriteTeamDataSource.CREATE_COMMAND);
        db.execSQL(FavoriteLeagueDataSource.CREATE_COMMAND);
        db.execSQL(ReadNotificationDataSource.CREATE_COMMAND);
        db.execSQL(TeamDataSource.CREATE_COMMAND);
        db.execSQL(LeagueDataSource.CREATE_COMMAND);
        db.execSQL(NotificationsDataSource.CREATE_COMMAND);
        db.execSQL(PastMatchesDataSource.CREATE_COMMAND);
        db.execSQL(FutureMatchesDataSource.CREATE_COMMAND);
        db.execSQL(LeagueScoreTableResultDataSource.CREATE_COMMAND);
        db.execSQL(TeamScoreTableResultDataSource.CREATE_COMMAND);
        db.execSQL(UpdatesDataSource.CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteTeamDataSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + FavoriteLeagueDataSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + ReadNotificationDataSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TeamDataSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + LeagueDataSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + NotificationsDataSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + PastMatchesDataSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + FutureMatchesDataSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + LeagueScoreTableResultDataSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TeamScoreTableResultDataSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + UpdatesDataSource.TABLE_NAME);

        onCreate(db);
    }
}
