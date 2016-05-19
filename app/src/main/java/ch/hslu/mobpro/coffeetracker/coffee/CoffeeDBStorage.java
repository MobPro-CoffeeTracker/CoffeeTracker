package ch.hslu.mobpro.coffeetracker.coffee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

class CoffeeDBStorage implements ICoffeeStorage {
    private final SQLiteDatabase database;
    private final SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy MM DD HH mm ss SSS z");

    public CoffeeDBStorage(Context context) {
        database = new CoffeeDbHelper(context).getWritableDatabase();
    }


    @Override
    public void storeLocation(Date timestamp, Location location) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CoffeeEntry.COLUMN_NAME_DATE, parserSDF.format(timestamp));
        values.put(CoffeeEntry.COLUMN_NAME_LATITUDE, location.getLatitude());
        values.put(CoffeeEntry.COLUMN_NAME_LONGITUDE, location.getLongitude());
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = database.insert(
                CoffeeEntry.TABLE_NAME,
                null,
                values);
    }

    @Override
    public void clearAll() {
        database.execSQL(CoffeeEntry.SQL_DELETE_ALL_VALUES);
    }

    @Override
    public Map<Date, Location> getAll() {

        String[] projection = {
                CoffeeEntry.COLUMN_NAME_DATE,
                CoffeeEntry.COLUMN_NAME_LATITUDE,
                CoffeeEntry.COLUMN_NAME_LONGITUDE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                CoffeeEntry.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = database.query(
                CoffeeEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();

        Map<Date, Location> values = new LinkedHashMap();
        while (!cursor.isAfterLast()) {
            try {
                Location location = new Location("CoffeeDBStorage");
                location.setLatitude(cursor.getLong(cursor.getColumnIndex(CoffeeEntry.COLUMN_NAME_LATITUDE)));
                location.setLongitude(cursor.getLong(cursor.getColumnIndex(CoffeeEntry.COLUMN_NAME_LONGITUDE)));

                values.put(parserSDF.parse(
                        cursor.getString(cursor.getColumnIndex(CoffeeEntry.COLUMN_NAME_DATE))),
                        location);
            } catch (Exception e) {
                Log.e("CoffeeDBStorage", e.getMessage());
            }

            cursor.moveToNext();
        }

        cursor.close();

        return values;
    }

    private class CoffeeDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Coffee.db";

        public CoffeeDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CoffeeEntry.SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(CoffeeEntry.SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private static abstract class CoffeeEntry implements BaseColumns {
        public static final String TABLE_NAME = "CoffeeList";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LATITUDE = "latitude";

        private static final String TEXT_TYPE = " TEXT";
        private static final String DOUBLE_TYPE = " DOUBLE";
        private static final String COMMA_SEP = ",";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + CoffeeEntry.TABLE_NAME + " (" +
                        CoffeeEntry._ID + " INTEGER PRIMARY KEY," +
                        CoffeeEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                        CoffeeEntry.COLUMN_NAME_LATITUDE + DOUBLE_TYPE + COMMA_SEP +
                        COLUMN_NAME_LONGITUDE + DOUBLE_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + CoffeeEntry.TABLE_NAME;

        private static final String SQL_DELETE_ALL_VALUES = "delete from " + TABLE_NAME;

    }
}