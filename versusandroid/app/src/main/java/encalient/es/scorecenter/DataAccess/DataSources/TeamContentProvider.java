package encalient.es.scorecenter.DataAccess.DataSources;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class TeamContentProvider extends ContentProvider{

    private DbHelper dbHelper;

    private static final int ALL_TEAMS = 1;
    private static final int SINGLE_TEAM = 2;

    private static final String AUTHORITY = "es.encalient.scorecenter.teamcontentprovider";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/teams");

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "teams", ALL_TEAMS);
        uriMatcher.addURI(AUTHORITY, "teams/#", SINGLE_TEAM);
    }

    @Override
    public boolean onCreate() {
        // get access to the database helper
        dbHelper = new DbHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case ALL_TEAMS:
                return "vnd.android.cursor.dir/vnd.es.encalient.scorecenter.teamcontentprovider.teams";
            case SINGLE_TEAM:
                return "vnd.android.cursor.item/vnd.es.encalient.scorecenter.teamcontentprovider.teams";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
         SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_TEAMS:
                //do nothing
                break;
             case SINGLE_TEAM:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(FavoriteTeamDataSource.COLUMN_ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        long id = db.insert(FavoriteTeamDataSource.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FavoriteTeamDataSource.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ALL_TEAMS:
                //do nothing
                break;
            case SINGLE_TEAM:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(FavoriteTeamDataSource.COLUMN_ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_TEAMS:
                //do nothing
                break;
            case SINGLE_TEAM:
                String id = uri.getPathSegments().get(1);
                selection = FavoriteTeamDataSource.COLUMN_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(FavoriteTeamDataSource.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_TEAMS:
                //do nothing
                break;
            case SINGLE_TEAM:
                String id = uri.getPathSegments().get(1);
                selection = FavoriteTeamDataSource.COLUMN_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(FavoriteTeamDataSource.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

}