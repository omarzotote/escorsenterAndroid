package encalient.es.scorecenter.DataAccess.Loaders;

import android.content.Context;
import android.content.SharedPreferences;

//import encalient.es.DataAccess.scorecenter;
import encalient.es.scorecenter.Common.SharedSettings;

import java.util.Map;
import java.util.Stack;

/**
 * Created by encalientesmac2 on 21/01/15.
 */
public class SharedPreferencesLoader {
    protected static Stack<Map.Entry<String, Integer>> integers = new Stack<Map.Entry<String, Integer>>();
    protected static Stack<Map.Entry<String, Long>> longs = new Stack<Map.Entry<String, Long>>();
    protected static Stack<Map.Entry<String, Float>> floats = new Stack<Map.Entry<String, Float>>();
    protected static Stack<Map.Entry<String, String>> strings = new Stack<Map.Entry<String, String>>();
    protected static Stack<Map.Entry<String, Boolean>> booleans = new Stack<Map.Entry<String, Boolean>>();

    public Context context;

    private static SharedPreferences settings = null;
    private static SharedPreferences.Editor editor = null;

    private static SharedPreferencesLoader instance = null;

    private  SharedPreferencesLoader(Context ctx) {
        context = ctx;
        settings = context.getSharedPreferences(SharedSettings.sharedProperties, 0);
        editor = settings.edit();
    }

    public SharedPreferencesLoader getInstance(Context ctx) {
        if(instance == null) {
            instance = new SharedPreferencesLoader(ctx);
        }
        return instance;
    }

    public void putBoolean(String k, Boolean v) {
        booleans.add(new Entry<String, Boolean>(k, v));
    }
    public void putInt(String k, Integer v) {
        integers.add(new Entry<String, Integer>(k, v));
    }
    public void putLong(String k, Long v) {
        longs.add(new Entry<String, Long>(k, v));
    }
    public void putFloat(String k, Float v) {
        floats.add(new Entry<String, Float>(k, v));
    }
    public void putString(String k, String v) {
        strings.add(new Entry<String, String>(k, v));
    }
    public boolean getBoolean(String k, boolean d) {
        return settings.getBoolean(k, d);
    }
    public int getInt(String k, int d) {
        return settings.getInt(k, d);
    }
    public long getLong(String k, long d) {
        return settings.getLong(k, d);
    }
    public float getFloat(String k, float d) {
        return settings.getFloat(k, d);
    }
    public String getString(String k, String d) {
        return settings.getString(k, d);
    }

    final class Entry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        public Entry(K k, V v) {
            key = k;
            value = v;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = value;
            value = value;
            return old;
        }
    }
}
