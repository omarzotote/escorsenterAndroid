package encalient.es.scorecenter.DataAccess.DataSources;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by EnCalientes PC 3 on 3/24/2015.
 */
public abstract class DataSource<T> {
    protected SQLiteDatabase mDatabase;
    public DataSource(SQLiteDatabase database){
        mDatabase = database;
    }

    public abstract boolean insert(T entity);
    public abstract boolean delete(T entity);
    public abstract boolean update(T entity);
    public abstract List read();
    public abstract List read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy);

}
